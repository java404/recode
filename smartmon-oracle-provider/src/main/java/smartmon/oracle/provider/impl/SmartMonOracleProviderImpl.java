package smartmon.oracle.provider.impl;

import feign.Client;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartmon.oracle.provider.SmartMonOracleProvider;
import smartmon.oracle.provider.SmartMonOracleRemote;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.remote.RemoteApiBuilder;
import smartmon.utilities.remote.RemoteApiClientBuilder;


@Service
public class SmartMonOracleProviderImpl implements SmartMonOracleProvider {
  @Value("${smartmon.oracle.apiConnectTimeout:50}")
  private int connectTimeout;

  @Value("${smartmon.oracle.apiRequestTimeout:60}")
  private int requestTimeout;

  private Client oracleClient;

  private final Map<TargetHost, SmartMonOracleRemote> clients = new ConcurrentHashMap<>();

  @PostConstruct
  void init() {
    oracleClient = new RemoteApiClientBuilder()
      .withConnectTimeout(connectTimeout).withRequestTimeout(requestTimeout).build();
  }

  public SmartMonOracleRemote getClient(TargetHost remote) {
    SmartMonOracleRemote client = clients.get(remote);
    if (client != null) {
      return client;
    }
    synchronized (this) {
      client = clients.get(remote);
      if (client != null) {
        return client;
      }
      final SmartMonOracleRemote newClient =  new RemoteApiBuilder(remote)
        .withClient(oracleClient).withApiPrefix("/injector/api/v2/oracle")
        .build(SmartMonOracleRemote.class);
      clients.put(remote, newClient);
      return newClient;
    }
  }

  @Override
  public SmartMonOracleRemote getOracleRemote(TargetHost remote) {
    return getClient(remote);
  }
}
