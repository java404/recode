package smartmon.falcon.remote.types.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconTemplate {
  private Integer id;
  @JsonProperty("tpl_name")
  private String templateName;
  @JsonProperty("parent_id")
  private Integer parentId;
  @JsonProperty("create_user")
  private String createUser;
}
