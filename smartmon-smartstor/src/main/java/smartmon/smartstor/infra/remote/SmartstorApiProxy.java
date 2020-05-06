package smartmon.smartstor.infra.remote;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.model.ApiVersion;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.domain.model.Group;
import smartmon.smartstor.domain.model.Lun;
import smartmon.smartstor.domain.model.Pool;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.domain.model.StorageNode;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.infra.remote.client.PbDataClient;
import smartmon.smartstor.infra.remote.client.PbDataClientService;
import smartmon.smartstor.infra.remote.types.PbDataApiVersion;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskAddParam;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfo;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfos;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupInfos;
import smartmon.smartstor.infra.remote.types.lun.PbDataAsmDiskInfo;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunInfo;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunInfoBackendRes;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunInfos;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeInfos;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeItem;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolInfo;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolInfos;
import smartmon.utilities.misc.BeanConverter;

@Component
@Slf4j
public class SmartstorApiProxy implements SmartstorApiService {
  @Autowired
  private PbDataClientService pbDataClientService;

  @Value("${smartmon.smartstor.port:9000}")
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
    final PbDataNodeInfos pbDataNodeInfos = client.listNodes();
    if (pbDataNodeInfos != null) {
      return ListUtils.emptyIfNull(BeanConverter.copy(pbDataNodeInfos.getNodeInfos(), StorageNode.class));
    }
    return Collections.emptyList();
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
  public List<Disk> getDisks(String serviceIp) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final PbDataDiskInfos pbDataDiskInfos = client.listDisks();
    if (pbDataDiskInfos != null) {
      return ListUtils.emptyIfNull(pbDataDiskInfos.getDisks());
    }
    return Collections.emptyList();
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

  @Override
  public List<Group> getGroups(String serviceIp) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final PbDataGroupInfos pbDataGroupInfos = client.listGroups();
    if (pbDataGroupInfos != null) {
      return ListUtils.emptyIfNull(BeanConverter.copy(pbDataGroupInfos.getGroupInfos(), Group.class));
    }
    return Collections.emptyList();
  }

  @Override
  public List<Pool> getPools(String serviceIp) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final PbDataPoolInfos pbDataPoolInfos = client.listPools();
    if (pbDataPoolInfos != null) {
      return convertPools(pbDataPoolInfos.getPoolInfos());
    }
    return Collections.emptyList();
  }

  private List<Pool> convertPools(List<PbDataPoolInfo> poolInfos) {
    if (CollectionUtils.isEmpty(poolInfos)) {
      return Collections.emptyList();
    }
    List<Pool> pools = new ArrayList<>();
    for (PbDataPoolInfo pbdataPoolInfo : poolInfos) {
      final Pool pool = BeanConverter.copy(pbdataPoolInfo, Pool.class);
      if (pool != null) {
        if (pbdataPoolInfo.getDirtyThresh() != null) {
          BeanUtils.copyProperties(pbdataPoolInfo.getDirtyThresh(), pool);
        }
        if (pbdataPoolInfo.getExportInfo() != null) {
          BeanUtils.copyProperties(pbdataPoolInfo.getExportInfo(), pool);
        }
        pools.add(pool);
      }
    }
    return pools;
  }

  @Override
  public List<Lun> getLuns(String serviceIp) {
    final PbDataClient client = pbDataClientService.getClient(serviceIp, getSmartStorApiPort());
    final PbDataLunInfos lunInfos = client.listluns();
    if (lunInfos != null) {
      return convertLuns(lunInfos.getLunInfos());
    }
    return Collections.emptyList();
  }

  private List<Lun> convertLuns(List<PbDataLunInfo> lunInfos) {
    if (CollectionUtils.isEmpty(lunInfos)) {
      return Collections.emptyList();
    }
    List<Lun> luns = new ArrayList<>();
    for (PbDataLunInfo pbdataLunInfo : lunInfos) {
      final Lun lun = BeanConverter.copy(pbdataLunInfo, Lun.class);
      if (lun != null) {
        lun.setConfigState(pbdataLunInfo.getConfigState());
        lun.setShowStatus(pbdataLunInfo.getShowStatus());
        lun.setAsmStatus(pbdataLunInfo.getAsmStatus());
        lun.setActualState(pbdataLunInfo.getActualState());
        final PbDataLunInfoBackendRes lunInfoBackendRes = pbdataLunInfo.getLunInfoBackendRes();
        if (lunInfoBackendRes != null) {
          lun.setExtDataDiskName(lunInfoBackendRes.getDataDiskName());
          if (lunInfoBackendRes.getPalCacheLiBr() != null) {
            lun.setExtCacheDevNames(lunInfoBackendRes.getPalCacheLiBr().getCacheDevName());
            lun.setExtCacheDiskName(lunInfoBackendRes.getPalCacheLiBr().getCacheDiskName());
            lun.setExtCacheSize(lunInfoBackendRes.getPalCacheLiBr().getCacheSize());
          }
        }
        final PbDataAsmDiskInfo asmDiskInfo = pbdataLunInfo.getAsmDiskInfo();
        if (asmDiskInfo != null) {
          lun.setLastHeartbeatTime(asmDiskInfo.getLastHeartbeatTime());
        }
        luns.add(lun);
      }
    }
    return luns;
  }
}
