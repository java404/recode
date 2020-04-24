package smartmon.webtools.page;

import com.google.common.base.Strings;
import java.util.Collection;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import smartmon.utilities.general.SmartMonResponse;

@Slf4j
public class SmartMonPageResponseBuilder<E> {
  private final Collection<E> items;
  private final Map<String, String> requestParam;

  public SmartMonPageResponseBuilder(Collection<E> items, Map<String, String> requestParam) {
    this.items = CollectionUtils.emptyIfNull(items);
    this.requestParam = requestParam;
  }

  public SmartMonResponse<Page<E>> build() {
    log.debug("Items {}", items.size());
    log.debug("page-no {}", Strings.nullToEmpty(requestParam.get("page-no")));
    // TODO Convert result to cache
    return null;
  }
}
