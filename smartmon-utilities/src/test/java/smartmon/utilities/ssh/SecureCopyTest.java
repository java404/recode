package smartmon.utilities.ssh;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Ignore;
import org.junit.Test;
import smartmon.utilities.misc.TargetHost;

public class SecureCopyTest {
  @Test
  @Ignore
  public void test() {
    final SecureCopyParameters parameters = new SecureCopyParameters();
    final Set<String> sources = Stream.of("demo.sh", "base1/demo.sh",
      "base2/demo.sh", "base2/base3/demo.sh").collect(Collectors.toCollection(HashSet::new));
    parameters.setTargetFolder("/tmp/scp-test");
    parameters.setSourceFile(sources);
    parameters.setSourceRoot("d:/tmp");
    parameters.setRecreateTargetFolder(true);
    final String log = sources.toString();

    final TargetHost host = TargetHost.builder("172.24.8.55", 22)
      .username("root").password("root123").build();
    final SecureCopy scp = new SecureCopy(host);
    scp.setCopyEvent(new SecureCopyEvent() {
      @Override
      public void copyProgress(SecureCopyParameters options,
                               String source, String target,
                               long sourceSize, long count, boolean ended) {
        System.out.println("From: " + source + ", to: "
          + target + String.format("(%d/%d)", count, sourceSize));
      }
    });
    try {
      scp.copy(parameters);
    } catch (JSchException | IOException | SftpException err) {
      err.printStackTrace();
    }
    System.out.println("done");
  }
}
