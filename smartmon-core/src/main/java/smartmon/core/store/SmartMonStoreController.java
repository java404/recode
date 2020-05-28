package smartmon.core.store;

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
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonResponse;


@Api(tags = "utils")
@RestController
@RequestMapping("${smartmon.api.prefix:/core/api/v2}/store")
@Slf4j
public class SmartMonStoreController {
  @Autowired
  private SmartMonStoreService storeService;

  @ApiOperation("Put file")
  @PostMapping
  public SmartMonResponse<SmartMonStoreFile> put(@RequestBody SmartMonStoreInputFile localFile) {
    return new SmartMonResponse<>(storeService.put(localFile));
  }

  @ApiOperation("Get Store file info by id")
  @GetMapping("{fileId}")
  public SmartMonResponse<SmartMonStoreFile> findById(@PathVariable("fileId") Long fileId) {
    return new SmartMonResponse<>(storeService.findById(fileId));
  }


  @ApiOperation("Get file info list")
  @GetMapping
  public SmartMonResponse<List<SmartMonStoreFile>> findAll() {
    return new SmartMonResponse<>(storeService.findAll());
  }
}
