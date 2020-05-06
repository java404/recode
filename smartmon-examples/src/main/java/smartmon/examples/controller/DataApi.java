package smartmon.examples.controller;

import io.swagger.annotations.Api;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.examples.data.ExampleEntry;
import smartmon.examples.data.TestExampleMapper;

@Api(tags = "data")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/data")
@Slf4j
public class DataApi {
  @Autowired
  private TestExampleMapper testExampleMapper;

  @GetMapping("test")
  public String test() {
    final List<ExampleEntry> result1 = testExampleMapper.findAll();
    log.debug("Data: {}", result1);

    final ExampleEntry item = new ExampleEntry();
    item.setName("test");
    item.setValue("abc");
    testExampleMapper.add(item);

    final List<ExampleEntry> result2 = testExampleMapper.findAll();
    log.debug("Data: {}", result2);
    return "ok";
  }
}
