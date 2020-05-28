package smartmon.falcon.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.config.FalconApiComponent;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.template.FalconAction;
import smartmon.falcon.remote.types.template.FalconActionUpdateParam;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.falcon.strategy.model.StrategyOptions;
import smartmon.falcon.user.Team;
import smartmon.utilities.misc.BeanConverter;

@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

  @Autowired
  private FalconApiComponent falconApiComponent;

  @Override
  public List<HostGroupTemplate> getHostGroupTemplates() {
    // TODO: need to add new Falcon API
    return Lists.newArrayList();
  }

  @Override
  public List<Template> getTemplatesByGroupId(Integer groupId) {
    return CollectionUtils.emptyIfNull(falconApiComponent.getFalconClient()
      .getTemplatesByGroupId(groupId,
        falconApiComponent.getApiToken())
      .getTemplates()).stream()
      .map(falconTemplate -> BeanConverter.copy(falconTemplate,
        Template.class)).collect(Collectors.toList());

  }

  @Override
  public FalconResponseData bindTeamAndTemplate(Integer templateId, String teamName) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final Map<String, String> token = falconApiComponent.getApiToken();
    FalconAction action = falconClient.getTemplateInfoById(templateId,
        token).getAction();
    Set<String> teamSet = action == null ? Sets.newHashSet() :
        new HashSet(Arrays.asList(StringUtils
        .split(action.getUic(), ",")));
    teamSet.add(teamName);
    action.setUic(StringUtils.join(teamSet, ","));
    return falconClient.updateTemplateAction(BeanConverter.copy(action,
        FalconActionUpdateParam.class), token);
  }

  @Override
  public FalconResponseData unbindTeamAndTemplate(Integer templateId, String teamName) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final Map<String, String> token = falconApiComponent.getApiToken();
    FalconAction action = falconClient.getTemplateInfoById(templateId,
        token).getAction();
    List<String> teamList = action == null ? Lists.newArrayList() :
        Lists.newArrayList(StringUtils
        .split(action.getUic(), ","));
    if (teamList.contains(teamName)) {
      teamList.remove(teamName);
    }
    action.setUic(StringUtils.join(teamList, ","));
    return falconClient.updateTemplateAction(BeanConverter.copy(action,
        FalconActionUpdateParam.class), token);
  }

  @Override
  public List<Strategy> getStrategiesByTemplateId(Integer templateId) {
    final ObjectMapper mapper = new ObjectMapper();
    return CollectionUtils.emptyIfNull(falconApiComponent.getFalconClient()
      .getTemplateInfoById(templateId, falconApiComponent.getApiToken()).getStrategies())
      .stream()
      .map(s -> {
        Strategy strategy = BeanConverter.copy(s, Strategy.class);
        try {
          if (StringUtils.isNotEmpty(s.getFalconStrategyOptions())) {
            StrategyOptions strategyOptions = mapper.readValue(s
                .getFalconStrategyOptions(), StrategyOptions.class);
            strategy.setStrategyOptions(strategyOptions);
          }
        } catch (JsonProcessingException e) {
          log.error(" get strategyOptions error ", e);
        }
        return strategy;
      })
      .collect(Collectors.toList());
  }

  @Override
  public List<Team> getTeamsByTemplateId(Integer templateId) {
    final FalconClient falconClient = falconApiComponent.getFalconClient();
    final Map<String, String> token = falconApiComponent.getApiToken();
    final List<String> actionUicList = Arrays.asList(StringUtils
        .split(falconClient.getTemplateInfoById(templateId, token)
        .getAction().getUic(), ","));
    return CollectionUtils.emptyIfNull(falconClient.listTeams(token)).stream()
      .map(teamInfo -> teamInfo.getTeam())
      .filter(team -> actionUicList.contains(team.getName()))
      .map(team -> BeanConverter.copy(team, Team.class))
      .collect(Collectors.toList());
  }

  @Override
  public List<Template> listTemplate() {
    return CollectionUtils.emptyIfNull(falconApiComponent.getFalconClient()
      .listTemplates(falconApiComponent.getApiToken()).getTemplates())
      .stream().map(f -> BeanConverter.copy(f.getTemplate(), Template.class))
      .collect(Collectors.toList());
  }
}
