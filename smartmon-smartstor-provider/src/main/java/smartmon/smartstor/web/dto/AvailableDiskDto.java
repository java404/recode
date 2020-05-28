package smartmon.smartstor.web.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AvailableDiskDto extends SimpleDiskDto {
  private Integer partCount;
  private List<Integer> partNos = new ArrayList<>();
}
