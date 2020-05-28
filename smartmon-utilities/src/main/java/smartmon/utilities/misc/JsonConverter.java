package smartmon.utilities.misc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;

@Slf4j
public class JsonConverter {
  private static final ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  private static ObjectMapper getMapper() {
    return mapper;
  }

  public static JsonNode writeValue(Object value) {
    return getMapper().valueToTree(value);
  }

  public static String writeValueAsString(Object value) throws JsonProcessingException {
    return (value instanceof String) ? value.toString() : getMapper().writeValueAsString(value);
  }

  public static String writeValueAsStringQuietly(Object value) {
    try {
      return writeValueAsString(value);
    } catch (JsonProcessingException error) {
      log.warn("Json process exception:", error);
      return "";
    }
  }

  public static <T> T treeToValue(JsonNode node, Class<T> valueType) throws JsonProcessingException {
    return getMapper().treeToValue(node, valueType);
  }

  public static <T> T treeToValueQuietly(JsonNode node, Class<T> valueType) {
    try {
      return treeToValue(node, valueType);
    } catch (JsonProcessingException error) {
      log.warn("Json process exception:", error);
      return null;
    }
  }

  public static <T> T readValueQuietly(String value, Class<T> valueType) {
    try {
      return getMapper().readValue(value, valueType);
    } catch (JsonProcessingException error) {
      log.warn("Json process exception:", error);
      return null;
    }
  }

  public static <T> T readValueQuietly(String value, Class<?> parametrized, Class<?>... parameterClasses) {
    try {
      if (value == null) {
        return null;
      }
      JavaType javaType = getMapper().getTypeFactory().constructParametricType(parametrized, parameterClasses);
      return getMapper().readValue(value, javaType);
    } catch (JsonProcessingException error) {
      log.warn("Json process exception:", error);
      return null;
    }
  }

  public static JsonNode readTreeQuietly(String value) {
    try {
      return readTree(value);
    } catch (Exception error) {
      log.warn("Json process exception:", error);
      return null;
    }
  }

  public static JsonNode readTree(String value) throws JsonProcessingException {
    return getMapper().readTree(value);
  }

  /** Find json node via key path (no exception). */
  public static JsonNode findValueQuietly(JsonNode rootNode, String keyPath) {
    try {
      return findValue(rootNode, keyPath);
    } catch (Exception ignore) {
      return null;
    }
  }

  /** Find json node via key path.
   *  Example: { "a": { "b" : 1 } } =>  "a.b" = 1.
   */
  private static JsonNode findValue(JsonNode rootNode, String keyPath) {
    if (Strings.isNullOrEmpty(keyPath)) {
      return null;
    }
    final List<String> keys = ListUtils.emptyIfNull(Splitter.on(".")
      .trimResults().splitToList(keyPath));
    JsonNode node = rootNode;
    for (final String key : keys) {
      if (Strings.isNullOrEmpty(key)) {
        return null;
      }
      node = node.get(key);
      if (node == null) {
        return null;
      }
    }
    return node;
  }

  /** Parse JSON String list. */
  public static List<String> parseStrListQuite(String list) {
    try {
      return parseStrList(list);
    } catch (Exception ignore) {
      return null;
    }
  }

  private static List<String> parseStrList(String source) throws IOException {
    final JsonNode list = readTree(source);
    if (list == null || !list.isArray()) {
      return null;
    }
    final List<String> values = new ArrayList<>();
    for (int i = 0; i < list.size(); ++i) {
      final JsonNode item = list.get(i);
      values.add(item == null ? "" : item.asText());
    }
    return values;
  }
}
