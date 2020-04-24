package smartmon.smartstor.infra.persistence.entity.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import smartmon.smartstor.domain.model.enums.IEnum;

public class EnumValueTypeHandler extends BaseTypeHandler<IEnum> {

  private Class<IEnum> type;

  public EnumValueTypeHandler(Class<IEnum> type) {
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i,
                                  IEnum parameter,
                                  JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getCode());
  }

  @Override
  public IEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return convert(rs.getInt(columnName));
  }

  @Override
  public IEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return convert(rs.getInt(columnIndex));
  }

  @Override
  public IEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return convert(cs.getInt(columnIndex));
  }

  private IEnum convert(int code) {
    IEnum[] enumConstants = type.getEnumConstants();
    for (IEnum em: enumConstants) {
      if (em.getCode() == code) {
        return  em;
      }
    }
    return null;
  }
}
