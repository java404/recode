package smartmon.smartstor.web.dto;

import lombok.Data;

@Data
public class SimpleDiskDto {
  private String diskName;
  private String devName;
  private String diskTypeName;
  private Long size;
  private Long extFreeSize;
  private Boolean actualState;
  private String health;
  private Boolean isRaid;
  private String raidType; // Raid
  private String raidCes;
  private String raidLedOperate;
  private String raidLedOperateResult;
  private String raidLedState;
  private String hddHealth;
  private String ssdHealth;
}
