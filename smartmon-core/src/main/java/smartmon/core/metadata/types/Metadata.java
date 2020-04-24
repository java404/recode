package smartmon.core.metadata.types;

import java.io.Serializable;
import lombok.Data;

@Data
public class Metadata implements Serializable {
  private static final long serialVersionUID = 1759412677331547664L;

  private String name;
  private String value;
}
