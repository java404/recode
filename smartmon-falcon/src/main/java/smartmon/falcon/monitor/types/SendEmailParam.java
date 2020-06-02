package smartmon.falcon.monitor.types;

import java.util.List;
import lombok.Data;

@Data
public class SendEmailParam {
  private String subject;
  private String text;
  private List<String> receiverMails;
}
