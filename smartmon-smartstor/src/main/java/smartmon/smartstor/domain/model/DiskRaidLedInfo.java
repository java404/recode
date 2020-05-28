package smartmon.smartstor.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import smartmon.smartstor.domain.model.enums.DiskRaidLedOperateEnum;
import smartmon.smartstor.domain.model.enums.DiskRaidLedStateEnum;

@Data
@EqualsAndHashCode(callSuper = false)
public class DiskRaidLedInfo {
  private String diskUuid; //serviceIp + cesAddr
  private DiskRaidLedOperateEnum diskRaidLedOpt;
  private DiskRaidLedStateEnum diskRaidLedState;
  private String listenIp;
  private String cesAddr;

  public String getDiskUuid() {
    if (StringUtils.isBlank(listenIp) || StringUtils.isBlank(cesAddr)) {
      return "";
    }
    return listenIp + "_" + cesAddr;
  }
}
