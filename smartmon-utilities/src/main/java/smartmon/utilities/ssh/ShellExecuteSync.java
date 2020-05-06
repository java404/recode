package smartmon.utilities.ssh;

import com.jcraft.jsch.JSchException;
import java.io.IOException;
import smartmon.utilities.encryption.Encryption;
import smartmon.utilities.misc.TargetHost;

public class ShellExecuteSync extends ShellExecute {
  private final StringBuffer resultBuffer = new StringBuffer();

  public ShellExecuteSync(TargetHost hostInfo) {
    this(hostInfo, null);
  }

  public ShellExecuteSync(TargetHost hostInfo, Encryption encryption) {
    super(hostInfo, encryption);
    setExecuteEvent(resultBuffer::append);
  }

  public String invoke(String command) throws JSchException, IOException {
    return invoke(command, ShellSession.DEFAULT_CONNECT_TIMEOUT, ShellSession.DEFAULT_SO_TIMEOUT);
  }

  public String invoke(String command, int connTimeout, int soTimeout) throws JSchException, IOException {
    resultBuffer.setLength(0);
    run(command, connTimeout, soTimeout);
    return resultBuffer.toString();
  }
}
