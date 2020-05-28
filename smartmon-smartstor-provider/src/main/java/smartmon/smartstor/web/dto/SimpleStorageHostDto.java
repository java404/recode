package smartmon.smartstor.web.dto;

import lombok.Data;

@Data
public class SimpleStorageHostDto {
  private String hostId;
  private String hostname;
  private String listenIp;
  private String sysModeDesc;
  private String version;
  private Integer versionNum;
  private String nodeName;
  private boolean ios;

  public boolean isIos() {
    return "storage".equalsIgnoreCase(this.sysModeDesc) || "merge".equalsIgnoreCase(this.sysModeDesc);
  }
}
