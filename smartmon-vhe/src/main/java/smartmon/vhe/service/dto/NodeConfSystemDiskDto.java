package smartmon.vhe.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NodeConfSystemDiskDto {
  private String diskName;
  private String diskRaid;
  private String diskSize;
  private String diskState;

  private Long hdd;
  private Long ssd;
  @JsonProperty("os_disk")
  private OsDiskInfo osDiskInfo;

  @Data
  public static class OsDiskInfo {
    private String name;
    private String raid;
    private String size;
    private String status;
  }

  public String getDiskName() {
    if (osDiskInfo != null) {
      return osDiskInfo.getName();
    }
    return diskName;
  }

  public String getDiskRaid() {
    return osDiskInfo == null ? diskRaid : osDiskInfo.getRaid();
  }

  public String getDiskSize() {
    return osDiskInfo == null ? diskSize : osDiskInfo.getSize();
  }

  public String getDiskState() {
    return osDiskInfo == null ? diskState : osDiskInfo.getStatus();
  }
}
