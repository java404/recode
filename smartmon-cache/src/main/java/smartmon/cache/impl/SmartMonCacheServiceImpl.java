package smartmon.cache.impl;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.cache.SmartMonCacheService;
import smartmon.cache.SmatMonCacheItem;
import smartmon.cache.mapper.SmartMonCacheMapper;
import smartmon.utilities.misc.JsonConverter;

@Slf4j
@Service
public class SmartMonCacheServiceImpl implements SmartMonCacheService {
  @Autowired
  private SmartMonCacheMapper cacheMapper;

  @Override
  public void put(String key, Object value) {
    final CacheItem item = new CacheItem();
    item.setKey(key);
    item.setData(JsonConverter.writeValueAsStringQuietly(value));
    item.setTimestamp(System.nanoTime());
    cacheMapper.put(item);
  }

  @Override
  public void putError(String key, Object error) {
    cacheMapper.putError(key, JsonConverter.writeValueAsStringQuietly(error));
  }

  @Override
  public SmatMonCacheItem get(String key) {
    final CacheItem item = cacheMapper.get(key);
    if (item == null) {
      log.warn("cannot find cache key {}", Strings.nullToEmpty(key));
      return null;
    }
    return new SmatMonCacheItem(item.getKey(), item.getData(), item.getErrors());
  }
}
