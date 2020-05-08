package smartmon.utilities.ssh;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import smartmon.utilities.misc.TargetHost;

public class ShellExecuteSyncTest {
  @Test
  @Ignore
  public void test() {
    final TargetHost host = TargetHost.builder("172.24.8.193", 22)
      .username("root").password("root123").build();
    final ShellExecuteSync exec = new ShellExecuteSync(host);

    try {
      final ShellResult result = exec.invoke("ls ~/");
      System.out.println(result);
    } catch (JSchException | IOException err) {
      err.printStackTrace();
    }
  }
}