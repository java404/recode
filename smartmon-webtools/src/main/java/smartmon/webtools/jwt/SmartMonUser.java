package smartmon.webtools.jwt;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SmartMonUser {
  private String username;
  private String password;
  private List<String> roles = new ArrayList<>();
}
