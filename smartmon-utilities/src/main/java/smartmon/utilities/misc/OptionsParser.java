package smartmon.utilities.misc;

import com.google.common.base.Strings;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.ini4j.Options;

@Slf4j
public class OptionsParser {
  private final Options options;

  public OptionsParser(String content) {
    this(IOUtils.toInputStream(Strings.nullToEmpty(content), StandardCharsets.UTF_8));
  }

  public OptionsParser(File src) {
    Options opts;
    try (FileInputStream content = new FileInputStream(src)) {
      opts = new Options(content);
    } catch (IOException error) {
      log.warn("Cannot parse input file stream");
      opts = new Options();
    }
    this.options = opts;
  }

  public OptionsParser(InputStream input) {
    Options opts;
    try {
      opts = new Options(input);
    } catch (IOException e) {
      log.warn("Cannot parse input stream");
      opts = new Options();
    }
    this.options = opts;
  }

  public String get(String key) {
    return options.get(key);
  }
}
