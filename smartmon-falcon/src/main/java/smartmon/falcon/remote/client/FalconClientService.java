package smartmon.falcon.remote.client;

public interface FalconClientService {
  FalconClient getClient(String address, int port);
}
