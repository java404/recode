package smartmon.smartstor.infra.remote.client;

public interface PbDataClientService {
  PbDataClient getClient(String address, int port);
}
