package smartmon.utilities.misc;

import ch.qos.logback.core.pattern.ConverterUtil;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;

@Slf4j
public class BeanConverter {

  public static <T, R> R copy(T source, Class<R> targetClz) {
    if (source == null) {
      return null;
    }
    R result = null;
    try {
      result = targetClz.newInstance();
      BeanUtils.copyProperties(result, source);
    } catch (Exception e) {
      log.warn("Bean convert exception:", e);
    }
    return result;
  }

  public static <T, R> List<R> copy(List<T> sources, Class<R> targetClz) {
    if (sources == null) {
      return null;
    }
    List<R> results = new ArrayList<>(sources.size());
    try {
      for (T source : sources) {
        R result = targetClz.newInstance();
        BeanUtils.copyProperties(result, source);
        results.add(result);
      }
    } catch (Exception e) {
      log.warn("Beans convert exception:", e);
    }
    return results;
  }

  public static <T> Map<String, Object> beanToMap(T bean) {
    Map<String, Object> map = Maps.newHashMap();
    if (bean != null) {
      BeanMap beanMap = new BeanMap(bean);
      for (Object key : beanMap.keySet()) {
        Object value = beanMap.get(key);
        if (Objects.nonNull(value)) {
          map.put(Objects.toString(key), beanMap.get(key));
        }
      }
    }
    return map;
  }
}
