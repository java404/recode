package smartmon.oracle.misc;

import java.util.List;
import java.util.function.Function;

import com.google.common.base.Splitter;
import org.junit.Assert;
import org.junit.Test;
import smartmon.oracle.types.OraInventoryHome;

public class OraInventoryTest {
  @Test
  public void test() {
    final String testContent = "<INVENTORY>"
      + "  <VERSION_INFO>"
      + "    <SAVED_WITH>12.1.0.2.0</SAVED_WITH>"
      + "    <MINIMUM_VER>2.1.0.6.0</MINIMUM_VER>"
      + "  </VERSION_INFO>"
      + "  <HOME_LIST>"
      + "    <HOME NAME=\"OraGI12Home1\" LOC=\"/u01/app/12.1.0.2/grid\" TYPE=\"O\" IDX=\"1\" CRS=\"true\">"
      + "      <NODE_LIST> "
      + "       <NODE NAME=\"smartmoncluster1\"/> "
      + "       <NODE NAME=\"smartmoncluster2\"/> "
      + "      </NODE_LIST> "
      + "    </HOME> "
      + "    <HOME NAME=\"OraDB12Home1\" LOC=\"/u01/app/12.1.0.2/database\" TYPE=\"O\" IDX=\"2\">"
      + "      <NODE_LIST>"
      + "        <NODE NAME=\"smartmoncluster1\"/>"
      + "        <NODE NAME=\"smartmoncluster2\"/> "
      + "      </NODE_LIST> "
      + "    </HOME> "
      + " </HOME_LIST> "
      + "<COMPOSITEHOME_LIST> </COMPOSITEHOME_LIST>"
      + " </INVENTORY> ";

    final OraInventory oraInventory = OraInventory.loadFromContent(testContent);
    assert oraInventory != null;
    final List<OraInventoryHome> result = oraInventory.parseHomeList();
    Assert.assertEquals(result.size(), 2);
  }
}