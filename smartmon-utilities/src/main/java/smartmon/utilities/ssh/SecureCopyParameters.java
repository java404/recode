package smartmon.utilities.ssh;

import java.util.Set;
import lombok.Data;

@Data
public class SecureCopyParameters {
  private String sourceRoot;
  private Set<String> sourceFile;
  private String targetFolder;
  private boolean recreateTargetFolder = false;
}
