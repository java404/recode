package smartmon.injector.oracle;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.oracle.api.SmartMonOracle;
import smartmon.oracle.types.OracleClusterInfo;

@Api(tags = "Oracle")
@RequestMapping("${smartmon.api.prefix:/injector/api/v2}/oracle")
@RestController
public class SmartMonOracleController {
  @Autowired
  private SmartMonOracle smartMonOracle;

  @GetMapping("clusters")
  public OracleClusterInfo getClusterInfo() {
    return smartMonOracle.getClusterInfo();
  }
}
