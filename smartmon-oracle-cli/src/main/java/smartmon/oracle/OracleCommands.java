package smartmon.oracle;

import java.util.HashMap;
import java.util.Map;
import smartmon.oracle.command.OraInstLocCommand;
import smartmon.oracle.command.OraInventoryCommand;
import smartmon.oracle.command.OraOlsnodesCommand;

public class OracleCommands {
  private static final Map<String, OracleCommand> commands;

  static {
    commands = new HashMap<>();
    commands.put(OraInstLocCommand.getInstance().getName(), OraInstLocCommand.getInstance());
    commands.put(OraInventoryCommand.getInstance().getName(), OraInventoryCommand.getInstance());
    commands.put(OraOlsnodesCommand.getInstance().getName(), OraOlsnodesCommand.getInstance());
  }

  public OracleCommand getCommand(String name) {
    return commands.get(name);
  }
}
