package smartmon.falcon.remote.types.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import smartmon.falcon.remote.types.strategy.FalconStrategy;

import java.util.List;

@Data
public class FalconTemplateInfo {
  private FalconTemplate template;
  @JsonProperty("parent_name")
  private String parentName;
  @JsonProperty("stratges")
  private List<FalconStrategy> strategies;
  private FalconAction action;
}
