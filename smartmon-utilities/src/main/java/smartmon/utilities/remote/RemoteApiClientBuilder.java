package smartmon.utilities.remote;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import lombok.Getter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

@Getter
public class RemoteApiClientBuilder {
  private static final int CONNECTION_REQ_TIMEOUT = 500;
  private int connectTimeout = 20;
  private int requestTimeout = 30;
  private boolean skipConnectionPool = false;
  private RemoteApiConnectionPool connectionPool = RemoteApiConnectionPool.getInstance();

  public RemoteApiClientBuilder withConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  public RemoteApiClientBuilder withRequestTimeout(int requestTimeout) {
    this.requestTimeout = requestTimeout;
    return this;
  }

  public RemoteApiClientBuilder withConnectionPool(RemoteApiConnectionPool connectionPool) {
    this.connectionPool = connectionPool;
    return this;
  }

  public RemoteApiClientBuilder skipConnectionPool(boolean skipConnectionPool) {
    this.skipConnectionPool = skipConnectionPool;
    return this;
  }

  private RequestConfig makeRequestConfig() {
    final int configConnectTimeout = Math.max(5, connectTimeout) * 1_000;
    final int configRequestTimeout = Math.max(10, requestTimeout) * 1_000;
    return RequestConfig.custom()
      .setConnectTimeout(configConnectTimeout).setSocketTimeout(configRequestTimeout)
      .setConnectionRequestTimeout(CONNECTION_REQ_TIMEOUT).build();
  }

  private void updateConnectionPool(HttpClientBuilder builder) {
    if (skipConnectionPool || connectionPool == null) {
      return;
    }
    builder.setConnectionManager(connectionPool.getPool())
      .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
      .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
  }

  public Client build() {
    final HttpClientBuilder clientBuilder = HttpClients.custom()
      .setDefaultRequestConfig(makeRequestConfig());
    updateConnectionPool(clientBuilder);
    return new ApacheHttpClient(clientBuilder.build());
  }

  public static Client buildDefault() {
    return new RemoteApiClientBuilder().build();
  }
}
