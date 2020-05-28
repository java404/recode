package smartmon.gateway.uploader;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import smartmon.utilities.general.SmartMonResponse;

@Slf4j
@Api(tags = "uploader")
@RestController
@RequestMapping("${smartmon.prefix:/gateway/api/v2}/store")
public class SmartMonUploader {
  @Autowired
  private SmartMonStore smartMonStore;

  @ApiImplicitParams({
    @ApiImplicitParam(name = "file", dataType = "__file", paramType = "form", required = true)
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Mono<SmartMonResponse<?>> put(@RequestPart("type") String type,
                                       @RequestPart("file") Mono<FilePart> file) {
    return file.flatMap(part -> Mono.just(smartMonStore.put(type, part)));
  }
}
