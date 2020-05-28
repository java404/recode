package smartmon.injector.executors;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Service;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.LogOutputStream;

@Service
public class ExecutorService {
  public void executeShellCommand(String command, LogOutputProcessor logOutputProcessor) {
    try {
      int code = new ProcessExecutor().command("/bin/sh", "-c", command)
        .redirectOutput(new LogOutputStream() {
          @Override
          protected void processLine(String line) {
            if (logOutputProcessor != null) {
              logOutputProcessor.processLine(line);
            }
          }
        }).execute().getExitValue();
      if (!Objects.equals(0, code)) {
        throw new RuntimeException("execute command error");
      }
    } catch (IOException | InterruptedException | TimeoutException err) {
      throw new RuntimeException(err);
    }
  }

  public String executeShellCommand(String command) {
    return executeShellCommand(command, 0);
  }

  public String executeShellCommand(String command, int... validExitCodes) {
    try {
      ProcessResult processResult = new ProcessExecutor()
        .command("/bin/sh", "-c", command)
        .exitValues(validExitCodes)
        .readOutput(true).execute();
      String result = processResult.outputString();
      if (result.endsWith("\n")) {
        result = result.substring(0, result.length() - 1);
      }
      return result;
    } catch (IOException | InterruptedException | TimeoutException | InvalidExitValueException err) {
      throw new RuntimeException(err);
    }
  }
}
