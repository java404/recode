package smartmon.core.hosts.types;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "smartmon_host")
public class SmartMonHost implements Serializable {
  private static final long serialVersionUID = 2775014481625358613L;
  private static final int DEFAULT_SSH_PORT = 22;

  @Id
  private String hostUuid;
  private String manageIp;
  private String sysUsername;
  private String sysPassword;
  private Integer sshPort = DEFAULT_SSH_PORT;

  public Integer getSshPort() {
    return Objects.nonNull(sshPort) ? sshPort : DEFAULT_SSH_PORT;
  }
}
