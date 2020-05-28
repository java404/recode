package smartmon.utilities.misc;

import com.google.common.base.Strings;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalNetworkInterface {
  @Getter
  private final Set<String> addresses = new HashSet<>();

  public LocalNetworkInterface() {
    try {
      final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        final NetworkInterface netIfs = interfaces.nextElement();
        final Enumeration<InetAddress> inetAddresses = netIfs.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
          final InetAddress inetAddress = inetAddresses.nextElement();
          final String address = inetAddress.getHostAddress();
          if (Strings.isNullOrEmpty(address)) {
            continue;
          }
          addresses.add(address);
        }
      }
    } catch (SocketException error) {
      log.error("Cannot get the list of host addresses");
    }
  }

  private synchronized boolean findInLocalAddress(String ip) {
    return addresses.stream().anyMatch(item -> item.equals(ip));
  }

  public boolean isLocalIp(String ip) {
    return findInLocalAddress(Strings.nullToEmpty(ip));
  }

  private static class Holder {
    static final LocalNetworkInterface INSTANCE = new LocalNetworkInterface();
  }

  public static LocalNetworkInterface getInstance() {
    return Holder.INSTANCE;
  }
}
