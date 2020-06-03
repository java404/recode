package smartmon.utilities.misc;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class RetryUtils {
  public static <T> T retryForExceptionAndResult(
    Callable<T> callable, Predicate<T> predicate, int retryTimes, long sleepSeconds)
    throws ExecutionException, RetryException {
    final Retryer<T> retryer = RetryerBuilder
      .<T>newBuilder()
      .retryIfException()
      .retryIfResult(predicate::test)
      .withWaitStrategy(WaitStrategies.fixedWait(sleepSeconds, TimeUnit.SECONDS))
      .withStopStrategy(StopStrategies.stopAfterAttempt(retryTimes))
      .build();
    return retryer.call(callable);
  }
}
