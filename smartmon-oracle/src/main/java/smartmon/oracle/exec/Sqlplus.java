package smartmon.oracle.exec;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.zeroturnaround.exec.ProcessExecutor;
import smartmon.utilities.misc.LinesParser;

@Slf4j
public class Sqlplus {
  private final String oracleHome;

  public Sqlplus(String oracleHome) {
    this.oracleHome = oracleHome;
  }

  public String getSqlPlusBin() {
    return FilenameUtils.concat(oracleHome, "bin/sqlplus");
  }

  private Map<String, String> makeEnv() {
    return Collections.singletonMap("ORACLE_HOME", oracleHome);
  }

  // Output example:
  //
  // sqlplus -V
  //  \n
  //  SQL*Plus: Release 12.1.0.2.0 Production
  //  \n
  public String getVersion() {
    try {
      final String output = new ProcessExecutor().command(getSqlPlusBin(), "-V")
        .readOutput(true).environment(makeEnv()).execute().outputUTF8();
      return Strings.emptyToNull(LinesParser.matchGroup(output,
        "(\\d+\\.\\d+\\.\\d+\\.\\d+\\.\\d+)", 1));
    } catch (IOException | InterruptedException | TimeoutException error) {
      log.error("Cannot run sqlplus command: ", error);
      return null;
    }
  }
}
