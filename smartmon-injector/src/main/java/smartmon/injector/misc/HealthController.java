package smartmon.injector.misc;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "utils")
@RequestMapping("${smartmon.api.prefix:/injector/api/v2}/health")
@RestController
public class HealthController {
  @ApiOperation("get healthy status")
  @GetMapping
  public Boolean isHealthy() {
    return true;
  }
}
