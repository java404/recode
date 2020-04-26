package smartmon.vhe.config;

import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import java.io.IOException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.JsonConverter;
import smartmon.vhe.exception.FeignClientException;

@Configuration
@Slf4j
public class FeignConfig {
  @Bean
  public Decoder decoder() {
    return new JacksonDecoder();
  }

  @Bean
  public Encoder encoder() {
    return new JacksonEncoder();
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    return new CustomErrorDecoder();
  }

  public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
      Exception ex = null;
      try {
        String res = Util.toString(response.body().asReader());
        SmartMonResponse errorMessage = JsonConverter.readValueQuietly(res, SmartMonResponse.class);
        if (errorMessage != null && errorMessage.getContent() != null) {
          ex =  new FeignClientException((String) errorMessage.getContent());
        } else {
          log.warn("Feign exec failed: {}", res);
          ex =  new FeignClientException("Internal server error");
        }
      } catch (IOException e) {
        log.warn(e.getMessage(), e);
      }
      return ex;
    }
  }

  @Data
  @NoArgsConstructor
  public static class ErrorMessage {
    private Integer status;
    private String error;
    private String message;
    private String timestamp;
  }
}
