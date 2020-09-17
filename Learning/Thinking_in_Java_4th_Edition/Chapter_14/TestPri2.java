/**
 * @author banbao
 */

import java.lang.reflect.Method;
import java.lang.reflect.Field;
interface TestPriInterface {
    void g();
}

class OuterA implements TestPriInterface {
    @Override
    public void g() {}
    private static class InnerA implements TestPriInterface{
        @Override
        public void g() {}
        private void f() {
            System.out.println("private:A.f()");
        }
    }
    public static TestPriInterface make() {
        return new InnerA();
    }
    public static TestPriInterface make2() {
        return new TestPriInterface() {
            @Override
            public void g() {}
            private void f() {
                System.out.println("private:A.f()");
            }
        };
    }
    // 测试私有域
    private int a = 0;
    private static int b = 0;
    private final int c = 0;
    private static final int d = 0;
    public void print(){
        System.out.format("%d %d %d %d\n", a, b, c, d);
    }
}

public class TestPri2 extends A {
    public static void main(String...args) {
        // 私有内部类
        TestPriInterface a = OuterA.make();
        // a.f(); // 错误: f() 在 A 中是 private 访问控制
        try {
            Method m = a.getClass().getDeclaredMethod("f");
            m.setAccessible(true);
            m.invoke(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 匿名类
        a = OuterA.make2();
        // a.f(); // 错误: f() 在 A 中是 private 访问控制
        try {
            Method m = a.getClass().getDeclaredMethod("f");
            m.setAccessible(true);
            m.invoke(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 私有域
        a = new OuterA();
        String[] modifier = {
            "private",
            "private static",
            "private final",
            "private static final",
        };
        String[] name = {"a", "b", "c", "d", };
        for(int i = 0;i < 4; ++i) {
            try {
                Field f = a.getClass().getDeclaredField(name[i]);
                f.setAccessible(true);
                System.out.println(modifier[i]);
                System.out.println("f.getInt(a):" + f.getInt(a));
                f.setInt(a, 1);
                System.out.println("(After set)f.getInt(a):" + f.getInt(a));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(a instanceof OuterA) {
            ((OuterA)a).print();
        }
    }
}
/* Output
private:A.f()
*/
