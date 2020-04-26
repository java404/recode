package smartmon.smartstor.infra.cache;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import smartmon.cache.SmatMonCacheItem;

@Data
@AllArgsConstructor
public class CachedData<T> {
  private List<T> data;
  private boolean expired;
  private String error;

  public CachedData(SmatMonCacheItem smatMonCacheItem, Class<T> cls) {
    data = smatMonCacheItem.getDataList(cls);
    expired = smatMonCacheItem.isExpired();
    error = smatMonCacheItem.getError(String.class);
  }
}
