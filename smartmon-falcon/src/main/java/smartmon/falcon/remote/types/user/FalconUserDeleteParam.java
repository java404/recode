package smartmon.falcon.remote.types.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalconUserDeleteParam {
  @JsonProperty("user_id")
  private Integer userId;
}
