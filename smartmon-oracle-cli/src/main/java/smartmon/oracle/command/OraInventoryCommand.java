package smartmon.oracle.command;

import java.util.List;
import smartmon.oracle.OracleCommand;
import smartmon.oracle.misc.OraInventory;
import smartmon.oracle.types.OraInventoryHome;

public class OraInventoryCommand implements OracleCommand {
  private static final OraInventoryCommand INSTANCE = new OraInventoryCommand();

  @Override
  public String getName() {
    return "oraInventory";
  }

  @Override
  public void exec() {
    final OraInventory oraInventory = OraInventory.loadFromConfig();
    if (oraInventory == null) {
      System.out.println("cannot load oracle inventory");
      return;
    }

    System.out.println("ORA Inventory: ");
    final List<OraInventoryHome> homes = oraInventory.parseHomeList();
    for (final OraInventoryHome home : homes) {
      System.out.println(home.print());
    }
  }

  public static OracleCommand getInstance() {
    return INSTANCE;
  }
}
