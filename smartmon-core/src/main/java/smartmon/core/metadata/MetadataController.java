package smartmon.core.metadata;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.metadata.types.Metadata;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "utils")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/metadata")
@Slf4j
public class MetadataController {
  @Autowired
  private MetadataService metadataService;

  @ApiOperation("Get the hippo client summary info")
  @GetMapping
  SmartMonResponse<List<Metadata>> getAll() {
    final List<Metadata> metadata = metadataService.getAll();
    return new SmartMonResponse<>(metadata);
  }
}
