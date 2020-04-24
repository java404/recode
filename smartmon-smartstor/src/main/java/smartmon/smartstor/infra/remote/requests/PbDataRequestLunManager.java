package smartmon.smartstor.infra.remote.requests;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunConfigParam;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunCreateParam;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunInfo;
import smartmon.smartstor.infra.remote.types.lun.PbDataLunInfos;

public interface PbDataRequestLunManager {
  @RequestLine("GET /instances/luns")
  PbDataLunInfos listLuns();

  @RequestLine("GET /instances/luns/{lun_name}")
  PbDataLunInfo getLunInfo(@Param("lun_name") String name);

  @RequestLine("POST /instances/luns")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode lunCreate(@RequestBody PbDataLunCreateParam lunCreateParam);

  @RequestLine("DELETE /instances/luns/{lun_name}")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode lunDel(@PathVariable("lun_name") String lunName,
                            @RequestParam(value = "ignore_asmstatus", required = false) Boolean asmStatus,
                            @RequestParam(value = "ignore_vote", required = false) Boolean vote);

  @RequestLine("PATCH /instances/luns/{lun_name}/onlinestate")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode lunOnline(@Param("lun_name") String lunName);

  @RequestLine("PATCH /instances/luns/{lun_name}/offlinestate")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode lunOffline(@Param("lun_name") String lunName,
                                @RequestParam(value = "ignore_asmstatus", required = false) Boolean asmStatus);

  @RequestLine("PATCH /instances/luns/{lun_name}/activestate")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode lunActive(@Param("lun_name") String lunName);

  @RequestLine("PATCH /instances/luns/{lun_name}/inactivestate")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode lunInActive(@Param("lun_name") String lunName,
                                 @RequestParam(value = "ignore_asmstatus", required = false) Boolean asmStatus);

  @RequestLine("PATCH /instances/luns/{lun_name}")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode lunConfig(@RequestBody PbDataLunConfigParam lunConfigParam,
                               @Param("lun_name") String lunName);
}
