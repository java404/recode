package smartmon.falcon.remote.client.impl;

import feign.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.client.FalconClientService;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.remote.RemoteApiClientBuilder;
import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FalconClientServiceImpl implements FalconClientService {
  private final Map<TargetHost, FalconClient> clients = new ConcurrentHashMap<>();
  private Client remoteApiClient;

  @Value("${smartmon.falcon.apiConnectTimeout:20}")
  private int connectTimeout;
  @Value("${smartmon.falcon.apiRequestTimeout:30}")
  private int requestTimeout;

  @PostConstruct
  void init() {
    this.remoteApiClient = new RemoteApiClientBuilder()
      .withConnectTimeout(connectTimeout).withRequestTimeout(requestTimeout).build();
  }

  @Override
  public FalconClient getClient(String address, int port) {
    final TargetHost targetHost = TargetHost.builder(address, port).build();
    FalconClient client = clients.get(targetHost);
    if (client != null) {
      return client;
    }
    synchronized (this) {
      client = clients.get(targetHost);
      if (client != null) {
        return client;
      }
      final FalconClient newClient = new FalconClient(targetHost, remoteApiClient);
      clients.put(targetHost, newClient);
      return newClient;
    }
  }
}
