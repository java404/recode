package smartmon.smartstor.infra.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartmon.cache.SmartMonCacheService;
import smartmon.cache.SmatMonCacheItem;
import smartmon.smartstor.domain.model.Disk;

@Component
public class DataCacheManager {
  private static final String CACHE_KEY_PREFIX = "Smartstor";
  private static final String DISKS_CACHE_KEY = CACHE_KEY_PREFIX + "#Disks";

  @Autowired
  private SmartMonCacheService smartMonCacheService;

  public void saveDisks(String serviceIp, List<Disk> disks) {
    String key = getDisksCacheKey(serviceIp);
    smartMonCacheService.put(key, disks);
  }

  public void saveDisksSyncError(String serviceIp, String errorMessage) {
    String key = getDisksCacheKey(serviceIp);
    smartMonCacheService.putError(key, errorMessage);
  }

  public CachedData<Disk> getDisks(String serviceIp) {
    String key = getDisksCacheKey(serviceIp);
    SmatMonCacheItem smatMonCacheItem = smartMonCacheService.get(key);
    return smatMonCacheItem == null ? null : new CachedData<>(smatMonCacheItem, Disk.class);
  }

  private String getDisksCacheKey(String serviceIp) {
    return String.format("%s#%s", DISKS_CACHE_KEY, serviceIp);
  }
}
