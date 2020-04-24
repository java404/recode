package smartmon.smartstor.domain.model.enums;

import java.util.Arrays;
import java.util.Objects;

public interface IEnum {
  int getCode();

  String getName();

  static <T extends IEnum> T getByCode(Class<T> clz, Integer code) {
    return Arrays.stream(clz.getEnumConstants())
      .filter(enumConstant -> Objects.equals(enumConstant.getCode(), code))
      .findFirst().orElse(null);
  }

  static <T extends IEnum> T getByName(Class<T> clz, String name) {
    return Arrays.stream(clz.getEnumConstants())
      .filter(enumConstant -> Objects.equals(enumConstant.getName(), name))
      .findFirst().orElse(null);
  }
}
