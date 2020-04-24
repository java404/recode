package smartmon.smartstor.infra.remote;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.model.ApiVersion;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.domain.model.StorageNode;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.infra.remote.client.PbDataClient;
import smartmon.smartstor.infra.remote.client.PbDataClientService;
import smartmon.smartstor.infra.remote.types.PbDataApiVersion;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskAddParam;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfo;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeItem;
import smartmon.utilities.misc.BeanConverter;

@Component
@Slf4j
public class SmartstorApiProxy implements SmartstorApiService {
  @Autowired
  private PbDataClientService pbDataClientService;

  @Value("${smartmon.smartstor.apiConnectTimeout:9000}")
  private int smartStorApiPort = 9000;

  private int getSmartStorApiPort() {
    return smartStorApiPort;
  }

  @Override
  public ApiVersion getApiVersion(String serviceIp) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final PbDataApiVersion apiVersionInfo = client.getApiVersionInfo();
    return BeanConverter.copy(apiVersionInfo, ApiVersion.class);
  }

  @Override
  public List<StorageNode> getNodes(String serviceIp) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final List<PbDataNodeItem> pbDataNodeInfos = client.listNodes().getNodeInfos();
    return BeanConverter.copy(pbDataNodeInfos, StorageNode.class);
  }

  @Override
  public List<StorageHost> getHosts(String serviceIp) {
    List<StorageNode> nodes = getNodes(serviceIp);
    Map<String, StorageHost> hostMap = Maps.newHashMap();
    for (StorageNode node : nodes) {
      StorageHost host = BeanConverter.copy(node, StorageHost.class);
      StorageHost hostInMap = hostMap.get(host.getHostKey());
      if (hostInMap == null) {
        hostMap.put(host.getHostKey(), host);
        continue;
      }
      hostInMap.setSysMode(SysModeEnum.MERGE);
      hostInMap.setNodeName(host.getNodeName().replaceAll("[d,s]u", "hu"));
    }
    return new ArrayList<>(hostMap.values());
  }

  @Override
  public StorageNode getNodeInfo(String serviceIp) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final PbDataNodeItem nodeInfo = client.getNodeInfo();
    return BeanConverter.copy(nodeInfo, StorageNode.class);
  }

  @Override
  public List<Disk> getDisks(List<String> serviceIps) {
    List<Disk> diskList = Lists.newArrayList();
    for (final String serviceIp : serviceIps) {
      final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
      final List<Disk> disks = client.listDisks().getDisks();
      diskList.addAll(disks);
    }
    return diskList;
  }

  @Override
  public Disk getDiskInfo(String serviceIp, String diskName) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final PbDataDiskInfo diskInfo = client.getDiskInfo(diskName);
    final Disk copyDiskInfo = BeanConverter.copy(diskInfo, Disk.class);
    if (diskInfo.getNvmeDiskHealthInfo() != null && copyDiskInfo != null) {
      BeanUtils.copyProperties(diskInfo.getNvmeDiskHealthInfo(), copyDiskInfo);
    }
    return copyDiskInfo;
  }

  @Override
  public void addDisk(String serviceIp, String devName, Integer partitionCount, String diskType) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    PbDataResponseCode pbDataResponseCode = client.diskAdd(
      new PbDataDiskAddParam(devName, partitionCount, diskType));
    System.out.println(pbDataResponseCode);
  }

  @Override
  public void delDisk(String serviceIp, String diskName) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    PbDataResponseCode pbDataResponseCode = client.diskDel(diskName);
    System.out.println(pbDataResponseCode);
  }

  @Override
  public void diskRaidLedOnState(String serviceIp, String cesAddr) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final PbDataResponseCode pbDataResponseCode = client.diskRaidLedOnState(cesAddr);
    System.out.println(pbDataResponseCode);
  }

  @Override
  public void diskRaidLedOffState(String serviceIp, String cesAddr) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final PbDataResponseCode pbDataResponseCode = client.diskRaidLedOnState(cesAddr);
    System.out.println(pbDataResponseCode);
  }
}
