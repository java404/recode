package smartmon.smartstor.app.command;

import lombok.Data;

@Data
public class LunStateCommand {
  private String serviceIp;
  private String lunName;
  private Boolean online;

  public String getCommandStepName() {
    return this.online ? "Lun online" : "Lun offline";
  }
}

