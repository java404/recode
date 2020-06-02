package smartmon.falcon.template;

import java.util.List;
import lombok.Data;
import smartmon.falcon.strategy.model.Strategy;

@Data
public class TemplateInfo {
  private Template template;
  private String parentName;
  private List<Strategy> strategies;
  private Action action;
}
