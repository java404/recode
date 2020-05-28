package smartmon.smartstor.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SimpleLunDto {
  private String lunName;
  private String lunTypeDisplay;
  private String cusStatus;
  private String groupName;
  private Boolean actualState;
  private String asmStatus;

}
