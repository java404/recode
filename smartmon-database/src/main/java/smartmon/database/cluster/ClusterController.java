package smartmon.database.cluster;

import io.swagger.annotations.Api;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.database.oracle.SmartMonOracleService;
import smartmon.oracle.types.OraInventoryHome;

@Api(tags = "Oracle Cluster")
@RestController
@RequestMapping("${smartmon.api.prefix:/core/api/v2}/oracle/cluster")
@Slf4j
public class ClusterController {
  @Autowired
  private SmartMonOracleService oracleService;

  @GetMapping("{hostUuid}")
  public List<OraInventoryHome> getClusterInfo(@PathVariable String hostUuid) {
    return oracleService.getClusterInfo(hostUuid);
  }
}
