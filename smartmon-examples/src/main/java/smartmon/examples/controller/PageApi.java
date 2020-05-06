package smartmon.examples.controller;

import io.swagger.annotations.Api;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.webtools.page.SmartMonPageParams;
import smartmon.webtools.page.SmartMonPageResponseBuilder;

@Slf4j
@Api(tags = "page")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/page")
public class PageApi {

  @Data
  private static class TestItem {
    private int id;
    private String name;
  }

  private List<TestItem> makeTestItems() {
    final int itemCnt = 100;
    final List<TestItem> items = new ArrayList<>(itemCnt);
    for (int i = 0; i < itemCnt; ++i) {
      final TestItem item = new TestItem();
      item.setId(i);
      item.setName(String.format("item %d", i));
      items.add(item);
    }
    return items;
  }

  @SmartMonPageParams
  @GetMapping
  public SmartMonResponse<Page<TestItem>> getPages(ServerHttpRequest request) {
    return new SmartMonPageResponseBuilder<TestItem>(makeTestItems(), request, "id").build();
  }
}
