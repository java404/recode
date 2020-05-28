package smartmon.smartstor.web.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DiskDto extends SimpleDiskDto {
  private String raidSize;
  private String mode;
  private DiskNvmeHealthDto nvmeHealth;
  private List<DiskPartDto> parts;
  private Long partCount;
  private boolean expired;
  private Long lastHeartbeatTime;
  private List<DiskHealthDto> healths = new ArrayList<>();
  public static DiskDto empty() {
    return new DiskDto();
  }
}
