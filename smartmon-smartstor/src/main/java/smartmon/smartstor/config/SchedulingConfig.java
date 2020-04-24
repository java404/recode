package smartmon.smartstor.config;

import java.util.concurrent.Executors;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import smartmon.smartstor.domain.gateway.DataSyncService;

@Slf4j
@EnableScheduling
@Configuration
public class SchedulingConfig implements SchedulingConfigurer {
  private static final int CORE_POOL_SIZE = 10;

  @Autowired
  private DataSyncConfig dataSyncConfig;
  @Autowired
  private DataSyncService dataSyncService;

  @Override
  public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(Executors.newScheduledThreadPool(CORE_POOL_SIZE));
    addTriggerTask(taskRegistrar, this::syncStorage, dataSyncConfig.getSmartstor()::getCron);
  }

  private void addTriggerTask(ScheduledTaskRegistrar taskRegistrar, Runnable task, Supplier<String> cronSupplier) {
    Trigger trigger = (triggerContext) -> {
      CronTrigger cronTrigger = new CronTrigger(cronSupplier.get());
      return cronTrigger.nextExecutionTime(triggerContext);
    };
    taskRegistrar.addTriggerTask(task, trigger);
  }

  private void syncStorage() {
    if (dataSyncConfig.getSync() && dataSyncConfig.getSmartstor().getSync()) {
      log.info(">>>>>>>>> SchedulingConfig.syncStorage");
      dataSyncService.syncAll();
    }
  }
}
