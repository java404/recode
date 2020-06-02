package smartmon.injector.config;

import java.io.File;
import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Slf4j
@Configuration
public class SmartMonBatchConfig {
  @Value("${smartmon.batch.rootFolder:}")
  private String rootFolder;

  @PostConstruct
  private void init() {
    if (StringUtils.isEmpty(rootFolder)) {
      try {
        rootFolder = new File(ResourceUtils.getURL("classpath:").getPath())
          .getParentFile().getParentFile().getParent().replace("file:", "");
      } catch (Exception err) {
        log.error("get root path error", err);
        return;
      }
    }
    if (!rootFolder.endsWith("/")) {
      rootFolder += "/";
    }
    log.info(rootFolder);
  }

  public String getFileUploadTargetPath() {
    return rootFolder + "files/";
  }

  public String getScriptsPath() {
    return rootFolder + "scripts/";
  }
}
