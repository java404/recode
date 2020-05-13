package smartmon.webtools.misc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SmartMonContextProvider implements ApplicationContextAware {
  private static ApplicationContext context;

  private SmartMonContextProvider() {
    // NOP
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }

  public static ApplicationContext getContext() {
    return context;
  }
}
