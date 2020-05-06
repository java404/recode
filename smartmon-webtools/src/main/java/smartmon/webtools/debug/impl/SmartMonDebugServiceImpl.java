package smartmon.webtools.debug.impl;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Service;
import smartmon.webtools.debug.SmartMonDebugService;
import smartmon.webtools.misc.SmartMonContextProvider;

@Service
public class SmartMonDebugServiceImpl implements SmartMonDebugService {
  @Override
  public List<String> getProperties() {
    final Environment env = SmartMonContextProvider.getContext().getEnvironment();
    final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
    return StreamSupport.stream(sources.spliterator(), false)
      .filter(ps -> ps instanceof EnumerablePropertySource)
      .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
      .flatMap(Arrays::stream)
      .distinct()
      .filter(prop -> !StringUtils.containsIgnoreCase(prop, "password"))
      .map(prop -> {
        try {
          return String.format("%s = %s", prop, Strings.nullToEmpty(env.getProperty(prop)));
        } catch (Exception err) {
          return "";
        }
      })
      .collect(Collectors.toList());
  }
}
