package smartmon.oracle.command;

import java.util.List;
import smartmon.oracle.OracleCommand;
import smartmon.oracle.cluster.OracleNode;
import smartmon.oracle.exec.Olsnodes;

public class OraOlsnodesCommand implements OracleCommand {
  private static final OraOlsnodesCommand INSTANCE = new OraOlsnodesCommand();

  @Override
  public String getName() {
    return "olsnodes";
  }

  @Override
  public void exec() {
    final Olsnodes olsnodes = new Olsnodes();
    System.out.println("Olsnodes: ");
    final List<OracleNode> nodes = olsnodes.listNodes();
    for (final OracleNode node : nodes) {
      System.out.println(node);
    }
  }

  public static OracleCommand getInstance() {
    return INSTANCE;
  }
}
