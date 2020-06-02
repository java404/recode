package smartmon.webtools.misc;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Slf4j
public class SmartMonRequestAddress {
  public static String parseRemoteAddress(ServerHttpRequest request) {
    try {
      final InetSocketAddress socketAddress = request.getRemoteAddress();
      assert socketAddress != null;
      final InetAddress address = socketAddress.getAddress();
      return address.getHostAddress();
    } catch (Exception err) {
      log.warn("cannot parse remote address: ", err);
      return "Unknown";
    }
  }
}
