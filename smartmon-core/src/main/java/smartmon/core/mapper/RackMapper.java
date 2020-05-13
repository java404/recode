package smartmon.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import smartmon.core.racks.model.RackAllocation;

@Mapper
public interface RackMapper {
  @Select("SELECT * FROM rack_allocation")
  List<RackAllocation> findAll();

  @Select("SELECT * FROM rack_allocation WHERE idc_id = #{idcId} AND rack_name = #{rackName}")
  List<RackAllocation> findByIdcIdAndRackName(@Param("idcId") String idcId, @Param("rackName") String rackName);

  @Insert("INSERT INTO rack_allocation(idc_id, rack_name, rack_index, host_uuid, size) "
    + "VALUES(#{idcId}, #{rackName}, #{rackIndex}, #{hostUuid}, #{size})")
  void save(RackAllocation rackAllocation);

  @Delete("DELETE FROM rack_allocation WHERE idc_id = #{idcId} AND rack_name = #{rackName} "
    + "AND rack_index = #{rackIndex}")
  void delete(@Param("idcId") String idcId, @Param("rackName") String rackName, @Param("rackIndex") Integer rackIndex);

  @Select("SELECT COUNT(*) FROM rack_allocation WHERE idc_id = #{idcId} AND rack_name = #{rackName}")
  Integer getRecordCountByIdcAndRackName(@Param("idcId") String idcId, @Param("rackName") String rackName);

  @Update("UPDATE rack_allocation SET rack_name = #{newRackName} WHERE idc_id = #{idcId} "
    + "AND rack_name = #{oldRackName}")
  void renameRack(@Param("idcId") String idcId, @Param("oldRackName") String oldRackName,
                  @Param("newRackName") String newRackName);

  @Select("SELECT DISTINCT rack_name FROM rack_allocation WHERE idc_id = #{idcId}")
  List<String> getRackNamesByIdcId(@Param("idcId") String idcId);

  @Select("SELECT * FROM rack_allocation WHERE host_uuid = #{hostUuid}")
  List<RackAllocation> getByHostUuid(@Param("hostUuid") String hostUuid);

  @Delete("DELETE FROM rack_allocation WHERE host_uuid = #{hostUuid}")
  void deleteByHostUuid(@Param("hostUuid") String hostUuid);
}
