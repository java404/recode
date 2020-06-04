package smartmon.core.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.ssh.ShellSession;
import smartmon.webtools.encryption.EncryptionService;

@Service
public class SshService {
  private static final int DEFAULT_CONNECT_TIMEOUT = 50;

  @Autowired
  private EncryptionService encryptionService;

  public boolean canConnect(String sshHostname, String sshUsername, String sshPassword) {
    return canConnect(sshHostname, sshUsername, sshPassword, ShellSession.DEFAULT_SSH_PORT);
  }

  public boolean canConnect(String sshHostname, String sshUsername, String sshPassword, Integer port) {
    port = port == null ? ShellSession.DEFAULT_SSH_PORT : port;
    TargetHost host = TargetHost.builder(sshHostname, port).username(sshUsername).password(sshPassword).build();
    ShellSession shellSession = new ShellSession(host, encryptionService.getEncryption());
    try {
      shellSession.connect(DEFAULT_CONNECT_TIMEOUT, 0);
      return shellSession.isConnected();
    } catch (Exception ignored) {
      return false;
    } finally {
      shellSession.disconnect();
    }
  }
}
