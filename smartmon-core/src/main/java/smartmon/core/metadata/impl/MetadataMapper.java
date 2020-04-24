package smartmon.core.metadata.impl;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import smartmon.core.metadata.types.Metadata;

@Mapper
public interface MetadataMapper {
  @Select("SELECT * FROM metadata")
  List<Metadata> findAll();
}
