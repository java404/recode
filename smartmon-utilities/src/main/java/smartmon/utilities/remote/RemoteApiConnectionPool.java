package smartmon.utilities.remote;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

@Slf4j
public class RemoteApiConnectionPool {
  private static final int DEFAULT_SO_TIMEOUT = 30;
  private static final int DEFAULT_POOL_MAX_PRE_ROUTE = 15;
  private static final int DEFAULT_POOL_MAX_TOTAL = 50;
  private final PoolingHttpClientConnectionManager pool;

  private static class Holder {
    static final RemoteApiConnectionPool INSTANCE = new RemoteApiConnectionPool();
  }

  public RemoteApiConnectionPool() {
    this(DEFAULT_POOL_MAX_PRE_ROUTE, DEFAULT_POOL_MAX_TOTAL);
  }

  public RemoteApiConnectionPool(int maxPreRoute, int maxTotal) {
    final Registry<ConnectionSocketFactory> registry = makeConnectionSocketFactory();
    pool = registry == null ? new PoolingHttpClientConnectionManager() :
      new PoolingHttpClientConnectionManager(registry);
    pool.setDefaultMaxPerRoute(maxPreRoute);
    pool.setMaxTotal(maxTotal);
    pool.setDefaultSocketConfig(makeDefaultSoConfig());
  }

  private SocketConfig makeDefaultSoConfig() {
    return SocketConfig.copy(SocketConfig.DEFAULT)
      .setSoKeepAlive(true)
      .setSoTimeout((int) TimeUnit.SECONDS.toMillis(DEFAULT_SO_TIMEOUT))
      .build();
  }

  private Registry<ConnectionSocketFactory> makeConnectionSocketFactory() {
    try {
      final TrustStrategy accepting = (cert, type) -> true;
      final SSLContext context = new SSLContextBuilder()
        .loadTrustMaterial(null, accepting).build();
      final SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(context,
        NoopHostnameVerifier.INSTANCE);
      return RegistryBuilder.<ConnectionSocketFactory>create()
        .register("https", ssf).register("http", new PlainConnectionSocketFactory())
        .build();
    } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException error) {
      log.error("Cannot load ssl context", error);
      return null;
    }
  }

  public PoolingHttpClientConnectionManager getPool() {
    return pool;
  }

  public static RemoteApiConnectionPool getInstance() {
    return Holder.INSTANCE;
  }
}
