package smartmon.oracle.misc;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import smartmon.oracle.exec.Sqlplus;
import smartmon.oracle.types.OraInventoryHome;
import smartmon.utilities.misc.XmlParser;

public class OraInventory {
  private static final String DEFAULT_LOCATION = "ContentsXML/inventory.xml";
  private final XmlParser xmlParser;

  private OraInventory(XmlParser xmlParser) {
    this.xmlParser = xmlParser;
  }

  public static OraInventory loadFromContent(String content) {
    XmlParser xmlParser = XmlParser.loadFromContent(content);
    return xmlParser == null ? null : new OraInventory(xmlParser);
  }

  public static OraInventory loadFromFile(String filename) {
    final XmlParser xmlParser = XmlParser.loadFromFile(filename);
    return xmlParser == null ? null : new OraInventory(xmlParser);
  }

  public static OraInventory loadFromConfig() {
    final OraInstLoc oraInstLoc = new OraInstLoc();
    return loadFromFile(FilenameUtils.concat(oraInstLoc.getInventoryLoc(), DEFAULT_LOCATION));
  }

  private OraInventoryHome parseHomeNode(Document doc, Node homeNode) {
    final OraInventoryHome inventoryHome = new OraInventoryHome();
    final Element element = (Element)homeNode;
    inventoryHome.setName(element.attributeValue("NAME"));
    inventoryHome.setLoc(element.attributeValue("LOC"));
    inventoryHome.setType(element.attributeValue("TYPE"));
    inventoryHome.setIdx(Integer.parseInt(element.attributeValue("IDX")));
    inventoryHome.setCrs(Boolean.parseBoolean(element.attributeValue("CRS")));
    final List<String> nodeNames = new ArrayList<>();
    inventoryHome.setNodeNames(nodeNames);
    final List<Node> nodes = doc.selectNodes(homeNode.getUniquePath() + "/NODE_LIST//NODE");
    for (final Node node : nodes) {

      final Element nodeElement = (Element)node;
      nodeNames.add(nodeElement.attributeValue("NAME"));
    }
    return inventoryHome;
  }

  public List<OraInventoryHome> parseHomeList() {
    final Document doc = xmlParser.getDoc();
    final List<Node> homeNodes = doc.selectNodes("/INVENTORY/HOME_LIST//HOME");
    final List<OraInventoryHome> result = new ArrayList<>();
    for (final Node homeNode : homeNodes) {
      result.add(parseHomeNode(doc, homeNode));
    }
    return result;
  }
}
