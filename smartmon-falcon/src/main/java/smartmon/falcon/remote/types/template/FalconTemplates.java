package smartmon.falcon.remote.types.template;

import lombok.Data;

import java.util.List;

@Data
public class FalconTemplates {
  private List<FalconTemplateInfo> templates;
}
