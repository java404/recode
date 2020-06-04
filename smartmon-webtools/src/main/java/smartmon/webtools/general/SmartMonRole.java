package smartmon.webtools.general;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SmartMonRole implements Serializable {
  private static final long serialVersionUID = 145705452821668756L;

  private Long roleId;
  private String roleName;

  private List<SmartMonAuthority> authorities;
}
