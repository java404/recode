package smartmon.utilities.misc;

import com.google.common.base.Strings;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

@Slf4j
public class XmlParser {
  @Getter
  private final Document doc;

  private XmlParser(Document doc) {
    this.doc = doc;
  }

  public static XmlParser loadFromStream(InputStream inputStream) {
    final SAXReader reader = new SAXReader();
    try {
      final Document doc = reader.read(inputStream);
      return new XmlParser(doc);
    } catch (DocumentException e) {
      log.warn("cannot parse xml stream: ", e);
      return null;
    }
  }

  public static XmlParser loadFromContent(String content) {
    return loadFromStream(IOUtils.toInputStream(Strings.nullToEmpty(content), StandardCharsets.UTF_8));
  }

  public static XmlParser loadFromFile(File src) {
    try (FileInputStream content = new FileInputStream(src)) {
      return loadFromStream(content);
    } catch (IOException error) {
      log.warn("cannot load xml file", error);
      return null;
    }
  }

  public static XmlParser loadFromFile(String filename) {
    return loadFromFile(new File(Strings.nullToEmpty(filename)));
  }
}
