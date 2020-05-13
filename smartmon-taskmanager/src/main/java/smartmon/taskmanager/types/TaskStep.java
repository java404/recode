package smartmon.taskmanager.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import lombok.Data;
import smartmon.utilities.misc.JsonConverter;

@Data
public class TaskStep {
  public static final String STRATEGY_CONTINUE = "continue";
  public static final String STRATEGY_BREAK = "break";

  private Long taskStepId;

  private Long taskId;
  private String strategy;

  private Object detail;
  private String detailContent;

  private Date createTime = new Date();
  private Date completeTime = new Date();

  private String stepLog;

  @JsonIgnore
  private Runnable step;

  @JsonIgnore
  private final StringBuffer logBuffer = new StringBuffer();
  @JsonIgnore
  private LogBufferEvents logBufferEvents;

  public TaskStep() {
    this(null);
  }

  public TaskStep(String strategy, Runnable step) {
    this.strategy = strategy;
    this.step = step;
  }

  public TaskStep(Runnable step) {
    this(STRATEGY_BREAK, step);
  }

  @JsonIgnore
  public void appendLog(String line) {
    logBuffer.append(line).append("\n");
    if (logBufferEvents != null) {
      logBufferEvents.bufferUpdated(this);
    }
  }

  public void flush() {
    detailContent = JsonConverter.writeValueAsStringQuietly(detail);
    stepLog = logBuffer.toString();
  }

  @JsonIgnore
  public boolean isBreakStrategy() {
    return STRATEGY_BREAK.equalsIgnoreCase(strategy);
  }
}
