package smartmon.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import smartmon.cache.SmartMonCacheService;
import smartmon.cache.SmatMonCacheItem;
import smartmon.utilities.misc.JsonConverter;

@Service
public class SmartMonCacheServiceImpl implements SmartMonCacheService {
  private Map<String, CacheItem> datum = new ConcurrentHashMap<>();

  private CacheItem makeCacheItem(String key) {
    if (datum.containsKey(key)) {
      return datum.get(key);
    }
    final CacheItem item = new CacheItem();
    item.setKey(key);
    datum.put(key, item);
    return item;
  }

  @Override
  public synchronized void put(String key, Object value) {
    final CacheItem item = makeCacheItem(key);
    item.setData(JsonConverter.writeValueAsStringQuietly(value));
    item.setTimestamp(System.nanoTime());
  }

  @Override
  public synchronized void putError(String key, Object error) {
    final CacheItem item = makeCacheItem(key);
    item.setErrors(JsonConverter.writeValueAsStringQuietly(error));
  }

  @Override
  public synchronized SmatMonCacheItem get(String key) {
    if (!datum.containsKey(key)) {
      return null;
    }
    final CacheItem item = datum.get(key);
    return new SmatMonCacheItem(item.getKey(), item.getData(), item.getErrors());
  }
}
