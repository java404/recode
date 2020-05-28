package smartmon.core.agent;

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
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.ssh.ShellExecute;

@Slf4j
@Getter
@Component
public class SmartMonBatchConfig {
  @Value("${smartmon.batch.sourceRoot:}")
  private String sourceRoot;
  @Value("${smartmon.batch.targetFolder:}")
  private String targetFolder;

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

  public String getWorkFolder() {
    String targetFolder = getTargetFolder();
    String folder = Strings.nullToEmpty(targetFolder).endsWith("/") ? targetFolder :
      Strings.nullToEmpty(targetFolder) + "/";
    return folder + "task-" + TaskContext.currentTaskContext().getTaskId();
  }

  public void deleteTempDir(TargetHost targetHost) {
    String dir = getWorkFolder();
    try {
      new ShellExecute(targetHost).run(String.format("rm -rf %s", dir));
    } catch (Exception err) {
      log.warn(String.format("delete temp dir error, host[%s], dir[%s]", targetHost.getAddress(), dir), err);
    }
  }
}
