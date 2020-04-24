package smartmon.examples.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonException;

@Api(tags = "exception")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/exception")
public class ExceptionApi {
  @GetMapping
  public void trigger() {
    throw new SmartMonException(1000, "Example Exception");
  }
}
