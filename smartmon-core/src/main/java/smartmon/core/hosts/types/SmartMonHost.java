package smartmon.core.hosts.types;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "smartmon_host")
public class SmartMonHost implements Serializable {
  private static final long serialVersionUID = 2775014481625358613L;

  @Id
  private String hostUuid;
  private String manageIp;
}
