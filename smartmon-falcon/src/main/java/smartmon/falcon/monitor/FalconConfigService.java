package smartmon.falcon.monitor;

import java.util.List;

import org.springframework.http.server.reactive.ServerHttpRequest;
import smartmon.falcon.monitor.types.CallBackParam;
import smartmon.falcon.monitor.types.EmailConfigInfo;
import smartmon.falcon.monitor.types.FalconConfig;

public interface FalconConfigService {
  List<FalconConfig> findItems(String key);

  FalconConfig findItem(String name);

  void put(List<FalconConfig> items);

  CallBackParam handleCallBackParam(ServerHttpRequest request);

  void snmpTrap();

  void sendEmail(EmailConfigInfo email, CallBackParam callBackParam);
}
