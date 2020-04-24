package smartmon.smartstor.web.dto;

import lombok.Data;

@Data
public class HostInitInfoDto {
  private boolean init;
  private String version;
}
