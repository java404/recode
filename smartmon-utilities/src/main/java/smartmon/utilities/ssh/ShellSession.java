package smartmon.utilities.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import smartmon.utilities.encryption.Encryption;
import smartmon.utilities.misc.TargetHost;

@Slf4j
public class ShellSession {
  private static final int MIN_SO_TIMEOUT_SECONDS = 60 * 1000;
  private static final int MIN_CONN_TIMEOUT_SECONDS = 30 * 1000;
  public static final int DEFAULT_CONNECT_TIMEOUT_SECONDS = 50;
  public static final int DEFAULT_SO_TIMEOUT = MIN_SO_TIMEOUT_SECONDS;
  public static final int DEFAULT_SSH_PORT = 22;

  protected final JSch jsch = new JSch();

  private static final UserInfo jscUserInfo = new UserInfo() {
    @Override
    public String getPassphrase() {
      return null;
    }

    @Override
    public String getPassword() {
      return null;
    }

    @Override
    public boolean promptPassword(String s) {
      return false;
    }

    @Override
    public boolean promptPassphrase(String s) {
      return false;
    }

    @Override
    public boolean promptYesNo(String s) {
      return true;
    }

    @Override
    public void showMessage(String s) {
      // NOP
    }
  };

  @Getter
  private final TargetHost hostInfo;
  @Getter
  private final Encryption encryption;

  @Getter
  private Session session;

  public ShellSession(TargetHost hostInfo, Encryption encryption) {
    this.hostInfo = hostInfo;
    this.encryption = encryption;
  }

  ShellSession(ShellSession session) {
    this(session.getHostInfo(), session.getEncryption());
  }

  public boolean isConnected() {
    return null != session;
  }

  public void disconnect() {
    if (this.session == null) {
      return;
    }
    this.session.disconnect();
    this.session = null;
  }

  public void connect(int connTimeout, int soTimeout) throws JSchException {
    if (isConnected()) {
      this.disconnect();
    }
    this.session = makeSession(connTimeout, soTimeout);
  }

  private Session makeSession(int connTimeout, int soTimeout) throws JSchException {
    log.debug("making jsc session: {}", hostInfo.toString());
    final Session session = jsch.getSession(hostInfo.getUsername(),
      hostInfo.getAddress(), hostInfo.getPort());
    session.setConfig("StrictHostKeyChecking", "no");
    session.setConfig("PreferredAuthentications", "password");
    session.setConfig("TCPKeepAlive", "yes");
    session.setPassword(getSshPassword());
    session.setUserInfo(jscUserInfo);
    session.setTimeout(Math.max(MIN_SO_TIMEOUT_SECONDS, soTimeout));
    session.connect(Math.max(connTimeout  * 1000, MIN_CONN_TIMEOUT_SECONDS));
    return session;
  }

  private String getSshPassword() {
    return encryption == null ? hostInfo.getPassword() : encryption.decryptQuietly(hostInfo.getPassword());
  }
}
