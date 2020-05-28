package smartmon.smartstor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiskNvmeHealthDto {
  private Long totallife; // "totallife": 26280
  private Long life; // "life": 26280
  private String health; // "health": "PASS"

}
