package smartmon.injector.files;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import smartmon.injector.config.SmartMonBatchConfig;

@Slf4j
@Service
public class FileService {
  @Autowired
  private SmartMonBatchConfig smartMonBatchConfig;

  void saveFile(MultipartFile multipartFile) {
    String filename = multipartFile.getOriginalFilename();
    if (StringUtils.isEmpty(filename)) {
      return;
    }
    String filepath = smartMonBatchConfig.getFileUploadTargetPath() + filename;
    File file = new File(filepath);
    if (!file.getParentFile().exists()) {
      if (!file.getParentFile().mkdirs()) {
        log.warn("make dir failed: {}", file.getParent());
      }
    }
    try {
      multipartFile.transferTo(file);
      file.setExecutable(true);
    } catch (IOException err) {
      log.error(String.format("save file:[%s] error:", filename), err);
      throw new RuntimeException(err);
    }
  }
}
