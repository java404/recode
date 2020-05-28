package smartmon.falcon.monitor.types;

import lombok.Data;

@Data
public class SnmpConfigInfo {
  private String objectIdentifiers; // "1.3.6.1.4.1.48183"
  private String version; // "3"
  private String snmpTrapAddress; // "localhost"
  private String snmpTrapPort; // "162"
  private String engineId; // "0x8000000001020304"
  private String username; // "username"
  private String securityLevel;
  private String authType; // "SHA", "MD5"
  private String authPass; // "123456"
  private String privateType; // "AES", "DES"
  private String privatePass; // "123456"
  private Boolean enabled; // true/false
}
