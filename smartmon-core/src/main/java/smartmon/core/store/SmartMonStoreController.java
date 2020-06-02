package smartmon.core.store;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.webtools.page.SmartMonPageParams;
import smartmon.webtools.page.SmartMonPageResponseBuilder;


@Api(tags = "utils")
@RestController
@RequestMapping("${smartmon.api.prefix:/core/api/v2}/store")
@Slf4j
public class SmartMonStoreController {
  @Autowired
  private SmartMonStoreService storeService;

  @ApiOperation("Get Store file info by id")
  @GetMapping("{fileId}")
  public SmartMonResponse<SmartMonStoreFile> findById(@PathVariable("fileId") Long fileId) {
    return new SmartMonResponse<>(storeService.findById(fileId));
  }

  @ApiOperation("Get file info list")
  @SmartMonPageParams
  @GetMapping
  public SmartMonResponse<Page<SmartMonStoreFile>> findAll(ServerHttpRequest request) {
    return new SmartMonPageResponseBuilder<SmartMonStoreFile>(storeService.findAll(),
      request, "fileId").build();
  }

  @ApiOperation("Delete files by the Id set.")
  @DeleteMapping
  public SmartMonResponse<List<SmartMonStoreFile>> deleteItems(@RequestParam("file") Set<Long> fileIds) {
    return new SmartMonResponse<List<SmartMonStoreFile>>(storeService.remove(fileIds));
  }
}
