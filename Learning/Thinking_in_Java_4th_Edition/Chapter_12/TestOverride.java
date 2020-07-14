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
    // �����������׳����๹����û�е��쳣
    Derived1() throws E1, Exception {}
    @Override
    public void func() throws E2 {}
}

class Derived2 extends Base {
    // Ĭ�Ϲ�������δ������쳣����E1
    // Derived2() {}
    Derived2() throws E1 {}
    // @Override
    // ����: Derived2�е�func()�޷�����Base�е�func()
    // �����ǵķ���δ�׳�Exception
    // public void func() throws Exception {}
}

public class TestOverride {
    public static void main(String...args){}
}
/*
*/