package smartmon.utilities.misc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class BeanConverterTest {
  @Test
  public void copy() {
    Source source = Source.createInstance();
    Target target = BeanConverter.copy(source, Target.class);
    Assert.assertNotNull(target);
    assertFieldsEqual(source, target);
  }

  @Test
  public void testCopy() {
    List<Source> sources = Collections.singletonList(Source.createInstance());
    List<Target> targets = BeanConverter.copy(sources, Target.class);
    Assert.assertNotNull(targets);
    Assert.assertEquals(sources.size(), targets.size());
    for (int i = 0; i < sources.size(); i++) {
      assertFieldsEqual(sources.get(i), targets.get(i));
    }
  }

  private void assertFieldsEqual(Source source, Target target) {
    Assert.assertEquals(source.getFieldStr(), target.getFieldStr());
    Assert.assertEquals(source.getFiledInt(), target.getFiledInt());
    Assert.assertEquals(source.getFiledInteger(), target.getFiledInteger());
    Assert.assertEquals(source.getFieldBoolean(), target.getFieldBoolean());
  }

  @Test
  public void beanToMap() {
    Source source = Source.createInstance();
    Map<String, Object> map = BeanConverter.beanToMap(source);
    Assert.assertNotNull(map);
    Assert.assertEquals(source.getFieldStr(), map.get("fieldStr"));
    Assert.assertEquals(source.getFiledInt(), map.get("filedInt"));
    Assert.assertEquals(source.getFiledInteger(), map.get("filedInteger"));
    Assert.assertEquals(source.getFieldBoolean(), map.get("fieldBoolean"));
  }

  public static class Source {
    private String fieldStr;
    private int filedInt;
    private Integer filedInteger;
    private Boolean fieldBoolean;

    private static Source createInstance() {
      Source source = new Source();
      source.setFieldStr("test");
      source.setFiledInteger(1);
      source.setFieldBoolean(true);
      return source;
    }

    public String getFieldStr() {
      return fieldStr;
    }

    public void setFieldStr(String fieldStr) {
      this.fieldStr = fieldStr;
    }

    public int getFiledInt() {
      return filedInt;
    }

    public void setFiledInt(int filedInt) {
      this.filedInt = filedInt;
    }

    public Integer getFiledInteger() {
      return filedInteger;
    }

    public void setFiledInteger(Integer filedInteger) {
      this.filedInteger = filedInteger;
    }

    public Boolean getFieldBoolean() {
      return fieldBoolean;
    }

    public void setFieldBoolean(Boolean fieldBoolean) {
      this.fieldBoolean = fieldBoolean;
    }
  }

  public static class Target {
    private String fieldStr;
    private int filedInt;
    private Integer filedInteger;
    private Boolean fieldBoolean;

    public String getFieldStr() {
      return fieldStr;
    }

    public void setFieldStr(String fieldStr) {
      this.fieldStr = fieldStr;
    }

    public int getFiledInt() {
      return filedInt;
    }

    public void setFiledInt(int filedInt) {
      this.filedInt = filedInt;
    }

    public Integer getFiledInteger() {
      return filedInteger;
    }

    public void setFiledInteger(Integer filedInteger) {
      this.filedInteger = filedInteger;
    }

    public Boolean getFieldBoolean() {
      return fieldBoolean;
    }

    public void setFieldBoolean(Boolean fieldBoolean) {
      this.fieldBoolean = fieldBoolean;
    }
  }
}
