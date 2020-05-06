package smartmon.webtools.page;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import smartmon.utilities.misc.JsonConverter;

public class PageCollection<E> {
  private final List<E> source;
  private final Pageable pageable;

  private final List<String> filterProperties;
  private final String filter;

  private final String conditionProperty;
  private final String condition;


  PageCollection(List<E> source, PageParams params) {
    this.filter = params.getFilter();
    this.filterProperties = params.getFilterProperties();
    this.condition = params.getCondition();
    this.conditionProperty = params.getConditionProperty();

    this.source = makeSublistFilter(Optional.ofNullable(source).orElse(new ArrayList<>()));
    this.source.sort((o1, o2) -> compare(o1, o2, params.getSortProperty(), params.getSortDirection()));

    Integer pageNo = params.getPageNo();
    Integer pageSize = params.getPageSize();
    if (pageNo == null || pageSize == null) {
      pageNo = 0;
      pageSize = CollectionUtils.size(source);
    }
    this.pageable = PageRequest.of(pageNo, Math.max(pageSize, 1),
      params.getSortDirection(), Strings.nullToEmpty(params.getSortProperty()));
  }

  /** make Page object. */
  public Page<E> makePage() {
    final int start = (int) pageable.getOffset();
    final int end = Math.min((start + pageable.getPageSize()), source.size());
    return new PageImpl<>(makeSublist(start, end), pageable, source.size());
  }

  private List<E> makeSublist(int start, int end) {
    if (start >= end) {
      return new ArrayList<>();
    }
    return source.subList(start, end);
  }

  private int compare(E o1, E o2, String sortProperty, Sort.Direction sortDirection) {
    final int result = compare(o1, o2, sortProperty);
    return sortDirection.isDescending() ? -result : result;
  }

  private int compare(E o1, E o2, String sortProperty) {
    try {
      String jsonPtrExpr = getJsonPtrExpr(sortProperty);
      final JsonNode node1 = JsonConverter.writeValue(o1).at(jsonPtrExpr);
      final JsonNode node2 = JsonConverter.writeValue(o2).at(jsonPtrExpr);
      if (node1.isNumber() && node2.isNumber()) {
        return Long.compare(node1.longValue(), node2.longValue());
      } else {
        return StringUtils.compare(node1.asText(), node2.asText());
      }
    } catch (Exception err) {
      return 0;
    }
  }

  private List<E> makeSublistFilter(List<E>  sourceSubList) {
    return sourceSubList.stream().filter(this::doItemFilter).collect(Collectors.toList());
  }

  private boolean doItemFilter(E item) {
    final JsonNode node = JsonConverter.writeValue(item);
    if (node == null) {
      // It's an invalid node just skip it.
      return false;
    }
    if (!doItemCondition(node)) {
      return false;
    }

    if (CollectionUtils.isEmpty(filterProperties) || Strings.isNullOrEmpty(filter)) {
      return true;
    }
    for (String property : filterProperties) {
      final JsonNode value = node.get(property);
      if (value == null) {
        continue;
      }
      if (StringUtils.containsIgnoreCase(value.asText(""), filter)) {
        return true;
      }
    }
    return false;
  }

  private boolean doItemCondition(JsonNode node) {
    if (Strings.isNullOrEmpty(condition) || Strings.isNullOrEmpty(conditionProperty)) {
      return true;
    }
    final List<String> properties = ListUtils.emptyIfNull(Splitter.on(";")
      .trimResults().splitToList(conditionProperty));
    if (properties.size() <= 1) {
      return doItemConditionExpression(node, conditionProperty, condition);
    }

    final Map<String, String> conditionMap = parseConditionMap(properties, condition);
    for (final Map.Entry<String, String> entry : conditionMap.entrySet()) {
      final boolean itemResult = doItemConditionExpression(node,
        entry.getKey(), Strings.nullToEmpty(entry.getValue()));
      if (!itemResult) {
        return false;
      }
    }
    return true;
  }

  private boolean doItemConditionExpression(JsonNode node, String property, String propertyValue) {
    final JsonNode value = JsonConverter.findValueQuietly(node, property);
    if (value == null) {
      return false;
    }
    return StringUtils.equals(value.asText(""), propertyValue);
  }

  private Map<String, String> parseConditionMap(List<String> properties, String values) {
    final Map<String, String> condMap = new HashMap<>();
    final List<String> valueItems = ListUtils.emptyIfNull(JsonConverter.parseStrListQuite(values));
    int index = 0;
    for (final String prop : ListUtils.emptyIfNull(properties)) {
      condMap.put(prop, getValueItem(valueItems, index));
      index++;
    }
    return condMap;
  }

  private String getValueItem(List<String> item, int index) {
    return index >= item.size() ? "" : item.get(index);
  }

  private String getJsonPtrExpr(String sortProperty) {
    final String prefix = "/";
    if (sortProperty.startsWith(prefix)) {
      return sortProperty;
    } else {
      return prefix + sortProperty;
    }
  }
}
