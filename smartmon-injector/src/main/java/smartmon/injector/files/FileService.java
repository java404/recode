package smartmon.injector.files;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileService {
  @Value("${smartmon.batch.targetFolder:}")
  private String targetFolder;

  public String getTargetFolder() {
    return targetFolder;
  }

  @PostConstruct
  private void init() {
    if (StringUtils.isEmpty(targetFolder)) {
      try {
        targetFolder = new File(ResourceUtils.getURL("classpath:").getPath())
          .getParentFile().getParentFile().getParent().replace("file:", "") + "/files";
      } catch (Exception ignored) {
      }
    }
    if (!targetFolder.endsWith("/")) {
      targetFolder += "/";
    }
    log.info(targetFolder);
  }

  void saveFile(MultipartFile multipartFile) {
    String filename = multipartFile.getOriginalFilename();
    if (StringUtils.isEmpty(filename)) {
      return;
    }
    String filepath = targetFolder + filename;
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
