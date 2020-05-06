package smartmon.utilities.ssh;

import com.google.common.base.Strings;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import smartmon.utilities.encryption.Encryption;
import smartmon.utilities.misc.TargetHost;

@Slf4j
public class SecureCopy {
  private final ShellSession session;

  @Getter
  @Setter
  private SecureCopyEvent copyEvent;

  public SecureCopy(TargetHost hostInfo) {
    this(hostInfo, null);
  }

  public SecureCopy(TargetHost hostInfo, Encryption encryption) {
    this.session = new ShellSession(hostInfo, encryption);
  }

  public boolean copy(SecureCopyParameters options)
      throws JSchException, IOException, SftpException {
    return copy(options, ShellSession.DEFAULT_CONNECT_TIMEOUT, ShellSession.DEFAULT_SO_TIMEOUT);
  }

  /** Copy files to remote host via SCP protocol. */
  public boolean copy(SecureCopyParameters options, int connTimeout, int soTimeout)
      throws JSchException, IOException, SftpException {
    if (CollectionUtils.isEmpty(options.getSourceFile())
      || Strings.isNullOrEmpty(options.getSourceRoot())
      || Strings.isNullOrEmpty(options.getTargetFolder())) {
      throw new JSchException("scp error: invalid source filename or target filename");
    }
    ChannelSftp channelSftp = null;
    try {
      recreateTargetFolder(options);
      session.connect(connTimeout, soTimeout);
      final Session jschSession = session.getSession();
      channelSftp = (ChannelSftp)jschSession.openChannel("sftp");
      channelSftp.connect(connTimeout);

      final Set<String> sources = options.getSourceFile();
      for (String source : sources) {
        if (Strings.isNullOrEmpty(source)) {
          continue;
        }
        log.debug("copy file {} to host {} - {}",
          source, session.getHostInfo().toString(), options.getTargetFolder());
        copySourceFile(options, source, channelSftp);
      }
      try {
        changeModeBit(options);
      } catch (Exception err) {
        log.error("cannot change ssh file mod", err);
      }
    } catch (JSchException | IOException | SftpException err) {
      log.error("JSch error: ", err);
      throw err;
    } finally {
      if (channelSftp != null) {
        channelSftp.disconnect();
      }
      this.session.disconnect();
    }
    return true;
  }

  private void changeModeBit(SecureCopyParameters options)
      throws IOException, JSchException {
    final ShellExecute shellExecute = new ShellExecute(session);
    shellExecute.run(String.format("(chmod -R a+rwx %s && sleep 2 && exit)", options.getTargetFolder()));
  }

  private void recreateTargetFolder(SecureCopyParameters options)
      throws IOException, JSchException {
    final ShellExecute shellExecute = new ShellExecute(session);
    final Set<String>  sources = options.getSourceFile();
    final StringBuilder sb = new StringBuilder();
    final String targetFolder = options.getTargetFolder();
    sb.append(" mkdir -pv ").append(targetFolder).append(" && ");
    for (final String source : sources) {
      final int index = source.lastIndexOf('/');
      if (index <= 0) {
        continue;
      }
      sb.append("mkdir -pv ").append(targetFolder).append("/")
        .append(source, 0, index).append(" && ");
    }
    sb.append(" echo 'done' && sleep 2 && exit ");
    if (options.isRecreateTargetFolder()) {
      shellExecute.run(String.format("(rm -Rvf %s && %s)", targetFolder, sb.toString()));
    } else {
      shellExecute.run(String.format("(%s)", sb.toString()));
    }
  }

  private void copySourceFile(SecureCopyParameters options, String source, ChannelSftp channelSftp)
      throws IOException, SftpException {
    final File sourceFile = new File(String.format("%s/%s", options.getSourceRoot(), Strings.nullToEmpty(source)));
    copyStream(options, source, sourceFile, channelSftp);
  }

  private String makeTargetFilename(SecureCopyParameters options, String source) {
    return String.format("%s/%s", options.getTargetFolder(), source);
  }

  private void copyStream(SecureCopyParameters options,
                          String sourceItem, File sourceFile,
                          ChannelSftp channelSftp)
      throws IOException, SftpException {
    try (FileInputStream source = new FileInputStream(sourceFile)) {
      channelSftp.put(source, makeTargetFilename(options, sourceItem), new SftpProgressMonitor() {
        private String src;
        private String destination;
        private long countSize = 0;
        private final long sourceSize = sourceFile.length();

        @Override
        public void init(int op, String src, String destination, long max) {
          this.src = src;
          this.destination = destination;
          fileCopyProgress(options, src, destination, sourceSize, countSize, false);
        }

        @Override
        public boolean count(long count) {
          this.countSize += count;
          fileCopyProgress(options, src, destination, sourceSize, countSize, false);
          return count > 0;
        }

        @Override
        public void end() {
          fileCopyProgress(options, src, destination, sourceSize, countSize, true);
        }
      });
    }
  }

  private void fileCopyProgress(SecureCopyParameters options,
                                String source, String destination,
                                long sourceSize, long count, boolean ended) {
    if (copyEvent != null) {
      copyEvent.copyProgress(options, source, destination, sourceSize, count, ended);
    }
  }
}
