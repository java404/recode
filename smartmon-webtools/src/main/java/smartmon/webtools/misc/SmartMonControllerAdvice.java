package smartmon.webtools.misc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import smartmon.utilities.general.SmartMonException;
import smartmon.utilities.general.SmartMonResponse;

@Slf4j
@RestControllerAdvice
public class SmartMonControllerAdvice {
  @ExceptionHandler(SmartMonException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public SmartMonResponse<String> handleException(SmartMonException error) {
    log.error("Server Error: ", error);
    return new SmartMonResponse<String>(error.getErrno(), error.getMessage());
  }
}
