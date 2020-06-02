package smartmon.examples.data;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = TestService.class, version = "1.0")
public class TestServiceImpl implements TestService {
  @Override
  public String getMessage() {
    return "RPC TEST get message";
  }
}
