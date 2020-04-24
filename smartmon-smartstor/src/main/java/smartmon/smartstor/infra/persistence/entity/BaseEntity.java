package smartmon.smartstor.infra.persistence.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BaseEntity {
  private LocalDateTime createTime;
  private LocalDateTime updateTime;

  public BaseEntity() {
    createTime = LocalDateTime.now();
    updateTime = LocalDateTime.now();
  }
}
