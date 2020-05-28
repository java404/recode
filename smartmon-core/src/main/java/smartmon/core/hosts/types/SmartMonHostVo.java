package smartmon.core.hosts.types;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import smartmon.core.agent.AgentStateEnum;
import smartmon.core.ipmi.PowerStateEnum;

@Getter
@Setter
public class SmartMonHostVo {
  private String hostUuid;
  private String hostname;
  private String systemVendor;
  private String distribution;
  private String manageIp;
  private String ipmiAddress;
  private Date createTime;
  private PowerStateEnum powerState;
  private Date statusTime;
  private AgentStateEnum agentState;
  private String agentTaskId;
}
