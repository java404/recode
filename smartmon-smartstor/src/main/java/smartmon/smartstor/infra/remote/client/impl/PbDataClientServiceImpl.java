package smartmon.smartstor.infra.remote.client.impl;

import feign.Client;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartmon.smartstor.infra.remote.client.PbDataClient;
import smartmon.smartstor.infra.remote.client.PbDataClientService;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.remote.RemoteApiClientBuilder;

@Service
public class PbDataClientServiceImpl implements PbDataClientService {
  private final Map<TargetHost, PbDataClient> clients = new ConcurrentHashMap<>();
  private Client remoteApiClient;

  @Value("${smartmon.smartstor.apiConnectTimeout:20}")
  private int connectTimeout;
  @Value("${smartmon.smartstor.apiRequestTimeout:30}")
  private int requestTimeout;

  @PostConstruct
  void init() {
    this.remoteApiClient = new RemoteApiClientBuilder()
        .withConnectTimeout(connectTimeout).withRequestTimeout(requestTimeout).build();
  }

  @Override
  public PbDataClient getClient(String address, int port) {
    final TargetHost targetHost = TargetHost.builder(address, port).build();
    PbDataClient client = clients.get(targetHost);
    if (client != null) {
      return client;
    }
    synchronized (this) {
      client = clients.get(targetHost);
      if (client != null) {
        return client;
      }
      final PbDataClient newClient = new PbDataClient(targetHost, remoteApiClient);
      clients.put(targetHost, newClient);
      return newClient;
    }
  }
}
