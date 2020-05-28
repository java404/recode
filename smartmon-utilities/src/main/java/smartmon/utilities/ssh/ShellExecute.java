package smartmon.utilities.ssh;

import com.google.common.base.Strings;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import smartmon.utilities.encryption.Encryption;
import smartmon.utilities.misc.TargetHost;

@Slf4j
public class ShellExecute {
  private static final int DEFAULT_STREAM_PAUSE_MS = 1000;
  private final ShellSession session;

  @Setter
  @Getter
  private ShellExecuteEvent executeEvent;

  @Getter
  @Setter
  private boolean disableCommandNewLine = false;

  public ShellExecute(TargetHost hostInfo) {
    this(hostInfo, null);
  }

  public ShellExecute(TargetHost hostInfo, Encryption encryption) {
    this(new ShellSession(hostInfo, encryption));
  }

  public ShellExecute(ShellSession session) {
    this.session = new ShellSession(session);
  }

  public int run(String command) throws JSchException, IOException {
    return run(command, ShellSession.DEFAULT_CONNECT_TIMEOUT, ShellSession.DEFAULT_SO_TIMEOUT);
  }

  public int run(String command, int connTimeout, int soTimeout) throws JSchException, IOException {
    if (Strings.isNullOrEmpty(command)) {
      log.error("Empty shell command");
      throw new JSchException("Invalid shell command");
    }
    log.debug("shell exec: {}", command);
    ChannelExec channelExec = null;
    try {
      this.session.connect(connTimeout, soTimeout);
      final Session jschSession = this.session.getSession();
      channelExec = (ChannelExec)jschSession.openChannel("exec");
      channelExec.setCommand(String.format(disableCommandNewLine ? "%s" : "%s \n", command));
      channelExec.setInputStream(null);

      final InputStream inputStream = channelExec.getInputStream();
      channelExec.connect(connTimeout);
      readOutput(channelExec, inputStream);
      return channelExec.getExitStatus();
    } catch (JSchException | IOException err) {
      log.error("cannot exec command {}", command, err);
      throw err;
    } finally {
      if (channelExec != null) {
        channelExec.disconnect();
      }
      this.session.disconnect();
    }
  }

  private void onShellOutput(String message) {
    if (!Strings.isNullOrEmpty(message) && executeEvent != null) {
      executeEvent.appendOutput(message);
    }
  }

  private void readOutput(ChannelExec channelExec, InputStream inputStream) throws IOException {
    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    while (true) {
      while (reader.ready()) {
        final String line = reader.readLine();
        onShellOutput(line);
      }

      if (channelExec.isClosed()) {
        if (inputStream.available() > 0) {
          continue;
        }
        break;
      }
      try {
        Thread.sleep(DEFAULT_STREAM_PAUSE_MS);
      } catch (InterruptedException err) {
        log.warn("SSH Stream pause err", err);
      }
    }
  }
}
