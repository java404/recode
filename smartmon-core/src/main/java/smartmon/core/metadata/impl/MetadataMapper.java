package smartmon.core.metadata.impl;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import smartmon.core.metadata.types.Metadata;

@Mapper
public interface MetadataMapper {
  @Select("SELECT * FROM metadata")
  List<Metadata> findAll();

  @Select("SELECT * FROM metadata WHERE name LIKE #{key}")
  List<Metadata> findItems(String key);

  @Select("SELECT * FROM metadata WHERE name = #{name}")
  Metadata findItem(String name);

  @Insert("INSERT INTO metadata (name, value) "
    + "VALUES (#{name}, #{value}) ON DUPLICATE KEY UPDATE value = #{value}")
  void put(Metadata metadata);
}
