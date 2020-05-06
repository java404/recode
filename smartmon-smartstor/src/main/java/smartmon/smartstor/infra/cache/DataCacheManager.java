package smartmon.smartstor.infra.cache;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartmon.cache.SmartMonCacheService;
import smartmon.cache.SmatMonCacheItem;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.domain.model.Lun;
import smartmon.smartstor.domain.model.Pool;

@Component
public class DataCacheManager {
  @Autowired
  private SmartMonCacheService smartMonCacheService;

  public <T> void save(String serviceIp, List<T> resources, Class<T> resourceType) {
    String key = CacheKey.getCacheKey(serviceIp, resourceType);
    smartMonCacheService.put(key, ListUtils.emptyIfNull(resources));
  }

  public <T> void saveError(String serviceIp, String errorMessage, Class<T> resourceType) {
    String key = CacheKey.getCacheKey(serviceIp, resourceType);
    smartMonCacheService.putError(key, errorMessage);
  }

  public <T> CachedData<T> gets(String serviceIp, Class<T> resourceType) {
    String key = CacheKey.getCacheKey(serviceIp, resourceType);
    SmatMonCacheItem smatMonCacheItem = smartMonCacheService.get(key);
    return smatMonCacheItem == null ? null : new CachedData<T>(smatMonCacheItem, resourceType);
  }

  private static class CacheKey {
    private static final String CACHE_KEY_PREFIX = "Smartstor";
    private static Map<Class<?>, String> resourceTypeKeyMap = Maps.newHashMap();

    static {
      resourceTypeKeyMap.put(Disk.class, "Disks");
      resourceTypeKeyMap.put(Pool.class, "Pools");
      resourceTypeKeyMap.put(Lun.class, "Luns");
    }

    static <T> String getCacheKey(String serviceIp, Class<T> resourceType) {
      String resourceKey = resourceTypeKeyMap.get(resourceType);
      return String.format("%s#%s#%s", CACHE_KEY_PREFIX, resourceKey, serviceIp);
    }
  }
}
