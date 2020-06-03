package smartmon.core.hosts;

import lombok.Data;

import java.io.Serializable;

@Data
public class SmartMonHost implements Serializable {
  private static final long serialVersionUID = -1853808585834685991L;
  private String hostUuid;
  private String manageIp;
}

