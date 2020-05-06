package smartmon.core.metadata;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.core.metadata.types.Metadata;
import smartmon.utilities.general.SmartMonResponse;

@Api(tags = "utils")
@RestController
@RequestMapping("${smartmon.api.prefix:/core/api/v2}/metadata")
@Slf4j
public class MetadataController {
  @Autowired
  private MetadataService metadataService;

  @ApiOperation("Get Items")
  @GetMapping
  SmartMonResponse<List<Metadata>> getItems(@RequestParam(required = false) String key) {
    return new SmartMonResponse<>(metadataService.findItems(key));
  }

  @ApiOperation("Find one item")
  @GetMapping("{name}")
  SmartMonResponse<Metadata> getItem(@PathVariable("name") String name) {
    return new SmartMonResponse<>(metadataService.findItem(name));
  }

  @ApiOperation("Put Items")
  @PostMapping
  SmartMonResponse<String> put(@RequestBody List<Metadata> items) {
    metadataService.put(items);
    return SmartMonResponse.OK;
  }
}
