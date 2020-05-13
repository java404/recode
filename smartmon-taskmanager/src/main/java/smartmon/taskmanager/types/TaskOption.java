package smartmon.taskmanager.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import lombok.Data;
import smartmon.utilities.misc.JsonConverter;

@Data
public class TaskOption {
  @JsonIgnore
  public static final TaskOption EMPTY = new TaskOption();
  private JsonNode data;

  public String dump() {
    return data == null ? "" : JsonConverter.writeValueAsStringQuietly(data);
  }

  public static TaskOption make(String data) {
    if (Strings.isNullOrEmpty(data)) {
      return EMPTY;
    }
    final TaskOption result = new TaskOption();
    result.setData(JsonConverter.readTreeQuietly(data));
    return result;
  }
}
