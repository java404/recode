package smartmon.oracle.exec;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.exec.ProcessExecutor;
import smartmon.oracle.types.OracleNode;
import smartmon.utilities.misc.LinesParser;

@Slf4j
public class Olsnodes {
  private static final String CMD = "olsnodes";

  private static class Holder {
    static final Olsnodes INSTANCE = new Olsnodes();
  }

  public static Olsnodes getInstance() {
    return Holder.INSTANCE;
  }

  // Ouptput example:
  //
  // olsnodes -n -i -a
  //    smartmoncluster1        1       smartmoncluster1-vip    Hub
  //    smartmoncluster2        2       smartmoncluster2-vip    Hub
  public List<OracleNode> listNodes() {
    try {
      final String output = new ProcessExecutor().command(CMD, "-n", "-i", "-a")
        .readOutput(true).execute().outputUTF8();
      return LinesParser.parse(output, "\\s", input -> {
        if (input == null || input.length < 3) {
          return null;
        }
        final OracleNode item = new OracleNode();
        item.setName(input[0]);
        item.setNumber((Integer.parseInt(input[1])));
        item.setVip(input[2]);
        return item;
      });
    } catch (IOException | InterruptedException | TimeoutException error) {
      log.error("Cannot run olsnodes: ", error);
      return Collections.emptyList();
    }
  }
}
