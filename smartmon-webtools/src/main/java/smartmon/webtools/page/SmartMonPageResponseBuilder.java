package smartmon.webtools.page;

import com.google.common.base.Strings;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import smartmon.utilities.general.SmartMonResponse;

@Slf4j
public class SmartMonPageResponseBuilder<E> {
  private final List<E> items;
  private final ServerHttpRequest serverRequest;
  private final String defaultSortProp;

  public SmartMonPageResponseBuilder(List<E> items,
                                     ServerHttpRequest serverRequest,
                                     String defaultSortProp) {
    this.items = items;
    this.serverRequest = serverRequest;
    this.defaultSortProp = defaultSortProp;
  }

  public SmartMonResponse<Page<E>> build() {
    final Map<String, String> requestParam = parseQueryParameter();
    log.debug("Items {}", items.size());
    log.debug("page-no {}", Strings.nullToEmpty(requestParam.get("page-no")));
    final PageParams pageParams = PageParams.make(requestParam, defaultSortProp);
    final PageCollection<E> pageCollection = new PageCollection<>(items, pageParams);
    return new SmartMonResponse<>(pageCollection.makePage());
  }

  private Map<String, String> parseQueryParameter() {
    return MapUtils.emptyIfNull(serverRequest.getQueryParams().toSingleValueMap());
  }
}
