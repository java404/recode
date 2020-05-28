package smartmon.smartstor.app.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import smartmon.smartstor.app.Command;
import smartmon.smartstor.web.vo.DiskLedStateVo;

@Data
@EqualsAndHashCode(callSuper = false)
public class DiskRaidLedCommand extends Command {
  private String serviceIp;
  private String cesAddr;
  private boolean ledOn;

  public DiskRaidLedCommand(DiskLedStateVo vo, String state) {
    super();
    this.serviceIp = vo.getServiceIp();
    this.cesAddr = vo.getCesAddr();
    this.ledOn = "on".equals(state);
  }

  public String getCommandStepName() {
    return this.ledOn ? "Raid led on" : "Raid led off";
  }
}
