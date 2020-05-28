package smartmon.core.ipmi;

import com.veraxsystems.vxipmi.api.async.ConnectionHandle;
import com.veraxsystems.vxipmi.api.sync.IpmiConnector;
import com.veraxsystems.vxipmi.coding.commands.IpmiVersion;
import com.veraxsystems.vxipmi.coding.commands.PrivilegeLevel;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatus;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatusResponseData;
import com.veraxsystems.vxipmi.coding.protocol.AuthenticationType;
import com.veraxsystems.vxipmi.coding.security.CipherSuite;

import java.net.InetAddress;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class IpmiManager {
  private static final int DEFAULT_IPMI_PORT = 0;

  public static PowerStateEnum getPowerState(String address, String username, String password) {
    if (StringUtils.isEmpty(address) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      throw new IllegalArgumentException("ipmi address and auth info required");
    }
    IpmiConnector connector = null;
    ConnectionHandle handle = null;
    try {
      connector = new IpmiConnector(DEFAULT_IPMI_PORT);
      handle = connector.createConnection(InetAddress.getByName(address));
      CipherSuite cs = connector.getAvailableCipherSuites(handle).get(3);
      connector.getChannelAuthenticationCapabilities(handle, cs, PrivilegeLevel.Administrator);
      connector.openSession(handle, username, password, null);
      GetChassisStatusResponseData rd = (GetChassisStatusResponseData) connector.sendMessage(
        handle, new GetChassisStatus(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus));
      return rd.isPowerOn() ? PowerStateEnum.ON : PowerStateEnum.OFF;
    } catch (Exception err) {
      log.error(String.format("get ipmi power state error, host[%s], user[%s]", address, username), err);
      return PowerStateEnum.UNKNOWN;
    } finally {
      releaseConnector(connector, handle);
    }
  }

  private static void releaseConnector(IpmiConnector connector, ConnectionHandle handle) {
    if (connector == null) {
      return;
    }
    if (handle != null) {
      try {
        connector.closeSession(handle);
      } catch (Exception ignored) {
      }
    }
    connector.tearDown();
  }
}
