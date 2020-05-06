package smartmon.falcon.remote.client;

import feign.Client;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import smartmon.falcon.remote.request.FalconRequestEndpointManager;
import smartmon.falcon.remote.request.FalconRequestHostGroupManager;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.endpoint.FalconEndpoint;
import smartmon.falcon.remote.types.endpoint.FalconEndpointQueryParam;
import smartmon.falcon.remote.types.host.FalconHostGroup;
import smartmon.falcon.remote.types.host.FalconHostGroupCreateParam;
import smartmon.falcon.remote.types.host.FalconHostGroupQueryParam;
import smartmon.falcon.remote.types.host.FalconHostGroupUpdateParam;
import smartmon.falcon.remote.types.host.FalconHosts;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.remote.RemoteApiBuilder;
import java.util.List;

@Slf4j
public class FalconClient {
  @Getter
  private final TargetHost targetHost;
  private final FalconRequestHostGroupManager hostGroupManagerRequest;
  private final FalconRequestEndpointManager endPointManagerRequest;

  public FalconClient(TargetHost targetHost) {
    this(targetHost, null);
  }

  /** init falcon client. */
  public FalconClient(TargetHost targetHost, Client client) {
    this.targetHost = targetHost;

    // Falcon Api Requests
    this.hostGroupManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client)
      .withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1")
      .build(FalconRequestHostGroupManager.class);
    this.endPointManagerRequest = new RemoteApiBuilder(targetHost)
      .withClient(client)
      .withDecoder(new FalconResponseDecode())
      .withApiPrefix("/api/v1")
      .build(FalconRequestEndpointManager.class);
  }

  public List<FalconHostGroup> listHostGroups() {
    return this.hostGroupManagerRequest.listHostGroups();
  }

  public List<FalconHostGroup> listHostGroupsByGroupRegex(String groupRegex) {
    return this.hostGroupManagerRequest.listHostGroupsByGroupRegex(new FalconHostGroupQueryParam(groupRegex));
  }

  public FalconHostGroup createHostGroup(String name, String note) {
    return this.hostGroupManagerRequest.createHostGroup(new FalconHostGroupCreateParam(name, note));
  }

  public FalconResponseData updateHostGroup(Integer id, String name, String note) {
    return this.hostGroupManagerRequest.updataHostGroup(new FalconHostGroupUpdateParam(id, name, note));
  }

  public FalconResponseData delHostGroup(Integer id) {
    return this.hostGroupManagerRequest.delHostGroup(id);
  }

  public FalconHosts getHostsByGroupId(Integer groupId) {
    return this.hostGroupManagerRequest.getHostsByGroupId(groupId);
  }


  public List<FalconEndpoint> listEndpoints(String endpointRegex) {
    return this.endPointManagerRequest.listEndpoints(new FalconEndpointQueryParam(endpointRegex));
  }
}
