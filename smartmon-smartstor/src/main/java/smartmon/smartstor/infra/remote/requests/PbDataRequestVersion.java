package smartmon.smartstor.infra.remote.requests;

import feign.RequestLine;
import smartmon.smartstor.infra.remote.types.PbDataApiVersion;

public interface PbDataRequestVersion {
  @RequestLine("GET /api/version")
  PbDataApiVersion getVersion();
}
