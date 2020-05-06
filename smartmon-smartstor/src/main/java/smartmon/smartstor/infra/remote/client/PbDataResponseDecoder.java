package smartmon.smartstor.infra.remote.client;

import com.fasterxml.jackson.databind.JsonNode;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import smartmon.smartstor.infra.remote.exception.PbDataBadResponseException;
import smartmon.smartstor.infra.remote.exception.PbDataInvalidResponseException;
import smartmon.smartstor.infra.remote.exception.PbDataNoBodyException;
import smartmon.smartstor.infra.remote.exception.PbDataNoResponseException;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.utilities.misc.JsonConverter;
import smartmon.utilities.remote.RemoteApiCoder;

@Slf4j
public class PbDataResponseDecoder implements Decoder {
  private final JacksonDecoder decoder;

  public PbDataResponseDecoder() {
    this.decoder = RemoteApiCoder.makeDecoder();
  }

  @Override
  public Object decode(Response response, Type type) throws FeignException, IOException {
    final JsonNode result = (JsonNode) decoder.decode(response, JsonNode.class);
    if (result == null) {
      log.error("PbData response decoder error ({})", type.getTypeName());
      throw new PbDataNoResponseException();
    }
    final PbDataResponseCode rc = JsonConverter.treeToValue(result.get("rc"), PbDataResponseCode.class);
    if (rc == null) {
      log.error("PbData response decoder error ({}), no rc", type.getTypeName());
      throw new PbDataInvalidResponseException();
    }

    if (type.equals(PbDataResponseCode.class)) {
      return rc;
    }

    if (!rc.isOk()) {
      log.error("PbData response decoder error ({}), rc {}", type.getTypeName(), rc.toString());
      throw new PbDataBadResponseException(rc);
    }

    final JsonNode body = result.get("body");
    if (body == null || body.isEmpty()) {
      throw new PbDataNoBodyException();
    }
    try {
      final JsonNode firstFiled = body.fields().next().getValue();
      return JsonConverter.treeToValue(firstFiled, (Class<?>) type);
    } catch (Exception error) {
      log.error("PbData response parse error:", error);
      throw new PbDataInvalidResponseException();
    }
  }
}
