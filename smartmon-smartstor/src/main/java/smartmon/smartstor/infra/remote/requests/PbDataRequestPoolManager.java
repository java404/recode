package smartmon.smartstor.infra.remote.requests;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolCacheConfigParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolCreateParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolDirtyConfigParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolInfos;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolSizeConfigParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolSynClevelConfgParam;
import smartmon.smartstor.infra.remote.types.pool.PbDataPoolSkipConfigParam;

public interface PbDataRequestPoolManager {
  @RequestLine("GET /instances/pools")
  PbDataPoolInfos listPools();

  @RequestLine("POST /instances/pools")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode poolCreate(@RequestBody PbDataPoolCreateParam poolCreateParam);

  @RequestLine("DELETE /instances/pools/{pool_name}")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode poolDel(@Param("pool_name") String poolName);

  @RequestLine("PATCH /instances/pools/{pool_name}/size")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode poolSizeUpdata(@Param("pool_name") String poolName,
                                    @RequestBody PbDataPoolSizeConfigParam sizeConfigParam);

  @RequestLine("PATCH /instances/pools/{pool_name}/dirtythresh")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode poolConfigDirtyThresh(@Param("pool_name") String poolName,
                                           @RequestBody PbDataPoolDirtyConfigParam dirtyConfigParam);

  @RequestLine("PATCH /instances/pools/{pool_name}/synclevel")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode poolConfigSynClevel(@Param("pool_name") String poolName,
                                         @RequestBody PbDataPoolSynClevelConfgParam synClevelConfgParam);

  @RequestLine("PATCH /instances/pools/{pool_name}/cachemodel")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode poolConfigCacheModel(@Param("pool_name") String poolName,
                                          @RequestBody PbDataPoolCacheConfigParam cacheConfigParam);

  @RequestLine("PATCH /instances/pools/{pool_name}/skip")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode poolConfigSkip(@Param("pool_name") String poolName,
                                    @RequestBody PbDataPoolSkipConfigParam skipConfigParam);
}
