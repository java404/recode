package smartmon.core.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import smartmon.core.agent.service.AgentStateService;
import smartmon.core.hostinfo.HostInfoService;
import smartmon.core.ipmi.IpmiService;

@Slf4j
@EnableScheduling
@Configuration
public class SchedulingConfig implements SchedulingConfigurer {
  private ScheduledExecutorService scheduledExecutorService;
  private ExecutorService executorService;
  @Autowired
  private DataSyncConfig dataSyncConfig;
  @Autowired
  private AgentStateService agentStateService;
  @Autowired
  private HostInfoService hostInfoService;
  @Autowired
  private IpmiService ipmiService;

  @PostConstruct
  private void init() {
    scheduledExecutorService = Executors.newScheduledThreadPool(10);
    executorService = new ThreadPoolExecutor(50, Integer.MAX_VALUE,
      60L, TimeUnit.SECONDS,
      new SynchronousQueue<>());
  }

  @PreDestroy
  private void destroy() {
    scheduledExecutorService.shutdown();
    executorService.shutdown();
  }

  @Override
  public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(scheduledExecutorService);
    addTriggerTask(taskRegistrar, this::syncAgentState, () -> dataSyncConfig.getAgentStateCron());
    addTriggerTask(taskRegistrar, this::syncHostInfo, () -> dataSyncConfig.getHostInfoCron());
    addTriggerTask(taskRegistrar, this::syncIpmiInfo, () -> dataSyncConfig.getIpmiInfoCron());
  }

  private void addTriggerTask(ScheduledTaskRegistrar taskRegistrar, Runnable task, Supplier<String> cronSupplier) {
    Trigger trigger = (triggerContext) -> {
      CronTrigger cronTrigger = new CronTrigger(cronSupplier.get());
      return cronTrigger.nextExecutionTime(triggerContext);
    };
    taskRegistrar.addTriggerTask(task, trigger);
  }

  private void syncAgentState() {
    try {
      log.info(">>>>>>>>> SchedulingConfig.syncAgentState");
      agentStateService.syncAgentState();
    } catch (Exception err) {
      log.error(">>>>>>>>> SchedulingConfig.syncAgentState error", err);
    }
  }

  private void syncHostInfo() {
    try {
      log.info(">>>>>>>>> SchedulingConfig.syncHostInfo");
      hostInfoService.syncHostInfo(executorService);
    } catch (Exception err) {
      log.error(">>>>>>>>> SchedulingConfig.syncHostInfo error", err);
    }
  }

  private void syncIpmiInfo() {
    try {
      log.info(">>>>>>>>> SchedulingConfig.syncIpmiInfo");
      ipmiService.syncIpmiInfo(executorService);
    } catch (Exception err) {
      log.error(">>>>>>>>> SchedulingConfig.syncIpmiInfo error", err);
    }
  }
}
