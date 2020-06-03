package smartmon.falcon.remote.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class B {

  private String name;
  private Integer password;
  private Integer id;
  private String ids;

  public String getPassword() {
    return String.valueOf(password);
  }

  public C getId() {
    return C.getByCode(this.id);
  }

  public List<String> getIds() {
    return Arrays.asList(ids.split(""));
  }
}
