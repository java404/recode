package smartmon.core.racks;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import smartmon.core.racks.model.Idc;

@Mapper
public interface IdcMapper {
  @Select("SELECT * FROM idc")
  List<Idc> findAll();

  @Select("SELECT * FROM idc WHERE name = #{name}")
  Idc findByName(@Param("name") String name);

  @Select("SELECT * FROM idc WHERE id = #{id}")
  Idc findById(@Param("id") String id);

  @Insert("INSERT INTO idc(id, name) VALUES(#{id}, #{name})")
  void save(Idc idc);

  @Update("UPDATE idc SET name = #{newName} WHERE name = #{oldName}")
  void renameIdc(@Param("oldName") String oldName, @Param("newName") String newName);

  @Select("SELECT COUNT(*) FROM idc WHERE name = #{name}")
  Integer getCountByName(@Param("name") String name);

  @Select("SELECT DISTINCT id FROM idc")
  List<String> getIdcIds();
}
