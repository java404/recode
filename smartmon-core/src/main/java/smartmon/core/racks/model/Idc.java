package smartmon.core.racks.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Idc implements Serializable {
  private static final long serialVersionUID = 5251840251218151711L;

  private String id;
  private String name;
}
