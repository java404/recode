package smartmon.core.hosts;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NetworkInfo {
  private List<String> interfaces;
}
