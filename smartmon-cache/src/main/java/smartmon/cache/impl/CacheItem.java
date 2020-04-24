package smartmon.cache.impl;

import lombok.Data;

@Data
public class CacheItem {
  private long timestamp;

  private String key;
  private String data;
  private String errors;
}
