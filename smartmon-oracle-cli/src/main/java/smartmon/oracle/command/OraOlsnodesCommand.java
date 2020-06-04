package smartmon.oracle.command;

import java.util.List;
import smartmon.oracle.OracleCommand;
import smartmon.oracle.exec.Olsnodes;
import smartmon.oracle.types.OracleNode;

public class OraOlsnodesCommand implements OracleCommand {
  private static final OraOlsnodesCommand INSTANCE = new OraOlsnodesCommand();

  @Override
  public String getName() {
    return "olsnodes";
  }

  @Override
  public void exec() {
    System.out.println("Olsnodes: ");
    final List<OracleNode> nodes = Olsnodes.getInstance().listNodes();
    for (final OracleNode node : nodes) {
      System.out.println(node);
    }
  }

  public static OracleCommand getInstance() {
    return INSTANCE;
  }
}
