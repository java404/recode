package smartmon.examples.controller;

import io.swagger.annotations.Api;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.webtools.page.SmartMonPageParams;
import smartmon.webtools.page.SmartMonPageResponseBuilder;

@Api(tags = "page")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/page")
public class PageApi {
  @SmartMonPageParams
  @GetMapping
  public SmartMonResponse<Page<String>> getPages(@RequestParam Map<String, String> params) {
    final int itemCnt = 100;
    return new SmartMonPageResponseBuilder<String>(IntStream.range(0, itemCnt)
      .mapToObj(i -> String.format("item %d", i)).collect(Collectors.toList()), params).build();
  }
}
