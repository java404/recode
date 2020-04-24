package smartmon.utilities.misc;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class BeanConverter {
  public static <T, R> R copy(T source, Class<R> targetClz) {
    if (source == null) {
      return null;
    }
    R result = null;
    try {
      result = targetClz.newInstance();
      BeanUtils.copyProperties(source, result);
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
        BeanUtils.copyProperties(source, result);
        results.add(result);
      }
    } catch (Exception e) {
      log.warn("Beans convert exception:", e);
    }
    return results;
  }
}
