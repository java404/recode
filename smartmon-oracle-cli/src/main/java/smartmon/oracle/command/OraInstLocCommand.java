package smartmon.oracle.command;

import smartmon.oracle.OracleCommand;
import smartmon.oracle.misc.OraInstLoc;

public class OraInstLocCommand implements OracleCommand {
  private static final OraInstLocCommand INSTANCE = new OraInstLocCommand();

  @Override
  public String getName() {
    return "oraInst.loc";
  }

  @Override
  public void exec() {
    final OraInstLoc oraInstLoc = new OraInstLoc();
    System.out.println("oraInstLoc:");
    System.out.println(oraInstLoc.toString());
  }

  public static OracleCommand getInstance() {
    return INSTANCE;
  }
}
