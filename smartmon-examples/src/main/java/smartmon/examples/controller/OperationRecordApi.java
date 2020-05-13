package smartmon.examples.controller;

import io.swagger.annotations.Api;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "OperationRecord")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/opt-record")
public class OperationRecordApi {

  @Data
  @ToString
  private static class Item {
    private String item1 = "item1";
    private String item2 = "item2";
  }


  @GetMapping
  public String test() {
    final Item item = new Item();
    final String result = item.toString();
    log.debug(result);
    return "ok";
  }
}
