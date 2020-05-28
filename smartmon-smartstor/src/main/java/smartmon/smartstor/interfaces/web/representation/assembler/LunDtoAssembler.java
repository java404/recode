package smartmon.smartstor.interfaces.web.representation.assembler;

import com.alibaba.fastjson.util.TypeUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import smartmon.smartstor.domain.model.Lun;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.web.dto.LunDto;
import smartmon.smartstor.web.dto.SimpleLunDto;
import smartmon.utilities.misc.BeanConverter;

public class LunDtoAssembler {
  public static CachedData<SimpleLunDto> toSimpleDtos(CachedData<Lun> cachedData) {
    if (cachedData == null || CollectionUtils.isEmpty(cachedData.getData())) {
      return null;
    }
    List<SimpleLunDto> lunDtos = cachedData
      .getData().stream().map(LunDtoAssembler::toSimpleDto).collect(Collectors.toList());
    return new CachedData<>(lunDtos, cachedData.isExpired(), cachedData.getError());
  }

  private static SimpleLunDto toSimpleDto(Lun lun) {
    SimpleLunDto simpleLunDto = BeanConverter.copy(lun, SimpleLunDto.class);
    simpleLunDto.setLunTypeDisplay(lun.getLunType().getName());
    simpleLunDto.setLunName(lun.getExtNodeName() + "_" + lun.getLunName());
    if (StringUtils.isNotBlank(lun.getShowStatus())) {
      simpleLunDto.setCusStatus(lun.getShowStatus());
    } else {
      simpleLunDto.setCusStatus(getLunState(lun));
    }
    return simpleLunDto;
  }

  public static LunDto toDto(Lun lun) {
    if (lun == null) {
      return null;
    }
    LunDto lunDto = BeanConverter.copy(lun, LunDto.class);
    lunDto.setLunName(lun.getExtNodeName() + "_" + lun.getLunName());
    lunDto.setLunTypeDisplay(lun.getLunType().getName());
    lunDto.setSize(lun.getExtSize());
    lunDto.setCacheSize(lun.getExtCacheSize());
    getDataDisk(lun, lunDto);
    getCacheDisk(lun, lunDto);
    if (StringUtils.isNotBlank(lun.getShowStatus())) {
      lunDto.setCusStatus(lun.getShowStatus());
    } else {
      lunDto.setCusStatus(getLunState(lun));
    }
    return lunDto;
  }

  private static void getCacheDisk(Lun lun, LunDto lunDto) {
    String cacheDiskName = lun.getExtCacheDiskName();
    String cacheDevName = lun.getExtCacheDevNames();
    String cacheDisk = null;
    if (StringUtils.isNotBlank(cacheDiskName)) {
      cacheDisk = cacheDiskName;
    }
    if (StringUtils.isNotBlank(cacheDevName)) {
      if (StringUtils.isNotBlank(cacheDisk)) {
        cacheDisk += "(" + cacheDevName + ")";
      } else {
        cacheDisk = cacheDevName;
      }
    }
    lunDto.setCacheDisk(cacheDisk);
  }

  private static void getDataDisk(Lun lun, LunDto lunDto) {
    String dataDevName = lun.getExtDataDevName();
    String dataDiskName = lun.getExtDataDiskName();
    lunDto.setDevName(dataDevName);
    String dataDisk = null;
    if (StringUtils.isNotBlank(dataDiskName)) {
      dataDisk = dataDiskName;
    }
    if (StringUtils.isNotBlank(dataDevName)) {
      if (StringUtils.isNotBlank(dataDisk)) {
        dataDisk += "(" + dataDevName + ")";
      } else {
        dataDisk = dataDevName;
      }
    }
    lunDto.setDataDisk(dataDisk);
  }

  // 获取Lun状态
  private static String getLunState(Lun lun) {
    Boolean configState = lun.getConfigState();
    Boolean actualState = lun.getActualState();
    String asmStatus = lun.getAsmStatus();
    String cusState = null;
    if (Boolean.TRUE.equals(configState) && Boolean.TRUE.equals(actualState)) {
      if (org.apache.commons.lang3.StringUtils.isNotBlank(asmStatus)) {
        cusState = asmStatus;
      } else {
        cusState = "ONLINE";
      }
      Integer ioError = lun.getExportIoError();
      if (ioError != null && ioError != 0) {
        cusState = "FAULTY";
      }
      boolean isFaulty = false;
      Boolean dataActualState = TypeUtils.castToBoolean(lun.getExtDataActualState());
      if (Boolean.FALSE.equals(dataActualState)) {
        cusState = "FAULTY";
        isFaulty = true;
      }

      if (!isFaulty) {
        String cacheActualStates = lun.getExtCacheActualStates();
        if (cacheActualStates != null && cacheActualStates.length() > 0) {
          String[] states = cacheActualStates.split(",");
          for (String state : states) {
            Boolean stat = TypeUtils.castToBoolean(state);
            if (Boolean.FALSE.equals(stat)) {
              cusState = "FAULTY";
            }
          }
        }
      }
    }

    if (Boolean.TRUE.equals(configState) && Boolean.FALSE.equals(actualState)) {
      cusState = "MISSING";
    }

    if (Boolean.FALSE.equals(configState)) {
      cusState = "OFFLINE";
    }
    return cusState;
  }
}
