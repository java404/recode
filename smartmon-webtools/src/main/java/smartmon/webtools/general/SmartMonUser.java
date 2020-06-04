package smartmon.webtools.general;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class SmartMonUser implements Serializable {
  private static final long serialVersionUID = 8601275405601697510L;

  private String username;
  private String password;
  private List<SmartMonRole> roles;
}
