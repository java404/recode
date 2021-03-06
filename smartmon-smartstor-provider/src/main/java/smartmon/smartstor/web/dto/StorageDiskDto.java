package smartmon.smartstor.web.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class StorageDiskDto {
  private String hostId;
  private String hostname;
  private String listenIp;
  private String hostType;
  private String version;
  private String nodeName;
  private List<SimpleDiskDto> disks = new ArrayList<>();
  private List<AvailableDiskDto> availableDisks = new ArrayList<>();
  private boolean expired;
  private String error;

  public String getHostname() {
    if (StringUtils.isNotBlank(this.hostname) && this.hostname.startsWith("hu")) {
      return this.hostname.replace("hu", "su");
    }
    return this.hostname;
  }
}
