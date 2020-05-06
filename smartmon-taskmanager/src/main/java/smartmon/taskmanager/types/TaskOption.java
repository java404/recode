package smartmon.taskmanager.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class TaskOption {
  @JsonIgnore
  public static final TaskOption EMPTY = new TaskOption();
  private JsonNode data;
}
