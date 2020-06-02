package smartmon.gateway.security.types;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
  @NotBlank
  private String username;
  private String password;
}
