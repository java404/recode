package smartmon.utilities.misc;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class UrlBuilder {
  private final String protocol;
  private final String address;
  private final int port;
  private String path;

  public UrlBuilder(String protocol, String address, int port) {
    this.protocol = parseProtocol(protocol);
    this.address = address;
    this.port = port;
  }

  private String parseProtocol(String protocol) {
    final List<String> items = Splitter.on(":").trimResults().splitToList(protocol);
    return CollectionUtils.isEmpty(items) ? protocol : items.get(0);
  }

  public UrlBuilder withPath(String path) {
    this.path = path;
    return this;
  }

  /** Make url with the IP address type.
   * example:
   * http://ipv4:port/path
   * http://[ipv6]:port/path
   */
  public String build() {
    final StringBuilder sb = new StringBuilder();
    return sb.append(this.protocol).append("://")
      .append(makeHost()).append("/")
      .append(parsePath(path)).toString();
  }

  /**
   * Make url with the IP address type.
   * example:
   * ipv4:port
   * [ipv6]:port
   */
  public String makeHost() {
    final StringBuilder sb = new StringBuilder();
    if (isIpV6Address(this.address)) {
      sb.append("[").append(this.address).append("]");
    } else {
      sb.append(this.address);
    }
    return sb.append(":").append(String.format("%d", this.port)).toString();
  }

  /** Check IP Address type. */
  private boolean isIpV6Address(String address) {
    try {
      final InetAddress inetAddress = InetAddress.getByName(address);
      return inetAddress instanceof Inet6Address;
    } catch (UnknownHostException e) {
      return false;
    }
  }

  /** Example: Path => "/abc/123" = "abc/123"; "abc/123" = "abc/123". */
  private String parsePath(String path) {
    if (Strings.isNullOrEmpty(path)) {
      return Strings.nullToEmpty(path);
    }
    final String trimPath = path.trim();
    if (trimPath.startsWith("/")) {
      return trimPath.substring(1);
    }
    return trimPath;
  }
}
