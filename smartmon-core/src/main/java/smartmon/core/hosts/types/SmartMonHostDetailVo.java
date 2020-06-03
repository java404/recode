package smartmon.core.hosts.types;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import smartmon.core.hosts.HardwareInfo;
import smartmon.core.hosts.NetworkInfo;
import smartmon.core.ipmi.PowerStateEnum;
import smartmon.utilities.misc.JsonConverter;

@Getter
@Setter
public class SmartMonHostDetailVo {
  private String hostUuid;
  private String hostname;
  private String manageIp;
  private String ipmiAddress;
  private Date createTime;
  private PowerStateEnum powerState;

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

  //Network info
  private String networks;
  private List<String> monitorInterfaces;

  public List<HardwareInfo.MountInfo> getMounts() {
    return JsonConverter.readValueQuietly(mounts, List.class, HardwareInfo.MountInfo.class);
  }

  public NetworkInfo getNetworks() {
    return JsonConverter.readValueQuietly(networks, NetworkInfo.class);
  }
}
