package smartmon.core.hosts.mapper;

import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;
import smartmon.core.hosts.SmartMonHost;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface SmartMonHostMapper extends Mapper<SmartMonHost> {
  @Update("UPDATE smartmon_host set monitor_net_interfaces = #{monitorNetInterfaces} WHERE host_uuid = #{hostUuid}")
  void updateMonitorNetInterfacesById(
    @Param("hostUuid") String hostUuid, @Param("monitorNetInterfaces") String monitorNetInterfaces);
}
