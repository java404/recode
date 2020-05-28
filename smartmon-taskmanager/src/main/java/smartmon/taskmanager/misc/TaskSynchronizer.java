package smartmon.taskmanager.misc;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smartmon.taskmanager.record.TaskStatus;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.taskmanager.vo.TaskVo;

@Slf4j
@Service
public class TaskSynchronizer {
  private static final long SYNC_PERIOD_SECONDS = 5;
  private static final int EXCEPTION_TOLERATE_CONTINUOUSLY = 24;

  public static void awaitCompletion(Supplier<TaskGroupVo> taskResultSupplier, Consumer<TaskGroupVo> taskLogConsumer) {
    TaskGroupVo taskGroup;
    int exceptionCountContinuously = 0;
    while (true) {
      try {
        taskGroup = taskResultSupplier.get();
        exceptionCountContinuously = 0;
      } catch (Exception err) {
        exceptionCountContinuously++;
        if (exceptionCountContinuously >= EXCEPTION_TOLERATE_CONTINUOUSLY) {
          log.error("Too many errors occurred:", err);
          break;
        }
        continue;
      }
      taskLogConsumer.accept(taskGroup);
      if (TaskStatus.COMPLETED.equals(taskGroup.getStatus())) {
        if (Objects.equals(Boolean.FALSE, taskGroup.isSuccess())) {
          for (TaskVo taskVo : taskGroup.getTasks()) {
            if (Objects.equals(Boolean.FALSE, taskVo.isSuccess())) {
              throw new RuntimeException(taskVo.getError());
            }
          }
        }
        break;
      }
      boolean interrupted = awaitNextLoop();
      if (interrupted) {
        break;
      }
    }
  }

  private static boolean awaitNextLoop() {
    try {
      TimeUnit.SECONDS.sleep(SYNC_PERIOD_SECONDS);
    } catch (InterruptedException err) {
      Thread.currentThread().interrupt();
      log.warn("Wait for task complete interrupted:", err);
      return true;
    }
    return false;
  }
}
