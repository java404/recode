package smartmon.utilities.misc;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Builder(builderMethodName = "buildTargetHost")
public class TargetHost {
  @NonNull
  private final String address;

  private final int port;

  @EqualsAndHashCode.Exclude
  private final String username;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private final String password;

  @EqualsAndHashCode.Exclude
  private final String privateKey;

  public static TargetHostBuilder builder(String address, int port) {
    return buildTargetHost().address(address).port(port);
  }
}
