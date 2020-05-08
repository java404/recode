package smartmon.core.repo;

import org.junit.Ignore;
import org.junit.Test;
import smartmon.utilities.misc.TargetHost;

public class RepoServiceImplTest {
  @Test
  @Ignore
  public void configRepoClient() {
    RepoServiceImpl.RepoConfig repoConfig = RepoServiceImpl.RepoConfig.builder()
      .repoId("SMARTMON-REPO")
      .repoFilename("smartmon.repo")
      .repoBaseUrl("http://172.24.8.121:8000/repo/")
      .build();
    RepoService repoService = new RepoServiceImpl(repoConfig);
    TargetHost targetHost = TargetHost.builder("172.24.8.142", 22)
      .username("root").password("root123").build();
    repoService.configRepoClient(targetHost);
  }
}
