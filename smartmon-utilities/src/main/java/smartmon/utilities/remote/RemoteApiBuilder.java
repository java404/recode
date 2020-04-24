package smartmon.utilities.remote;

import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.misc.UrlBuilder;

@Getter
@Slf4j
public class RemoteApiBuilder {
  private final String address;
  private final int port;

  private String apiPrefix;
  private boolean isHttps = false;
  private Client client;
  private Decoder decoder;
  private ErrorDecoder errorDecoder;

  public RemoteApiBuilder(TargetHost targetHost) {
    this(targetHost.getAddress(), targetHost.getPort());
  }

  public RemoteApiBuilder(String address, int port) {
    this.address = address;
    this.port = port;
  }

  public RemoteApiBuilder withApiPrefix(String apiPrefix) {
    this.apiPrefix = apiPrefix;
    return this;
  }

  public RemoteApiBuilder withHttps(boolean isHttps) {
    this.isHttps = isHttps;
    return this;
  }

  public RemoteApiBuilder withDecoder(Decoder decoder) {
    this.decoder = decoder;
    return this;
  }

  public RemoteApiBuilder withErrorDecoder(ErrorDecoder errorDecoder) {
    this.errorDecoder = errorDecoder;
    return this;
  }

  public RemoteApiBuilder withClient(Client client) {
    this.client = client;
    return this;
  }

  public <T> T build(Class<T> apiType) {
    final Feign.Builder builder = Feign.builder()
      .encoder(RemoteApiCoder.makeEncoder())
      .errorDecoder(Optional.ofNullable(errorDecoder).orElse(new ErrorDecoder.Default()))
      .decoder(Optional.ofNullable(decoder).orElse(RemoteApiCoder.makeDecoder()))
      .client(Optional.ofNullable(client).orElse(RemoteApiClientBuilder.buildDefault()));
    final String url = new UrlBuilder(isHttps ? "https" : "http", address, port)
      .withPath(apiPrefix).build();
    return builder.target(apiType, url);
  }
}
