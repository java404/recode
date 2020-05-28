package smartmon.falcon.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import smartmon.falcon.monitor.types.FalconConfig;

@Mapper
public interface FalconConfigMapper {
  @Select("SELECT * FROM falcon_config")
  List<FalconConfig> findAll();

  @Select("SELECT * FROM falcon_config WHERE name LIKE #{key}")
  List<FalconConfig> findItems(String key);

  @Select("SELECT * FROM falcon_config WHERE name = #{name}")
  FalconConfig findItem(String name);

  @Insert("INSERT INTO falcon_config (name, value) "
    + "VALUES (#{name}, #{value}) ON DUPLICATE KEY UPDATE value = #{value}")
  void put(FalconConfig metadata);
}
