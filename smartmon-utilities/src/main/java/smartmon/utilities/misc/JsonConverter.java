package smartmon.utilities.misc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonConverter {
  private static final ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  private static ObjectMapper getMapper() {
    return mapper;
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
}
