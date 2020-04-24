package smartmon.cache;

public interface SmartMonCacheService {
  void put(String key, Object value);

  void putError(String key, Object error);

  SmatMonCacheItem get(String key);
}

