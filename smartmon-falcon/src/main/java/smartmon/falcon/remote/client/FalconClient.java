package smartmon.falcon.remote.client;

import feign.Client;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import smartmon.falcon.remote.request.FalconRequestAlarmManager;
import smartmon.falcon.remote.request.FalconRequestEndpointManager;
import smartmon.falcon.remote.request.FalconRequestGraphManager;
import smartmon.falcon.remote.request.FalconRequestHostGroupManager;
import smartmon.falcon.remote.request.FalconRequestStrategyManager;
import smartmon.falcon.remote.request.FalconRequestTeamManager;
import smartmon.falcon.remote.request.FalconRequestTemplateManager;
import smartmon.falcon.remote.request.FalconRequestUserManager;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.alarm.FalconEventCaseDeleteParam;
import smartmon.falcon.remote.types.alarm.FalconEventCases;
import smartmon.falcon.remote.types.alarm.FalconEventCasesQueryParam;
import smartmon.falcon.remote.types.alarm.FalconEventNoteHandleParam;
import smartmon.falcon.remote.types.alarm.FalconEventNoteQueryParam;
import smartmon.falcon.remote.types.alarm.FalconEventNotes;
import smartmon.falcon.remote.types.alarm.FalconEvents;
import smartmon.falcon.remote.types.alarm.FalconEventsQueryParam;
import smartmon.falcon.remote.types.endpoint.FalconEndpoint;
import smartmon.falcon.remote.types.endpoint.FalconEndpointQueryParam;
import smartmon.falcon.remote.types.graph.FalconEndpointCounter;
import smartmon.falcon.remote.types.graph.FalconEndpointCounterQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphHistoryQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphLastPointQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphRecord;
import smartmon.falcon.remote.types.host.FalconHostGroup;
import smartmon.falcon.remote.types.host.FalconHostGroupCreateParam;
import smartmon.falcon.remote.types.host.FalconHostGroupQueryParam;
import smartmon.falcon.remote.types.host.FalconHostGroupUpdateParam;
import smartmon.falcon.remote.types.host.FalconHosts;
import smartmon.falcon.remote.types.strategy.FalconStrategy;
import smartmon.falcon.remote.types.strategy.FalconStrategyQueryParam;
import smartmon.falcon.remote.types.strategy.FalconStrategyUpdateParam;
import smartmon.falcon.remote.types.team.FalconTeamCreateParam;
import smartmon.falcon.remote.types.team.FalconTeamInfo;
import smartmon.falcon.remote.types.team.FalconTeamUpdateParam;
import smartmon.falcon.remote.types.team.FalconTeamUserInfo;
import smartmon.falcon.remote.types.template.FalconActionUpdateParam;
import smartmon.falcon.remote.types.template.FalconHostGroupTemplate;
import smartmon.falcon.remote.types.template.FalconTemplateInfo;
import smartmon.falcon.remote.types.template.FalconTemplates;
import smartmon.falcon.remote.types.user.FalconUser;
import smartmon.falcon.remote.types.user.FalconUserCreateParam;
import smartmon.falcon.remote.types.user.FalconUserDeleteParam;
import smartmon.falcon.remote.types.user.FalconUserUpdateParam;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.remote.RemoteApiBuilder;

@Slf4j
public class FalconClient {
  @Getter
  private final TargetHost targetHost;
  private final FalconRequestHostGroupManager hostGroupManagerRequest;
  private final FalconRequestEndpointManager endPointManagerRequest;
  private final FalconRequestTeamManager teamManagerRequest;
  private final FalconRequestUserManager userManagerRequest;
  private final FalconRequestTemplateManager templateManagerRequest;
  private final FalconRequestStrategyManager strategyManagerRequest;
  private final FalconRequestGraphManager graphManagerRequest;
  private final FalconRequestAlarmManager alarmManagerRequest;

  public FalconClient(TargetHost targetHost) {
    this(targetHost, null);
  }

  /** init falcon client. */
  public FalconClient(TargetHost targetHost, Client client) {
    this.targetHost = targetHost;

    // Falcon Api Requests
    this.hostGroupManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1").build(FalconRequestHostGroupManager.class);
    this.endPointManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1").build(FalconRequestEndpointManager.class);
    this.teamManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1").build(FalconRequestTeamManager.class);
    this.userManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1").build(FalconRequestUserManager.class);
    this.templateManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1").build(FalconRequestTemplateManager.class);
    this.strategyManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1").build(FalconRequestStrategyManager.class);
    this.graphManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1").build(FalconRequestGraphManager.class);
    this.alarmManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client).withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1").build(FalconRequestAlarmManager.class);

  }

  public List<FalconHostGroup> listHostGroups(Map<String, String> apiToken) {
    return this.hostGroupManagerRequest.listHostGroups(apiToken);
  }

  public List<FalconHostGroup> listHostGroupsByGroupRegex(FalconHostGroupQueryParam queryParam,
                                                          Map<String, String> falconApiToken) {
    return this.hostGroupManagerRequest.listHostGroupsByGroupRegex(queryParam, falconApiToken);
  }

  public FalconHostGroup createHostGroup(FalconHostGroupCreateParam createParam, Map<String, String> falconApiToken) {
    return this.hostGroupManagerRequest.createHostGroup(createParam, falconApiToken);
  }

  public FalconResponseData updateHostGroup(FalconHostGroupUpdateParam updateParam,
                                            Map<String, String> falconApiToken) {
    return this.hostGroupManagerRequest.updataHostGroup(updateParam, falconApiToken);
  }

  public FalconResponseData delHostGroup(Integer id, Map<String, String> falconApiToken) {
    return this.hostGroupManagerRequest.delHostGroup(id, falconApiToken);
  }

  public FalconHosts getHostsByGroupId(Integer groupId, Map<String, String> falconApiToken) {
    return this.hostGroupManagerRequest.getHostsByGroupId(groupId, falconApiToken);
  }


  public List<FalconEndpoint> listEndpoints(FalconEndpointQueryParam queryParam, Map<String, String> falconApiToken) {
    return this.endPointManagerRequest.listEndpoints(queryParam, falconApiToken);
  }


  public List<FalconTeamInfo> listTeams(Map<String, String> falconApiToken) {
    return this.teamManagerRequest.listTeams(falconApiToken);
  }

  public FalconResponseData createTeam(FalconTeamCreateParam createParam, Map<String, String> falconApiToken) {
    return this.teamManagerRequest.createTeam(createParam, falconApiToken);
  }

  public FalconResponseData updateTeam(FalconTeamUpdateParam updateParam, Map<String, String> falconApiToken) {
    return this.teamManagerRequest.updateTeam(updateParam, falconApiToken);
  }

  public FalconResponseData deleteTeam(Integer id, Map<String, String> falconApiToken) {
    return this.teamManagerRequest.deleteTeam(id, falconApiToken);
  }

  public List<FalconUser> getUsersByTeamId(Integer teamId, Map<String, String> falconApiToken) {
    final FalconTeamUserInfo teamUserInfo = this.teamManagerRequest.getTeamInfoByTeamId(teamId, falconApiToken);
    return teamUserInfo != null ? teamUserInfo.getUsers() : null;
  }

  public FalconTeamUserInfo getTeamUserInfoByTeamId(Integer teamId, Map<String, String> falconApiToken) {
    return this.teamManagerRequest.getTeamInfoByTeamId(teamId, falconApiToken);
  }


  public List<FalconUser> listUsers(Map<String, String> falconApiToken) {
    return this.userManagerRequest.listUsers(falconApiToken);
  }

  public FalconResponseData createUser(FalconUserCreateParam createParam, Map<String, String> falconApiToken) {
    return this.userManagerRequest.createUser(createParam, falconApiToken);
  }

  public FalconResponseData updateUser(FalconUserUpdateParam updateParam, Map<String, String> falconApiToken) {
    return this.userManagerRequest.updateUser(updateParam, falconApiToken);
  }

  public FalconResponseData deleteUser(FalconUserDeleteParam deleteParam, Map<String, String> falconApiToken) {
    return this.userManagerRequest.deleteUser(deleteParam, falconApiToken);
  }


  public FalconTemplates listTemplates(Map<String, String> falconApiToken) {
    return this.templateManagerRequest.listTemplates(falconApiToken);
  }

  public FalconResponseData updateTemplateAction(FalconActionUpdateParam action, Map<String, String> falconApiToken) {
    return this.templateManagerRequest.updateTemplateAction(action, falconApiToken);
  }

  public FalconHostGroupTemplate getTemplatesByGroupId(Integer groupId, Map<String, String> falconApiToken) {
    return this.templateManagerRequest.getTemplatesByGroupId(groupId, falconApiToken);
  }

  public FalconTemplateInfo getTemplateInfoById(Integer templateId, Map<String, String> falconApiToken) {
    return this.templateManagerRequest.getTemplateInfoById(templateId, falconApiToken);
  }


  public List<FalconStrategy> getStrategiesByTemplateId(FalconStrategyQueryParam queryParam,
                                                        Map<String, String> falconApiToken) {
    return this.strategyManagerRequest.getStrategiesByTemplateId(queryParam, falconApiToken);
  }

  public FalconResponseData updateStrategy(FalconStrategyUpdateParam updateParam,
                                           Map<String, String> falconApiToken) {
    return this.strategyManagerRequest.updateStrategy(updateParam,falconApiToken);
  }

  public FalconStrategy getStrategyById(Integer strategyId,
                                                        Map<String, String> falconApiToken) {
    return this.strategyManagerRequest.getStrategyById(strategyId, falconApiToken);
  }

  public List<FalconEndpointCounter> getEndpointCounters(FalconEndpointCounterQueryParam queryParam,
                                                         Map<String, String> falconApiToken) {
    return this.graphManagerRequest.getEndpointCounters(queryParam, falconApiToken);
  }

  public List<FalconGraphRecord> getGraphHistory(FalconGraphHistoryQueryParam queryParam,
                                                 Map<String, String> falconApiToken) {
    return this.graphManagerRequest.getGraphHistory(queryParam, falconApiToken);
  }

  public List<FalconGraphRecord> getGraphLastPoint(FalconGraphLastPointQueryParam queryParam,
                                                   Map<String, String> falconApiToken) {
    return this.graphManagerRequest.getGraphLastPoint(queryParam, falconApiToken);
  }


  public FalconEventCases listEventCases(FalconEventCasesQueryParam eventCaseQueryParam,
                                         Map<String, String> falconApiToken) {
    return this.alarmManagerRequest.listEventCases(eventCaseQueryParam, falconApiToken);
  }

  public FalconEvents listEvents(FalconEventsQueryParam eventsQueryParam,
                                 Map<String, String> falconApiToken) {
    return this.alarmManagerRequest.listEvents(eventsQueryParam, falconApiToken);
  }

  public FalconResponseData deleteEventCase(String eventId,
                                            Map<String, String> falconApiToken) {
    return this.alarmManagerRequest.deleteEventCase(eventId, falconApiToken);
  }

  public FalconResponseData batchDeleteEventCase(FalconEventCaseDeleteParam eventCaseDeleteParam,
                                                 Map<String, String> falconApiToken) {
    return this.alarmManagerRequest.batchDeleteEventCase(eventCaseDeleteParam, falconApiToken);
  }

  public FalconEventNotes listEventNotesById(FalconEventNoteQueryParam eventNoteQueryParam,
                                             Map<String, String> falconApiToken) {
    return this.alarmManagerRequest.listEventNoteById(eventNoteQueryParam, falconApiToken);
  }

  public FalconResponseData createEventNote(FalconEventNoteHandleParam eventNoteHandleParam,
                                            Map<String, String> falconApiToken) {
    return this.alarmManagerRequest.createEventNote(eventNoteHandleParam, falconApiToken);
  }
}
