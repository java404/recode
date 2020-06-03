package smartmon.falcon.remote.test;

import smartmon.falcon.remote.types.strategy.FalconStrategy;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.utilities.misc.BeanConverter;

public class Test {

  public static void main(String[] args) {

    B b = new B();
    b.setName("zhangsan");
    b.setPassword(123456);
    b.setId(10);
    b.setIds("123456789");

    final A copy = BeanConverter.copy(b, A.class);
    System.out.println(copy);

  }
}
