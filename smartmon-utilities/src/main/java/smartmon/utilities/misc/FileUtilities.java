package smartmon.utilities.misc;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;

public class FileUtilities {
  public static File createTempFile(String rootFolder,
                                    String prefix, String suffix) throws IOException {
    return File.createTempFile(prefix, suffix, new File(rootFolder));
  }

  public static File createTempFile(String rootFolder) throws IOException {
    return createTempFile(rootFolder, "smartmon", "-data");
  }

  public static File saveTempFile(InputStream source, String rootFolder,
                                  String prefix, String suffix) throws IOException {
    final File tmpFile = createTempFile(rootFolder, prefix, suffix);
    FileUtils.copyInputStreamToFile(source, tmpFile);
    return tmpFile;
  }

  public static void deleteQuietly(File file) {
    if (file != null) {
      FileUtils.deleteQuietly(file);
    }
  }

  public static String makeDirName(String root) {
    if (Strings.isNullOrEmpty(root)) {
      return "/";
    }
    return root.endsWith("/") ? root : root + "/";
  }
}
