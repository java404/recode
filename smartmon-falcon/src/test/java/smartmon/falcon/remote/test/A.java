package smartmon.falcon.remote.test;

import lombok.Data;

import java.util.List;

@Data
public class A {
  private String name;
  private String password;
  private C id;
  private List<String> ids;
}
