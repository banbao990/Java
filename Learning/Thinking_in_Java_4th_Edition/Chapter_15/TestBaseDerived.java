/**
 * @author banbao
 * @comment 修改自示例代码
 */

class Base {
    public Base test(){ return new Base(); }
    public void test2(Base b){ 
        System.out.println("Base:test2:Base");
    }
}
class Derived extends Base {
    @Override
    public Derived test(){ return new Derived(); }
    // @Override
    // 和基类是两个方法,只是重载(Overload)
    public void test2(Derived d){ 
        System.out.println("Derived:test2:Derived");
    }
}

class Base2<T> {
    public void test2(T b){ 
        System.out.println("Base2:test2:T");
    }
}
class Derived2 extends Base2<Base> {
    @Override
    public void test2(Base b){ 
        System.out.println("Derived2:test2:Base");
    }
    // @Override
    // 重载
    public void test2(Derived b){ 
        System.out.println("Derived2:test2:Derived");
    }
}

class SelfBounded<T extends SelfBounded<T>> {
    public void test2(T b){ 
        System.out.println("SelfBounded:test2:T");
    }
}

class Derived3 extends SelfBounded<Derived3> {
    @Override
    public void test2(Derived3 b){ 
        System.out.println("Derived3:test2:Derived3");
    }
}

public class TestBaseDerived {
    public static void main(String...args) {
        // 1. 正常的
        new Derived().test2(new Base());
        new Derived().test2(new Derived());
        // 2. 泛型
        new Derived2().test2(new Base());
        new Derived2().test2(new Derived());
        // 3. 自限定
        new Derived3().test2(new Derived3());
    }
}
/* Output
Base:test2:Base
Derived:test2:Derived
Derived2:test2:Base
Derived2:test2:Derived
Derived3:test2:Derived3
*/
