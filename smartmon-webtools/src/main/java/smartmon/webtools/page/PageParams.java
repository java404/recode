package smartmon.webtools.page;

import com.google.common.base.Strings;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.data.domain.Sort;
import smartmon.utilities.misc.PropertiesParser;

@Getter
public class PageParams {
  private final Integer pageNo;
  private final Integer pageSize;
  private final Sort.Direction sortDirection;
  private final String sortProperty;
  private final String filter;
  private final List<String> filterProperties;
  private final String conditionProperty;
  private final String condition;

  private PageParams(Integer pageNo, Integer pageSize,
                     Sort.Direction sortDirection, String sortProperty,
                     String filter, List<String> filterProperties,
                     String conditionProperty, String condition) {
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.sortDirection = sortDirection;
    this.sortProperty = sortProperty;
    this.filter = filter;
    this.filterProperties = filterProperties;
    this.conditionProperty = conditionProperty;
    this.condition = condition;
  }

  public static PageParams make(Map<String, String> params, String defaultSortProp) {
    final PropertiesParser parser = new PropertiesParser(params);
    return new PageParams(parser.parseNum("page-no"),
      parser.parseNum("page-size"),
      parseDirection(parser), parser.parseString("sort-property", defaultSortProp),
      parser.parseString("filter"), parser.parseStrings("filter-properties"),
      parser.parseString("condition-property"), parser.parseString("condition"));
  }

  private static Sort.Direction parseDirection(PropertiesParser parser) {
    final String sortDirection = parser.parseString("sort-direction");
    if (Strings.isNullOrEmpty(sortDirection)) {
      return Sort.Direction.DESC;
    }
    return sortDirection.trim().toLowerCase().equals("asc")
      ? Sort.Direction.ASC : Sort.Direction.DESC;
  }
}
