package smartmon.smartstor.interfaces.web.representation.assembler;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import smartmon.smartstor.domain.model.Pool;
import smartmon.smartstor.domain.model.PoolDiskInfo;
import smartmon.smartstor.domain.model.enums.PoolCacheModeEnum;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.web.dto.PoolDto;
import smartmon.smartstor.web.dto.SimplePoolDto;
import smartmon.utilities.misc.BeanConverter;

@Slf4j
public class PoolDtoAssembler {
  public static CachedData<SimplePoolDto> toSimpleDtos(CachedData<Pool> cachedData) {
    if (cachedData == null || CollectionUtils.isEmpty(cachedData.getData())) {
      return null;
    }
    List<SimplePoolDto> poolDtos = cachedData
      .getData().stream().map(PoolDtoAssembler::toSimpleDto).collect(Collectors.toList());
    return new CachedData<>(poolDtos, cachedData.isExpired(), cachedData.getError());
  }

  private static SimplePoolDto toSimpleDto(Pool pool) {
    SimplePoolDto simplePoolDto = BeanConverter.copy(pool, SimplePoolDto.class);

    return BeanConverter.copy(pool, SimplePoolDto.class);
  }

  public static CachedData<PoolDto> toDtos(CachedData<Pool> cachedData) {
    if (cachedData == null || CollectionUtils.isEmpty(cachedData.getData())) {
      return null;
    }
    List<PoolDto> poolDtos = cachedData
      .getData().stream().map(PoolDtoAssembler::toDto).collect(Collectors.toList());
    return new CachedData<>(poolDtos, cachedData.isExpired(), cachedData.getError());
  }

  private static PoolDto toDto(Pool pool) {
    PoolDto poolDto = BeanConverter.copy(pool, PoolDto.class);
    formatData(pool, poolDto);
    return poolDto;
  }


  public static void formatData(Pool pool, PoolDto poolDto) {
    try {
      Integer syncLevel = pool.getSyncLevel();
      poolDto.setSyncLevel((syncLevel == null || syncLevel == 0) ? 1 : syncLevel);
      Integer skipThresh = pool.getSkipThresh();
      poolDto.setSkipThresh(skipThresh == null ? 0 : skipThresh);

      Integer lower = pool.getDirtyThreshLower();
      Integer upper = pool.getDirtyThreshUpper();
      if (lower == null || lower == 0) {
        lower = 29;
      }
      if (upper == null || upper == 0) {
        upper = 69;
      }
      poolDto.setDirtyThreshLower(lower);
      poolDto.setDirtyThreshUpper(upper);
      poolDto.setDirtyLu(lower + "%/" + upper + "%");
      // dirty N/P
      Long dirty = pool.getExportDirty();
      Double dirtyP = pool.getExportPDirty();
      poolDto.setDirtyNp(dirty + "/" + converDoubleTwo(dirtyP) + "%");

      // Valid N/P
      Long valid = pool.getExportValid();// export_infoJson.getLong("valid");
      Double validP = pool.getExportPValid(); // export_infoJson.getDouble("p_valid");
      poolDto.setValidNp(valid + "/" + converDoubleTwo(validP) + "%");

      Long exportMaxSize = pool.getExportMaxSize(); // max size
      Long exportSize = pool.getExportSize(); // pool size
      Long pmtSize = pool.getExtPoolInfoPoolPmtSize(); // lun size

      poolDto.setSize(exportSize);
      if (exportSize != null) {
        poolDto.setCacheSize(exportSize * 512);
      }
      if (pmtSize != null) {
        poolDto.setPmtSize(pmtSize * 512);
      }

      String exportSizeStr = sizeConverGB(exportSize);
      String pmtSizeStr = sizeConverGB(pmtSize);

      Long freeSize = null; // free size
      if (exportMaxSize != null && exportMaxSize > 0) {
        if (exportSize != null && exportSize > 0) {
          freeSize = exportMaxSize - exportSize;
        }
        if (pmtSize != null && pmtSize > 0) {
          if (freeSize != null) {
            freeSize -= pmtSize;
          } else {
            freeSize = exportMaxSize - pmtSize;
          }
        }
      }

      if (freeSize != null) {
        poolDto.setFreeSize(freeSize * 512);
      }

      String freeSizeStr = sizeConverGB(freeSize);
      poolDto.setCachePmtFree(exportSizeStr + "/" + pmtSizeStr + "/" + freeSizeStr);

      // ext_poolinfo_pool_cache_model
      PoolCacheModeEnum poolCacheModel = pool.getExtPoolInfoPoolCacheModel();
      poolDto.setPoolCacheMode(poolCacheModel == null ? PoolCacheModeEnum.UNKNOWN.getName() : poolCacheModel.getName());

      // 磁盘信息
      String disk = null;
      PoolDiskInfo diskInfo = pool.getDiskInfo();
      String diskName = diskInfo.getDiskName();
      String diskDevName = diskInfo.getDiskDevName();
      if (StringUtils.isNotBlank(diskName)) {
        disk = diskName;
      }
      if (StringUtils.isNotBlank(diskDevName)) {
        if (StringUtils.isNotBlank(disk)) {
          disk += "(" + diskDevName + ")";
        } else {
          disk = diskDevName;
        }
      }
      poolDto.setDisk(disk);
    } catch (Exception e) {
      log.warn("Format pool info failed:{}", pool.getPoolName(), e);
    }
  }

  public static String converDoubleTwo(Double dou) {
    if (dou == null) {
      return "";
    } else if (dou == 0) {
      return "0";
    } else {
      return String.format("%.2f", dou);
    }
  }

  public static String sizeConverGB(Long size) {
    String rtn = "0G";
    DecimalFormat df = new DecimalFormat("0");
    if (size != null && size > 0) {
      rtn = df.format((double) size * 512 / 1073741824) + "G";
    }
    return rtn;
  }
}
