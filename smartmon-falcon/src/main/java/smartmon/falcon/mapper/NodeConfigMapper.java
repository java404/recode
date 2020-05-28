package smartmon.falcon.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import smartmon.falcon.entity.NodeConfigEntity;

@Mapper
public interface NodeConfigMapper {
  @Select("SELECT * FROM node_config")
  // @Results(id = "NodeConfigEntity", value = {
  // @Result(column = "createtime", property = "createTime", jdbcType =
  // JdbcType.TIMESTAMP) })
  List<NodeConfigEntity> findAll();

  @Select("SELECT * FROM node_config WHERE hostname = #{hostname} AND name = #{name}")
  List<NodeConfigEntity> findByHostnameAndName(@Param("hostname") String hostname,
      @Param("name") String name);

  @Insert("INSERT INTO node_config(uuid, data, hostname, name) "
      + "VALUES(#{uuid}, #{data}, #{hostname}, #{name}) ON DUPLICATE KEY UPDATE data=#{data}")
  void saveOrUpdate(@Param("uuid") String uuid, @Param("data") String data,
      @Param("hostname") String hostname, @Param("name") String name);

  @Insert("INSERT INTO node_config(uuid, data, hostname, name) "
      + "SELECT #{uuid} as uuid, #{data} as data, #{hostname} as hostname, #{name} as name from dual WHERE NOT EXISTS (SELECT * FROM node_config WHERE hostname = #{hostname} and name = #{name}) LIMIT 1")
  void save(@Param("uuid") String uuid, @Param("data") String data,
      @Param("hostname") String hostname, @Param("name") String name);

  @Update("UPDATE node_config set data= #{data} where hostname = #{hostname} and name = #{name}")
  void update(@Param("data") String data, @Param("hostname") String hostname,
      @Param("name") String name);

  @Delete("delete from node_config where id= #{id}")
  void deleteById(@Param("id") Integer id);
}
