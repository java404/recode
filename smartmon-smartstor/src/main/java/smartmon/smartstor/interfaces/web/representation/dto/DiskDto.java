package smartmon.smartstor.interfaces.web.representation.dto;

import lombok.Data;
import smartmon.smartstor.domain.model.enums.DiskTypeEnum;

@Data
public class DiskDto {
  private String diskName;
  private String devName;
  private DiskTypeEnum diskType;
  private Long size;
  private Long extFreeSize;
  private Boolean actualState;
}
