package smartmon.falcon.template;

import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.falcon.user.Team;

import java.util.List;

public interface TemplateService {
  List<HostGroupTemplate> getHostGroupTemplates();

  List<Template> getTemplatesByGroupId(Integer groupId);

  FalconResponseData bindTeamAndTemplate(Integer templateId, String teamName);

  FalconResponseData unbindTeamAndTemplate(Integer templateId, String teamId);

  List<Strategy> getStrategiesByTemplateId(Integer templateId);

  List<Team> getTeamsByTemplateId(Integer templateId);

  List<Template> listTemplate();

}
