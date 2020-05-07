package smartmon.falcon.remote.client;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import lombok.extern.slf4j.Slf4j;
import smartmon.falcon.remote.exception.FalconBadResponseException;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.utilities.remote.RemoteApiCoder;
import java.io.IOException;
import java.lang.reflect.Type;

@Slf4j
public class FalconResponseDecode implements Decoder {
  private JacksonDecoder decoder;

  public FalconResponseDecode() {
    this.decoder = RemoteApiCoder.makeDecoder();
  }

  @Override
  public Object decode(Response response, Type type) throws FeignException, IOException {
    if (type.equals(FalconResponseData.class)) {
      FalconResponseData responseData = (FalconResponseData) decoder.decode(response, FalconResponseData.class);
      if (responseData.getCode() == 0) {
        return responseData;
      } else {
        throw new FalconBadResponseException(responseData.getErrorMessage());
      }
    }
    return decoder.decode(response, type);
  }
}
