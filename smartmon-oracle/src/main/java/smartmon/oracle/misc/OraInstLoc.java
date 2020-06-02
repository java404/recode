package smartmon.oracle.misc;

import java.io.File;
import lombok.Getter;
import lombok.ToString;
import smartmon.utilities.misc.OptionsParser;

@ToString
public class OraInstLoc {
  @Getter
  private static final String ORA_INST_LOC = "/etc/oraInst.loc";
  private static final String KEY_INVENTORY_LOC = "inventory_loc";
  private static final String KEY_INST_GROUP = "inst_group";

  @Getter
  @ToString.Include
  private final String inventoryLoc;
  @Getter
  @ToString.Include
  private final String instGroup;

  public OraInstLoc() {
    final OptionsParser optionsParser = new OptionsParser(new File(ORA_INST_LOC));
    this.inventoryLoc = optionsParser.get(KEY_INVENTORY_LOC);
    this.instGroup = optionsParser.get(KEY_INST_GROUP);
  }
}
