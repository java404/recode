package smartmon.smartstor.interfaces.web.representation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GroupNodeDto {
  private String nodeName;
  private Integer listenPort;
  private String listenIp;
}
