package smartmon.falcon.template;

import java.util.List;

public interface TemplateService {
  List<HostGroupTemplate> getHostGroupTemplates();

  List<Template> getTemplatesByGroupId(Integer groupId);

  void bindTeamAndTemplate(Integer templateId, Integer teamId);

  void unbindTeamAndTemplate(Integer templateId, Integer teamId);

  List<String> getTeamNamesByTemplateId(Integer templateId);
}
