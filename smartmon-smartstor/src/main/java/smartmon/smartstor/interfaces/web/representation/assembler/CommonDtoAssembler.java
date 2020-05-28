package smartmon.smartstor.interfaces.web.representation.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.utilities.misc.BeanConverter;

public class CommonDtoAssembler {
  public static <T, R> CachedData<R> toDtos(CachedData<T> cachedData,
                                            Class<R> targetClz) {
    if (cachedData == null || CollectionUtils.isEmpty(cachedData.getData())) {
      return null;
    }
    List<R> dtos = cachedData.getData()
      .stream()
      .map(t -> toDto(t, targetClz)).collect(Collectors.toList());
    return new CachedData<>(dtos, cachedData.isExpired(), cachedData.getError());
  }

  private static <T, R> R toDto(T t, Class<R> targetClz) {
    return BeanConverter.copy(t, targetClz);
  }
}
