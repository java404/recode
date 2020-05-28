package smartmon.smartstor.app.command;

import lombok.Data;

@Data
public class LunDelCommand {
  private String serviceIp;
  private String lunName;
  private Boolean isLvvote;
  private Boolean ignoreAsmstatus;

}
