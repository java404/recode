package smartmon.utilities.misc;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class FileUtilitiesTest {
  @Ignore
  @Test
  public void test() throws IOException {
    final String tempRootFolder = "d:/tmp";
    final File tmpFile = FileUtilities.createTempFile(tempRootFolder);
    tmpFile.deleteOnExit();
    Assert.assertFalse(Strings.isNullOrEmpty(tmpFile.getAbsolutePath()));
  }
}
