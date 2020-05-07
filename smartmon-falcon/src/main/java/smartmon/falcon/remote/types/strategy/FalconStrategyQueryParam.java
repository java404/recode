package smartmon.falcon.remote.types.strategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconStrategyQueryParam {
  @JsonProperty("tid")
  private Integer templateId;
}
