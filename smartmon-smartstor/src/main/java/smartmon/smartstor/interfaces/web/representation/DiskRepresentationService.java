package smartmon.smartstor.interfaces.web.representation;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.web.result.LastGraphValueResult;
import smartmon.falcon.web.vo.LastGraphValueCompareQueryVo;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.domain.model.DiskPart;
import smartmon.smartstor.domain.model.DiskRaidLedInfo;
import smartmon.smartstor.domain.model.RaidDiskInfo;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.infra.cache.DataCacheManager;
import smartmon.smartstor.infra.feign.SmartmonFalconFeignClient;
import smartmon.smartstor.web.dto.AvailableDiskDto;
import smartmon.smartstor.web.dto.DiskDto;
import smartmon.smartstor.web.dto.DiskHealthDto;
import smartmon.smartstor.web.dto.DiskNvmeHealthDto;
import smartmon.smartstor.web.dto.DiskPartDto;
import smartmon.smartstor.web.dto.SimpleDiskDto;
import smartmon.smartstor.web.dto.StorageDiskDto;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.BeanConverter;

@Service
@Slf4j
public class DiskRepresentationService extends BaseRepresentationService {
  @Autowired
  private DataCacheManager dataCacheManager;
  @Autowired
  private SmartmonFalconFeignClient falconFeignClient;

  public CachedData<DiskDto> getDisks(String serviceIp, boolean getHealthInfo) {
    return getDisks(storageHostRepository.findByServiceIp(serviceIp), getHealthInfo);
  }

  public CachedData<DiskDto> getDisks(StorageHost host, boolean getHealthInfo) {
    if (host == null) {
      return null;
    }
    CachedData<Disk> cachedData = dataCacheManager.gets(host.getListenIp(), Disk.class);
    if (cachedData == null) {
      return null;
    }
    List<Disk> data = cachedData.getData();
    List<DiskDto> diskDtos = new ArrayList<>();
    Map<String, LastGraphValueResult> healthMap = null;
    if (getHealthInfo) {
      healthMap = getHealthMap(host.getGuid());
    }
    for (Disk disk : data) {
      DiskDto diskDto = BeanConverter.copy(disk, DiskDto.class);
      if (diskDto == null) {
        continue;
      }
      formatDisk(host, healthMap, disk, diskDto);
      diskDtos.add(diskDto);
    }
    return new CachedData<>(diskDtos, cachedData.isExpired(), cachedData.getError());
  }

  private void formatDisk(StorageHost host,
                          Map<String, LastGraphValueResult> healthMap,
                          Disk disk,
                          DiskDto diskDto) {
    try {
      String serviceIp = host.getListenIp();
      diskDto.setDiskTypeName(disk.getDiskType() == null ? "" : disk.getDiskType().getName());
      RaidDiskInfo raidInfo = disk.getRaidInfo();
      if (raidInfo != null) {
        diskDto.setIsRaid(true);
        //diskDto.setMode(raidInfo.getModel());
        diskDto.setHealthDetail(raidInfo.getHealth());
        String ctl = raidInfo.getCtl();
        Integer eid = raidInfo.getEid();
        Integer slot = raidInfo.getSlot();
        if (ctl != null && eid != null && slot != null) {
          String ces = ctl + ":" + eid + ":" + slot;
          diskDto.setRaidCes(ces);
          getLedState(serviceIp, diskDto, ces);
        }
        diskDto.setRaidSize(raidInfo.getSize());
        diskDto.setRaidType(raidInfo.getDriveType());
        //TODO ssd health ,hdd health
        //diskDto.setSsdHealth("");
        //diskDto.setHddHealth("");
      }

      DiskNvmeHealthDto nvmeHealthDto =
        new DiskNvmeHealthDto(disk.getNvmeTotallife(), disk.getNvmeLife(), disk.getNvmeHealth());
      //nvme
      diskDto.setNvmeHealth(nvmeHealthDto);
      if (StringUtils.isNotBlank(disk.getNvmeHealth())) {
        diskDto.setHealthDetail(disk.getNvmeHealth());
      }

      String diskName = disk.getDiskName();
      if (StringUtils.isNotBlank(diskName)) {
        diskDto.setPartCount((long) disk.getDiskParts().size());
        List<DiskPartDto> partDtos = BeanConverter.copy(disk.getDiskParts(), DiskPartDto.class);
        diskDto.setParts(partDtos);
      }
      if (MapUtils.isNotEmpty(healthMap)) {
        getDiskHealth(disk, diskDto, healthMap);
      }
    } catch (Exception e) {
      log.warn("Format disk failed", e);
    }
  }

  private Map<String, LastGraphValueResult> getHealthMap(String hostUuid) {
    try {
      LastGraphValueCompareQueryVo vo = LastGraphValueCompareQueryVo.newVo(null, "disk.health", hostUuid);
      SmartMonResponse<List<LastGraphValueResult>> resp = falconFeignClient.thresholdCompare(vo);
      if (resp == null || resp.getErrno() != 0) {
        return null;
      }
      List<LastGraphValueResult> result = resp.getContent();
      if (CollectionUtils.isEmpty(result)) {
        return null;
      }
      return result
        .stream()
        .collect(Collectors.toMap(LastGraphValueResult::getCounter, Function.identity(), (a, b) -> b));
    } catch (Exception e) {
      log.warn("Failed to get disk health from falcon", e);
    }
    return null;
  }

  private void getDiskHealth(Disk disk, DiskDto diskDto, Map<String, LastGraphValueResult> healthMap) {
    try {
      long unhealthyCount = 0;
      String devName = disk.getDevName();
      if (StringUtils.isBlank(devName)) {
        return;
      }
      devName = devName.substring(devName.lastIndexOf("/") + 1);
      String tag = "device=" + devName;
      List<DiskHealthDto> diskHealthDtos = new ArrayList<>();
      for (Map.Entry<String, LastGraphValueResult> entry : healthMap.entrySet()) {
        String key = entry.getKey();
        if (!key.contains(tag) && !key.contains(disk.getDevName())) { //device=sda || device=/dev/nvme0n1
          continue;
        }
        LastGraphValueResult graphValueResult = entry.getValue();
        DiskHealthDto diskHealthDto = convertDiskHealth(graphValueResult);
        if (!diskHealthDto.isHealth()) {
          unhealthyCount++;
        }
        diskHealthDtos.add(diskHealthDto);
      }
      if (CollectionUtils.isEmpty(diskHealthDtos)) {
        return;
      }
      diskDto.setHasHealth(true);
      diskDto.setHealths(diskHealthDtos);
      diskDto.setHealth(unhealthyCount == 0);
    } catch (Exception e) {
      log.warn("Get disk health failed: {}", disk.getDevName(), e);
    }
  }

  private DiskHealthDto convertDiskHealth(LastGraphValueResult result) {
    DiskHealthDto disk = BeanConverter.copy(result, DiskHealthDto.class);
    disk.setValue(BeanConverter.copy(result.getValue(), DiskHealthDto.GraphValue.class));
    return disk;
  }

  private void getPartNos(Disk disk, AvailableDiskDto diskDto) {
    Map<String, Integer> usedPartMap = new HashMap<>(); // 记录已使用过的分区
    // pool used part
    String diskPartToPoolNames = disk.getExtDiskpartToPoolNames();
    if (diskPartToPoolNames != null && diskPartToPoolNames.length() > 0) {
      String[] arr = diskPartToPoolNames.split(",");
      for (int i = 0; i < arr.length; i++) {
        String[] kv = arr[i].split("=");
        if (kv.length == 2) {
          String key = kv[0]; // 4506f506-dd87-11e8-a2c9-0cc47a129798.1
          if (org.apache.commons.lang3.StringUtils.isNotBlank(key) && key.contains(".")) {
            String[] keys = key.split("\\.");
            if (keys.length == 2) {
              usedPartMap.put(keys[1], i);
            }
          }
        }
      }
    }

    // lun used part
    String diskPartToLunNames = disk.getExtDiskpartToLunNames();
    if (diskPartToLunNames != null && diskPartToLunNames.length() > 0) {
      String[] arr = diskPartToLunNames.split(",");
      for (int i = 0; i < arr.length; i++) {
        String[] kv = arr[i].split("=");
        if (kv.length == 2) {
          String key = kv[0]; // 4506f506-dd87-11e8-a2c9-0cc47a129798.1
          if (org.apache.commons.lang3.StringUtils.isNotBlank(key) && key.contains(".")) {
            String[] keys = key.split("\\.");
            if (keys.length == 2) {
              usedPartMap.put(keys[1], i);
            }
          }
        }
      }
    }

    // filter available part
    List<Integer> avalParts = new ArrayList<>();
    List<DiskPart> diskParts = disk.getDiskParts();
    if (CollectionUtils.isEmpty(diskParts)) {
      return;
    }
    diskDto.setPartCount(diskParts.size());
    for (DiskPart diskPart: diskParts) {
      Integer partNo = diskPart.getDiskPart();
      if (partNo != null && !usedPartMap.containsKey(partNo + "")) {
        avalParts.add(partNo);
      }
    }
    diskDto.setPartNos(avalParts);
  }

  public List<StorageDiskDto> getDisks() {
    List<StorageHost> iosHosts = getAllIosServiceHosts();
    List<StorageDiskDto> storageDiskDtos = new ArrayList<>();
    for (StorageHost host : iosHosts) {
      try {
        StorageDiskDto storageDiskDto = BeanConverter.copy(host, StorageDiskDto.class);
        CachedData<DiskDto> cachedData = getDisks(host, true);
        if (cachedData == null) {
          continue;
        }
        List<DiskDto> data = cachedData.getData();
        storageDiskDto.setDisks(BeanConverter.copy(data, SimpleDiskDto.class));
        storageDiskDto.setHostType(host.getSysMode().getName());
        storageDiskDto.setExpired(cachedData.isExpired());
        storageDiskDto.setError(cachedData.getError());
        storageDiskDtos.add(storageDiskDto);
      } catch (Exception e) {
        log.warn("Get {} disks failed", host.getListenIp(), e);
      }
    }
    return storageDiskDtos;
  }

  private void getLedState(String serviceIp, DiskDto diskDto, String ces) {
    CachedData<DiskRaidLedInfo> ledStateCachedData =
      dataCacheManager.gets(serviceIp, DiskRaidLedInfo.class);
    if (ledStateCachedData == null) {
      return;
    }
    List<DiskRaidLedInfo> ledInfos = ledStateCachedData.getData();
    Map<String, DiskRaidLedInfo> raidDiskUuidMap = ledInfos
      .stream()
      .collect(Collectors.toMap(DiskRaidLedInfo::getDiskUuid, Function.identity(), (a, b) -> b));
    String diskUuid = serviceIp + "_" + ces;
    DiskRaidLedInfo raidLedInfo = raidDiskUuidMap.get(diskUuid);
    if (raidLedInfo != null) {
      diskDto.setRaidLedState(raidLedInfo.getDiskRaidLedState().getName());
      diskDto.setRaidLedOperate(raidLedInfo.getDiskRaidLedOpt().getName());
    }
  }

  public DiskDto getDiskInfo(String serviceIp, String diskName, String devName) {
    StorageHost host = storageHostRepository.findByServiceIp(serviceIp);
    CachedData<Disk> cachedData = dataCacheManager.gets(serviceIp, Disk.class);
    if (host == null || cachedData == null) {
      return null;
    }
    List<Disk> data = cachedData.getData();
    Disk matchDisk  = null;
    if (StringUtils.isNotBlank(diskName)) {
      matchDisk = data
        .stream()
        .filter(d -> diskName.equals(d.getDiskName()))
        .findFirst()
        .orElse(null);
    } else if (StringUtils.isNotBlank(devName)) {
      matchDisk = data
        .stream()
        .filter(d -> devName.equals(d.getDevName()))
        .findFirst()
        .orElse(null);
    }
    if (matchDisk == null) {
      return null;
    }
    DiskDto diskDto = BeanConverter.copy(matchDisk, DiskDto.class);
    formatDisk(host, getHealthMap(host.getGuid()), matchDisk, diskDto);
    return diskDto;
  }

  private List<StorageDiskDto> getDisksWithoutDiskHealth() {
    List<StorageHost> iosHosts = getAllIosServiceHosts();
    List<StorageDiskDto> storageDiskDtos = new ArrayList<>();
    for (StorageHost host : iosHosts) {
      try {
        StorageDiskDto storageDiskDto = BeanConverter.copy(host, StorageDiskDto.class);
        CachedData<DiskDto> cachedData = getDisks(host, false);
        if (cachedData == null) {
          continue;
        }
        List<DiskDto> data = cachedData.getData();
        storageDiskDto.setDisks(BeanConverter.copy(data, SimpleDiskDto.class));
        storageDiskDto.setHostType(host.getSysMode().getName());
        storageDiskDto.setExpired(cachedData.isExpired());
        storageDiskDto.setError(cachedData.getError());
        storageDiskDtos.add(storageDiskDto);
      } catch (Exception e) {
        log.warn("Get {} disks failed", host.getListenIp(), e);
      }
    }
    return storageDiskDtos;
  }

  public List<StorageDiskDto> getUninitDisks() {
    List<StorageDiskDto> storageDiskDtos = getDisksWithoutDiskHealth();
    for (StorageDiskDto storageDiskDto : storageDiskDtos) {
      try {
        List<SimpleDiskDto> disks = storageDiskDto.getDisks();
        if (CollectionUtils.isEmpty(disks)) {
          continue;
        }
        List<SimpleDiskDto> filterList = disks
          .stream()
          .filter(d -> StringUtils.isBlank(d.getDiskTypeName()) || StringUtils.isBlank(d.getDiskName()))
          .collect(Collectors.toList());
        storageDiskDto.setDisks(filterList);
      } catch (Exception e) {
        log.warn("Get uninit disks failed {}", storageDiskDto.getListenIp(), e);
      }
    }
    return storageDiskDtos;
  }

  public List<StorageDiskDto> getInitDisks(String diskType) {
    List<StorageDiskDto> storageDiskDtos = getDisksWithoutDiskHealth();
    for (StorageDiskDto storageDiskDto : storageDiskDtos) {
      try {
        List<SimpleDiskDto> disks = storageDiskDto.getDisks();
        if (CollectionUtils.isEmpty(disks)) {
          continue;
        }
        List<SimpleDiskDto> filterList = disks
          .stream()
          .filter(d -> {
            if (StringUtils.isNotBlank(d.getDiskTypeName()) && StringUtils.isNotBlank(d.getDiskName())) {
              if (StringUtils.isNotBlank(diskType)) {
                return diskType.equalsIgnoreCase(d.getDiskTypeName());
              }
              return true;
            }
            return false;
          })
          .collect(Collectors.toList());
        storageDiskDto.setDisks(filterList);
      } catch (Exception e) {
        log.warn("Get init disks failed {}", storageDiskDto.getListenIp(), e);
      }
    }
    return storageDiskDtos;
  }

  public List<StorageDiskDto> getAvailableDisks(String diskType) {
    List<StorageHost> iosHosts = getAllIosServiceHosts();
    List<StorageDiskDto> storageDiskDtos = new ArrayList<>();
    getStorageAvailableDisk(diskType, iosHosts, storageDiskDtos);
    return storageDiskDtos;
  }

  private void getStorageAvailableDisk(String diskType,
                                       List<StorageHost> iosHosts,
                                       List<StorageDiskDto> storageDiskDtos) {
    for (StorageHost host : iosHosts) {
      try {
        StorageDiskDto storageDiskDto = BeanConverter.copy(host, StorageDiskDto.class);
        CachedData<Disk> cachedData = dataCacheManager.gets(host.getListenIp(), Disk.class);
        if (cachedData == null) {
          continue;
        }
        List<Disk> data = cachedData.getData();
        List<AvailableDiskDto> diskDtos = new ArrayList<>();
        for (Disk disk : data) {
          DiskDto diskDto = BeanConverter.copy(disk, DiskDto.class);
          if (diskDto == null) {
            continue;
          }
          if (disk.getDiskType() == null
              || StringUtils.isBlank(disk.getDiskName())) {
            continue;
          }
          if (StringUtils.isNotBlank(diskType)
            && !diskType.equalsIgnoreCase(disk.getDiskType().getName())) {
            continue;
          }
          formatDisk(host, null, disk, diskDto);
          AvailableDiskDto availDisk = BeanConverter.copy(diskDto, AvailableDiskDto.class);
          getPartNos(disk, availDisk);
          if (CollectionUtils.isEmpty(availDisk.getPartNos())) {
            continue;
          }
          diskDtos.add(availDisk);
        }
        storageDiskDto.setAvailableDisks(diskDtos);
        storageDiskDto.setHostType(host.getSysMode().getName());
        storageDiskDto.setExpired(cachedData.isExpired());
        storageDiskDto.setError(cachedData.getError());
        storageDiskDtos.add(storageDiskDto);
      } catch (Exception e) {
        log.warn("Get {} disks failed", host.getListenIp(), e);
      }
    }
  }

  public List<StorageDiskDto> getAvailableDisks(String diskType, String serviceIp) {
    StorageHost storageHost = storageHostRepository.findByServiceIp(serviceIp);
    List<StorageDiskDto> storageDiskDtos = new ArrayList<>();
    getStorageAvailableDisk(diskType, Lists.newArrayList(storageHost), storageDiskDtos);
    return storageDiskDtos;
  }
}
