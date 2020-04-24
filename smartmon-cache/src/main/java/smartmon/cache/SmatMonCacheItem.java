package smartmon.cache;

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
    return JsonConverter.readValueQuietly(errors, valueType);
  }
}
