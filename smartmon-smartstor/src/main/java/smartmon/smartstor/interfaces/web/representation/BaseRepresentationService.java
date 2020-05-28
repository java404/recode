package smartmon.smartstor.interfaces.web.representation;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.StorageHost;

@Slf4j
@Service
public class BaseRepresentationService {
  @Autowired
  protected StorageHostRepository storageHostRepository;

  public List<StorageHost> getAllHosts() {
    try {
      return storageHostRepository.getAll();
    } catch (Exception e) {
      log.error("Get storage hosts info failed", e);
    }
    return Lists.newArrayList();
  }

  public List<StorageHost> getAllIosServiceHosts() {
    List<StorageHost> allHosts = getAllHosts();
    return allHosts.stream()
      .filter(StorageHost::isIos)
      .filter(host -> isIp(host.getListenIp()))
      .collect(Collectors.toList());
  }

  private boolean isIp(String ip) {
    if (StringUtils.isBlank(ip)) {
      return false;
    }
    String reg = "^$|^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)($|(?!\\.$)\\.)){4}$";
    return Pattern.matches(reg, ip);
  }
}
