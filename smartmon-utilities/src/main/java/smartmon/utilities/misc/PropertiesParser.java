package smartmon.utilities.misc;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;

public class PropertiesParser {
  final Map<String, String> props;

  public PropertiesParser(Map<String, String> props) {
    this.props = MapUtils.emptyIfNull(props);
  }

  public String parseString(String name) {
    return parseString(name, null);
  }

  public String parseString(String name, String defaultValue) {
    return props.getOrDefault(name, defaultValue);
  }

  public Integer parseNum(String name) {
    try {
      final String value = props.getOrDefault(name, null);
      return Strings.isNullOrEmpty(value) ? null : Integer.parseInt(value);
    } catch (Exception err) {
      return null;
    }
  }

  public List<String> parseStrings(String name) {
    final String value = parseString(name);
    return Strings.isNullOrEmpty(value) ? null :
      Splitter.on(',').trimResults().omitEmptyStrings().splitToList(value);
  }
}
