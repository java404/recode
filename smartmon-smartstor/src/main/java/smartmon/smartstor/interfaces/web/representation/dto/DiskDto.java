package smartmon.smartstor.interfaces.web.representation.dto;

import lombok.Data;

@Data
public class DiskDto {
  private String diskName;
  private String devName;
  private Integer diskType;
  private Long size;
  private Long extFreeSize;
  private Boolean actualState;
}
