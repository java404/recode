package smartmon.utilities.remote;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class RemoteApiCoder {
  private static class ObjectMapperHolder {
    static final ObjectMapper INSTANCE = new ObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .configure(SerializationFeature.INDENT_OUTPUT, true)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static ObjectMapper getMapper() {
    return ObjectMapperHolder.INSTANCE;
  }

  public static JacksonEncoder makeEncoder() {
    return new JacksonEncoder(ObjectMapperHolder.INSTANCE);
  }

  public static JacksonDecoder makeDecoder() {
    return new JacksonDecoder(ObjectMapperHolder.INSTANCE);
  }
}

