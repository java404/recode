package smartmon.vhe.config;

import com.google.common.base.Strings;

import java.io.File;
import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import smartmon.taskmanager.types.TaskContext;

@Slf4j
@Getter
@Component
public class SmartMonBatchConfig {
  @Value("${smartmon.batch.sourceRoot:}")
  private String sourceRoot;

  @PostConstruct
  public void init() {
    if (StringUtils.isEmpty(sourceRoot)) {
      try {
        sourceRoot = new File(ResourceUtils.getURL("classpath:").getPath())
          .getParentFile().getParentFile().getParent().replace("file:", "");
      } catch (Exception ignored) {
      }
    }
    if (!sourceRoot.endsWith("/")) {
      sourceRoot += "/";
    }
    log.info(sourceRoot);
  }

  public String getRemoteWorkFolder() {
    return "vhe-task-" + TaskContext.currentTaskContext().getTaskId() + "/";
  }
}
