package smartmon.examples.controller;

import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.cache.SmartMonCacheService;
import smartmon.cache.SmatMonCacheItem;

@Api(tags = "cache")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/cache")
@Slf4j
public class CacheApi {
  @Autowired
  private SmartMonCacheService smartMonCacheService;

  @Data
  static class TestItem {
    private String data = "123";
  }

  @GetMapping
  public void createCache() {
    final TestItem item = new TestItem();
    smartMonCacheService.put("item", item);
    final SmatMonCacheItem cacheItem = smartMonCacheService.get("item");
    log.debug("Item {}", cacheItem.getData(TestItem.class).getData());
  }
}
