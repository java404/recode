package smartmon.core.hostinfo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.agent.AgentStateEnum;
import smartmon.core.hosts.BasicInfo;
import smartmon.core.hosts.HardwareInfo;
import smartmon.core.hosts.NetworkInfo;
import smartmon.core.hosts.SmartMonHostInfo;
import smartmon.core.hosts.SystemInfo;
import smartmon.core.hosts.mapper.SmartMonHostMapper;
import smartmon.core.hosts.SmartMonHost;
import smartmon.injector.client.AgentClientService;
import smartmon.utilities.misc.JsonConverter;

@Slf4j
@Service
public class HostInfoSyncService {
  @Autowired
  private SmartMonHostMapper smartMonHostMapper;
  @Autowired
  private AgentClientService agentClientService;

  public void syncHostInfo(ExecutorService executorService) {
    List<SmartMonHost> smartMonHosts = smartMonHostMapper.selectAll();
    List<Callable<Void>> callableList = smartMonHosts.stream()
      .filter(smartMonHost -> smartMonHost.getAgentState() == AgentStateEnum.INSTALL_SUCCESS)
      .map(smartMonHost -> (Callable<Void>) () -> {
        syncHostInfo(smartMonHost);
        return null;
      }).collect(Collectors.toList());
    try {
      executorService.invokeAll(callableList);
    } catch (InterruptedException err) {
      log.error("sync host info interrupted", err);
    }
  }

  private void syncHostInfo(SmartMonHost smartMonHost) {
    String serviceIp = smartMonHost.getManageIp();
    try {
      SmartMonHostInfo smartMonHostInfo = agentClientService.getHostInfo(serviceIp);
      SmartMonHost smartMonHostLatest = smartMonHostMapper.selectByPrimaryKey(smartMonHost.getHostUuid());
      if (smartMonHostLatest == null) {
        return;
      }
      BasicInfo basicInfo;
      if ((basicInfo = smartMonHostInfo.getBasicInfo()) != null) {
        if (StringUtils.isNotEmpty(basicInfo.getHostname())) {
          smartMonHostLatest.setHostname(basicInfo.getHostname());
        }
      }
      SystemInfo systemInfo;
      if ((systemInfo = smartMonHostInfo.getSystemInfo()) != null) {
        smartMonHostLatest.setSystemVendor(systemInfo.getSystemVendor());
        smartMonHostLatest.setSystem(systemInfo.getSystem());
        smartMonHostLatest.setArchitecture(systemInfo.getArchitecture());
        smartMonHostLatest.setOsFamily(systemInfo.getOsFamily());
        smartMonHostLatest.setDistribution(systemInfo.getDistribution());
        smartMonHostLatest.setKernel(systemInfo.getKernel());
      }
      HardwareInfo hardwareInfo;
      if ((hardwareInfo = smartMonHostInfo.getHardwareInfo()) != null) {
        HardwareInfo.CpuInfo cpuInfo;
        if ((cpuInfo = hardwareInfo.getCpuInfo()) != null) {
          smartMonHostLatest.setProcessorModel(cpuInfo.getProcessorModel());
          smartMonHostLatest.setProcessorCores(cpuInfo.getProcessorCores());
          smartMonHostLatest.setProcessorCount(cpuInfo.getProcessorCount());
          smartMonHostLatest.setThreadsPerCore(cpuInfo.getThreadsPerCore());
          smartMonHostLatest.setLogicProcessorCount(cpuInfo.getLogicProcessorCount());
        }
        HardwareInfo.MemInfo memInfo;
        if ((memInfo = hardwareInfo.getMemInfo()) != null) {
          smartMonHostLatest.setMemoryTotal(memInfo.getMemoryTotal());
          smartMonHostLatest.setSwapTotal(memInfo.getSwapTotal());
        }
        HardwareInfo.MainboardInfo mainboardInfo;
        if ((mainboardInfo = hardwareInfo.getMainboardInfo()) != null) {
          smartMonHostLatest.setBiosVersion(mainboardInfo.getBiosVersion());
        }
        List<HardwareInfo.MountInfo> mountInfos = hardwareInfo.getMountInfos();
        if (mountInfos != null) {
          smartMonHostLatest.setMounts(JsonConverter.writeValueAsString(mountInfos));
        }
      }
      NetworkInfo networkInfo;
      if ((networkInfo = smartMonHostInfo.getNetworkInfo()) != null) {
        smartMonHostLatest.setNetworks(JsonConverter.writeValueAsString(networkInfo));
      }
      Date date = new Date();
      smartMonHostLatest.setUpdateTime(date);
      smartMonHostLatest.setStatusTime(date);
      smartMonHostMapper.updateByPrimaryKey(smartMonHostLatest);
    } catch (Exception err) {
      log.error(String.format("Sync host info error, host[%s]", serviceIp), err);
    }
  }
}
