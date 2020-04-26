package smartmon.smartstor.infra.remote.requests;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.disk.PbDataDisk;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfos;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskInfosV30;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskAddParam;
import smartmon.smartstor.infra.remote.types.disk.PbDataDiskV30;

public interface PbDataRequestDiskManager {
  @RequestLine("GET /instances/disks")
  PbDataDiskInfos listDisks();

  @RequestLine("GET /instances/disks")
  PbDataDiskInfosV30 listDisksV30();

  @RequestLine("GET /instances/disks/{disk_name}")
  @Headers({"Content-Type: application/json"})
  PbDataDisk getDiskInfo(@Param("disk_name") String diskName);

  @RequestLine("GET /instances/disks/{disk_name}")
  @Headers({"Content-Type: application/json"})
  PbDataDiskV30 getDiskInfoV30(@Param("disk_name") String diskName);

  @RequestLine("POST /instances/disks")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode diskAdd(@RequestBody PbDataDiskAddParam param);

  @RequestLine("DELETE /instances/disks/{disk_name}")
  PbDataResponseCode diskDel(@Param("disk_name") String diskName);

  @RequestLine("PATCH /instances/disks/{ces_addr}/led/onstate")
  PbDataResponseCode diskRaidLedOnState(@Param("ces_addr") String cesAddr);

  @RequestLine("PATCH /instances/disks/{ces_addr}/led/offstate")
  PbDataResponseCode diskRaidLedOffState(@Param("ces_addr") String cesAddr);


}
