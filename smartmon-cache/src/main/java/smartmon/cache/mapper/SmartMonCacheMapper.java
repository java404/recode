package smartmon.cache.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import smartmon.cache.impl.CacheItem;

@Mapper
public interface SmartMonCacheMapper {
  @Insert({"INSERT INTO cache (cache_key, timestamp, data) "
    + " VALUES (#{key}, #{timestamp}, #{data}) "
    + " ON DUPLICATE KEY UPDATE timestamp = #{timestamp}, data = #{data} ", })
  void put(CacheItem item);

  @Insert({"INSERT INTO cache (cache_key, errors) "
    + " VALUES (#{key}, #{errors} ) "
    + " ON DUPLICATE KEY UPDATE errors = #{errors} ", })
  void putError(@Param("key") String key, @Param("errors") String errors);

  @Select({"SELECT * FROM cache WHERE cache_key = #{key} "})
  @Results({
    @Result(property = "timestamp", column = "timestamp"),
    @Result(property = "data", column = "data"),
    @Result(property = "errors", column = "errors"),
    @Result(property = "key", column = "cache_key")
  })
  CacheItem get(@Param("key") String key);
}
