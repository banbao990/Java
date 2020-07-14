/**
 * @author banbao
 */
import java.io.*;

class E1 extends Exception {}
class E2 extends E1 {}
class Base {
    Base() throws E1 {}
    public void func() throws E1 {}
}
class Derived1 extends Base {
    // 构造器可以抛出基类构造器没有的异常
    Derived1() throws E1, Exception {}
    @Override
    public void func() throws E2 {}
}

class Derived2 extends Base {
    // 默认构造器中未报告的异常错误E1
    // Derived2() {}
    Derived2() throws E1 {}
    // @Override
    // 错误: Derived2中的func()无法覆盖Base中的func()
    // 被覆盖的方法未抛出Exception
    // public void func() throws Exception {}
}

public class TestOverride {
    public static void main(String...args){}
}
/*
*/