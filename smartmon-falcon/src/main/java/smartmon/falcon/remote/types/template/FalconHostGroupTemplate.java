package smartmon.falcon.remote.types.template;

import lombok.Data;
import smartmon.falcon.remote.types.host.FalconHostGroup;

import java.util.List;

@Data
public class FalconHostGroupTemplate {
  private FalconHostGroup hostGroup;
  private List<FalconTemplate> templates;
}
