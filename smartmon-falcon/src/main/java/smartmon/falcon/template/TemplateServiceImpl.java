package smartmon.falcon.template;

import com.google.common.collect.Lists;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TemplateServiceImpl implements TemplateService {
  @Override
  public List<HostGroupTemplate> getHostGroupTemplates() {
    // TODO: need to add new Falcon API
    return Lists.newArrayList();
  }

  @Override
  public List<Template> getTemplatesByGroupId(Integer groupId) {
    // TODO: call GET method api/v1/hostgroup/{groupId}/template, and parse the values of key 'templates'
    return Lists.newArrayList();
  }

  @Override
  public void bindTeamAndTemplate(Integer templateId, Integer teamId) {
    // TODO: call PUT method api/v1/template/action
  }

  @Override
  public void unbindTeamAndTemplate(Integer templateId, Integer teamId) {
    // TODO: call PUT method api/v1/template/action
  }

  @Override
  public List<String> getTeamNamesByTemplateId(Integer templateId) {
    // TODO: call GET method api/v1/template/{templateId}
    return Lists.newArrayList();
  }
}
