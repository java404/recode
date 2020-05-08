package smartmon.core.idc.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdcVo implements Serializable {
  private static final long serialVersionUID = 5251840251218151711L;

  private String id;
  private String name;
}
