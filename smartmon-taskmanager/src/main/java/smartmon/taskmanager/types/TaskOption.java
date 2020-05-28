package smartmon.taskmanager.types;

import com.google.common.base.Strings;
import lombok.Getter;
import smartmon.utilities.misc.JsonConverter;

@Getter
public class TaskOption {
  private final String action;
  private final String resource;
  private final String parameters;

  public TaskOption(String action, String resource, String parameters) {
    this.action = action;
    this.resource = resource;
    this.parameters = parameters;
  }

  public static TaskOption make(String action, String resource, Object parameters) {
    return new TaskOption(action, resource,
      parameters == null ? "" : JsonConverter.writeValueAsStringQuietly(parameters));
  }

  public <T> T getParameters(Class<T> valueType) {
    return Strings.isNullOrEmpty(parameters) ? null : JsonConverter.readValueQuietly(parameters, valueType);
  }
}
