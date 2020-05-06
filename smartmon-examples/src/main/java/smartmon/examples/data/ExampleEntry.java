package smartmon.examples.data;

import java.io.Serializable;
import lombok.Data;

@Data
public class ExampleEntry implements Serializable {
  private static final long serialVersionUID = -709509061404984347L;

  private String name;
  private String value;
}
