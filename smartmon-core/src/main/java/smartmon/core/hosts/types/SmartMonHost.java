package smartmon.core.hosts.types;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import smartmon.core.agent.AgentStateEnum;
import smartmon.core.ipmi.PowerStateEnum;
import smartmon.core.misc.SshService;

@Data
@Table(name = "smartmon_host")
public class SmartMonHost implements Serializable {
  private static final long serialVersionUID = 2775014481625358613L;

  @Id
  private String hostUuid;
  private Date createTime;
  private Date updateTime;
  private Date statusTime;
  private String manageIp;
  private String sysUsername;
  private String sysPassword;
  private Integer sshPort = SshService.DEFAULT_SSH_PORT;
  private String ipmiAddress;
  private String ipmiUsername;
  private String ipmiPassword;
  private PowerStateEnum powerState;
  private AgentStateEnum agentState;
  private String agentTaskId;
  private String hostname;

  //System info
  private String systemVendor;
  private String system;
  private String architecture;
  private String osFamily;
  private String distribution;
  private String kernel;

  //Hardware info - cpu
  private String processorModel;
  private Integer processorCores;
  private Integer processorCount;
  private Integer threadsPerCore;
  private Integer logicProcessorCount;

  //Hardware info - mem
  private Long memoryTotal;
  private Long swapTotal;

  //Hardware info - mounts
  private String mounts;

  //Hardware info - mainboard
  private String biosVersion;

  public Integer getSshPort() {
    return Objects.nonNull(sshPort) ? sshPort : SshService.DEFAULT_SSH_PORT;
  }

  public boolean ipmiEnabled() {
    return StringUtils.isNotEmpty(ipmiAddress)
      && StringUtils.isNotEmpty(ipmiUsername) && StringUtils.isNotEmpty(ipmiPassword);
  }

  public boolean hasIp(String ip) {
    //TODO: need improve, judge all ips
    return Objects.equals(manageIp, ip);
  }
}
