package smartmon.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import smartmon.utilities.misc.JsonConverter;

public class SmatMonCacheItem {
  @Getter
  private final String key;
  private final String data;
  private final String errors;

  public SmatMonCacheItem(String key, String data, String errors) {
    this.key = key;
    this.data = data;
    this.errors = errors;
  }

  public boolean isExpired() {
    // TODO Check the timestamp
    return false;
  }

  public <T> T getData(Class<T> valueType) {
    return JsonConverter.readValueQuietly(data, valueType);
  }

  public <T> T getError(Class<T> valueType) {
    if (errors == null) {
      return null;
    }
    return JsonConverter.readValueQuietly(errors, valueType);
  }

  public <T> List<T> getDataList(Class<T> valueType) {
    List<T> result = Lists.newArrayList();
    JsonNode root = JsonConverter.readTreeQuietly(data);
    if (root == null) {
      return result;
    }
    Iterator<JsonNode> elements = root.elements();
    while (elements.hasNext()) {
      JsonNode node = elements.next();
      T element = JsonConverter.readValueQuietly(node.toString(), valueType);
      result.add(element);
    }
    return result;
  }
}
