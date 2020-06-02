package smartmon.falcon.monitor;

import java.util.List;
import org.springframework.http.server.reactive.ServerHttpRequest;
import smartmon.falcon.monitor.types.CallBackParam;
import smartmon.falcon.monitor.types.EmailConfigInfo;
import smartmon.falcon.monitor.types.FalconConfig;
import smartmon.falcon.monitor.types.SendEmailParam;
import smartmon.falcon.monitor.types.SnmpConfigInfo;

public interface FalconConfigService {
  List<FalconConfig> findItems(String key);

  FalconConfig findItem(String name);

  void put(List<FalconConfig> items);

  CallBackParam handleCallBackParam(ServerHttpRequest request);

  SendEmailParam callBackParamToSendEmailParam(CallBackParam callBackParam);

  SendEmailParam testSendEmailParam(String receiver);

  void sendEmail(EmailConfigInfo emailConfigInfo, SendEmailParam sendEmailParam);

  void sendEmailNoAuth(EmailConfigInfo emailConfigInfo, SendEmailParam sendEmailParam);

  void snmpV2CTrap(SnmpConfigInfo snmpConfigInfo, CallBackParam callBackParam);

  void snmpV3Trap(SnmpConfigInfo snmpConfigInfo, CallBackParam callBackParam);


}
