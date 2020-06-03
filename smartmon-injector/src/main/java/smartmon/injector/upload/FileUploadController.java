package smartmon.injector.upload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.injector.config.SmartMonBatchConfig;

@Slf4j
@RequestMapping("${smartmon.api.prefix:/injector/api/v2}/upload")
@RestController
public class FileUploadController {
  @Autowired
  private FileUploadService fileUploadService;
  @Autowired
  private SmartMonBatchConfig smartMonBatchConfig;

  @PostMapping
  public void upload(@RequestBody FileChunkParameter param) throws Exception {
    fileUploadService.uploadFileByMappedByteBuffer(param, smartMonBatchConfig.getFileUploadTargetPath());
  }
}
