package smartmon.examples.data;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestExampleMapper {
  @Select("SELECT * FROM test_sample")
  List<ExampleEntry> findAll();

  @Insert("INSERT INTO test_sample(name, value) VALUES(#{name}, #{value})")
  void add(ExampleEntry item);
}
