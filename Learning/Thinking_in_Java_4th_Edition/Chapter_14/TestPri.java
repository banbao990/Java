/**
 * @author banbao
 */

import java.lang.reflect.Method;
class A {
    private void f() {
        System.out.println("private:A.f()");
    }
}

public class TestPri extends A {
    public static void main(String...args) {
        A a = new A();
        // a.f(); // ����: f() �� A ���� private ���ʿ���
        try {
            Method m = a.getClass().getDeclaredMethod("f");
            m.setAccessible(true);
            m.invoke(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/* Output
private:A.f()
*/