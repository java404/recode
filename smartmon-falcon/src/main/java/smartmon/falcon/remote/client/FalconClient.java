package smartmon.falcon.remote.client;

import feign.Client;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import smartmon.falcon.remote.request.FalconRequestEndpointManager;
import smartmon.falcon.remote.request.FalconRequestGraphManager;
import smartmon.falcon.remote.request.FalconRequestHostGroupManager;
import smartmon.falcon.remote.request.FalconRequestStrategyManager;
import smartmon.falcon.remote.request.FalconRequestTeamManager;
import smartmon.falcon.remote.request.FalconRequestTemplateManager;
import smartmon.falcon.remote.request.FalconRequestUserManager;
import smartmon.falcon.remote.types.FalconResponseData;
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
import smartmon.falcon.remote.types.team.FalconTeamCreateParam;
import smartmon.falcon.remote.types.team.FalconTeamInfo;
import smartmon.falcon.remote.types.team.FalconTeamUpdateParam;
import smartmon.falcon.remote.types.template.FalconHostGroupTemplate;
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

  }

  public List<FalconHostGroup> listHostGroups() {
    return this.hostGroupManagerRequest.listHostGroups();
  }

  public List<FalconHostGroup> listHostGroupsByGroupRegex(FalconHostGroupQueryParam queryParam) {
    return this.hostGroupManagerRequest.listHostGroupsByGroupRegex(queryParam);
  }

  public FalconHostGroup createHostGroup(FalconHostGroupCreateParam createParam) {
    return this.hostGroupManagerRequest.createHostGroup(createParam);
  }

  public FalconResponseData updateHostGroup(FalconHostGroupUpdateParam updateParam) {
    return this.hostGroupManagerRequest.updataHostGroup(updateParam);
  }

  public FalconResponseData delHostGroup(Integer id) {
    return this.hostGroupManagerRequest.delHostGroup(id);
  }

  public FalconHosts getHostsByGroupId(Integer groupId) {
    return this.hostGroupManagerRequest.getHostsByGroupId(groupId);
  }


  public List<FalconEndpoint> listEndpoints(FalconEndpointQueryParam queryParam) {
    return this.endPointManagerRequest.listEndpoints(queryParam);
  }


  public List<FalconTeamInfo> listTeams() {
    return this.teamManagerRequest.listTeams();
  }

  public FalconResponseData createTeam(FalconTeamCreateParam createParam) {
    return this.teamManagerRequest.createTeam(createParam);
  }

  public FalconResponseData updateTeam(FalconTeamUpdateParam updateParam) {
    return this.teamManagerRequest.updateTeam(updateParam);
  }

  public FalconResponseData deleteTeam(Integer id) {
    return this.teamManagerRequest.deleteTeam(id);
  }

  public List<FalconUser> getUserByTeamId(Integer teamId) {
    final FalconTeamInfo teamInfoByTeamId = this.teamManagerRequest.getTeamInfoByTeamId(teamId);
    return teamInfoByTeamId != null ? teamInfoByTeamId.getUsers() : null;
  }

  public List<FalconUser> listUsers() {
    return this.userManagerRequest.listUsers();
  }

  public FalconResponseData createUser(FalconUserCreateParam createParam) {
    return this.userManagerRequest.createUser(createParam);
  }

  public FalconResponseData updateUser(FalconUserUpdateParam updateParam) {
    return this.userManagerRequest.updateUser(updateParam);
  }

  public FalconResponseData deleteUser(FalconUserDeleteParam deleteParam) {
    return this.userManagerRequest.deleteUser(deleteParam);
  }


  public FalconTemplates listTemplates() {
    return this.templateManagerRequest.listTemplates();
  }

  public FalconHostGroupTemplate getTemplatesByGroupId(Integer groupId) {
    return this.templateManagerRequest.getTemplatesByGroupId(groupId);
  }


  public List<FalconStrategy> getStrategiesByTemplateId(FalconStrategyQueryParam queryParam) {
    return this.strategyManagerRequest.getStrategiesByTemplateId(queryParam);
  }


  public List<FalconEndpointCounter> getEndpointCounters(FalconEndpointCounterQueryParam queryParam) {
    return this.graphManagerRequest.getEndpointCounters(queryParam);
  }

  public List<FalconGraphRecord> getGraphHistory(FalconGraphHistoryQueryParam queryParam) {
    if (CollectionUtils.isEmpty(queryParam.getCounters())) {

    }
    return this.graphManagerRequest.getGraphHistory(queryParam);
  }

  public List<FalconGraphRecord> getGraphLastPoint(FalconGraphLastPointQueryParam queryParam) {
    return this.graphManagerRequest.getGraphLastPoint(queryParam);
  }


}
