package smartmon.webtools.debug;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "debug")
@RestController
@RequestMapping("${smartmon.api.prefix}/debug")
public class SmartMonDebugController {
  @Autowired
  private SmartMonDebugService smartMonDebugService;

  @ApiOperation("Dump server configuration")
  @GetMapping(value = "dump-config", produces = "text/plain")
  public String dumpConfig() {
    final List<String> properties = smartMonDebugService.getProperties();
    return "Server Config: \n" + String.join("\n", properties);
  }
}
