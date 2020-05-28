package smartmon.core.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import smartmon.core.store.SmartMonStoreFile;

public interface MetaFileMapper {
  @Insert("INSERT INTO metafile "
    + " ( original_filename, local_filename, local_filesize, timestamp, type, status ) VALUES "
    + " (#{originalFilename}, #{localFilename}, #{localFileSize}, "
    + "  #{timestamp}, #{type}, #{status}) ")
  @Options(useGeneratedKeys = true, keyProperty = "fileId", keyColumn = "file_id")
  void put(SmartMonStoreFile storeFile);

  @Update("UPDATE metafile SET status = #{status}, error_message = #{errorMessage} WHERE file_id = #{fileId}")
  void updateStatus(SmartMonStoreFile storeFile);

  @Select("SELECT * FROM metafile")
  @Results({
    @Result(property = "fileId", column = "file_id"),
    @Result(property = "originalFilename", column = "original_filename"),
    @Result(property = "localFilename", column = "local_filename"),
    @Result(property = "localFileSize", column = "local_filesize"),
    @Result(property = "timestamp", column = "timestamp"),
    @Result(property = "type", column = "type"),
    @Result(property = "status", column = "status"),
    @Result(property = "errorMessage", column = "error_message"),
  })
  List<SmartMonStoreFile> findAll();

  @Select("SELECT * FROM metafile WHERE file_id = #{fileId}")
  @Results({
    @Result(property = "fileId", column = "file_id"),
    @Result(property = "originalFilename", column = "original_filename"),
    @Result(property = "localFilename", column = "local_filename"),
    @Result(property = "localFileSize", column = "local_filesize"),
    @Result(property = "timestamp", column = "timestamp"),
    @Result(property = "type", column = "type"),
    @Result(property = "status", column = "status"),
    @Result(property = "errorMessage", column = "error_message"),
  })
  SmartMonStoreFile findById(long fileId);
}
