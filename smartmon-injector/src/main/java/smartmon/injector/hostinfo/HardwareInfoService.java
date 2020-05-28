package smartmon.injector.hostinfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.HardwareInfo;
import smartmon.injector.executors.ExecutorService;

@Slf4j
@Service
public class HardwareInfoService {
  @Autowired
  private ExecutorService executorService;

  public HardwareInfo getHardwareInfo() {
    HardwareInfo hardwareInfo = new HardwareInfo();
    try {
      hardwareInfo.setCpuInfo(retrieveCpuInfo());
      hardwareInfo.setMemInfo(retrieveMemInfo());
      hardwareInfo.setMainboardInfo(retrieveMainboardInfo());
      hardwareInfo.setMountInfos(retrieveMountInfos());
      return hardwareInfo;
    } catch (Exception err) {
      log.error("get system info error:", err);
      return null;
    }
  }

  private HardwareInfo.CpuInfo retrieveCpuInfo() {
    HardwareInfo.CpuInfo cpuInfo = new HardwareInfo.CpuInfo();
    String command = "grep 'model name' /proc/cpuinfo | cut -f2 -d: | head -1 | awk '$1=$1'";
    cpuInfo.setProcessorModel(executorService.executeShellCommand(command));
    command = "grep 'physical id' /proc/cpuinfo | sort -u | wc -l";
    cpuInfo.setProcessorCores(Integer.valueOf(executorService.executeShellCommand(command)));
    command = "grep 'core id' /proc/cpuinfo | sort -u | wc -l";
    cpuInfo.setProcessorCount(Integer.valueOf(executorService.executeShellCommand(command)));
    command = "grep 'processor' /proc/cpuinfo | sort -u | wc -l";
    cpuInfo.setLogicProcessorCount(Integer.valueOf(executorService.executeShellCommand(command)));
    Integer threadsPerCore = cpuInfo.getLogicProcessorCount() / cpuInfo.getProcessorCount()
      / cpuInfo.getProcessorCores();
    cpuInfo.setThreadsPerCore(threadsPerCore);
    return cpuInfo;
  }

  private HardwareInfo.MemInfo retrieveMemInfo() {
    HardwareInfo.MemInfo memInfo = new HardwareInfo.MemInfo();
    String command = "grep MemTotal /proc/meminfo | awk '{print $2}'";
    memInfo.setMemoryTotal(Long.parseLong(executorService.executeShellCommand(command)));
    command = "grep SwapTotal /proc/meminfo | awk '{print $2}'";
    memInfo.setSwapTotal(Long.parseLong(executorService.executeShellCommand(command)));
    return memInfo;
  }

  private HardwareInfo.MainboardInfo retrieveMainboardInfo() {
    HardwareInfo.MainboardInfo mainboardInfo = new HardwareInfo.MainboardInfo();
    String command = "dmidecode -s bios-version";
    mainboardInfo.setBiosVersion(executorService.executeShellCommand(command));
    return mainboardInfo;
  }

  private List<HardwareInfo.MountInfo> retrieveMountInfos() {
    List<HardwareInfo.MountInfo> mountInfos = Lists.newArrayList();
    String mountPoints = executorService.executeShellCommand("lsblk -n | awk '{print $NF}' | sort -u | grep '^/'");
    String mountDetails = executorService.executeShellCommand("df -T | sort -k7");
    Set<String> mountPointSet = Sets.newHashSet(mountPoints.split("\n"));
    Set<String> mountDetailSet = Sets.newLinkedHashSet(Arrays.asList(mountDetails.split("\n")));
    for (String mountDetail : mountDetailSet) {
      String[] mountDetailArray = mountDetail.split(" +");
      Arrays.stream(mountDetailArray).forEach(log::warn);
      String mountPoint = mountDetailArray[6];
      if (!mountPointSet.contains(mountPoint)) {
        continue;
      }
      HardwareInfo.MountInfo mountInfo = new HardwareInfo.MountInfo();
      mountInfo.setDevice(mountDetailArray[0]);
      mountInfo.setFsType(mountDetailArray[1]);
      mountInfo.setTotalSize(Long.parseLong(mountDetailArray[2]) * 1024);
      mountInfo.setAvailableSize(Long.parseLong(mountDetailArray[4]) * 1024);
      mountInfo.setMountPoint(mountPoint);
      mountInfos.add(mountInfo);
    }
    return mountInfos;
  }
}
