/**
 * @author banbao
 */
class Base {}
class Derived extends Base {
    static int i = 0;
    static void run() {
        System.out.println(i++);
    }
}

public class TestCast {
    public static void main(String...args) throws Exception {
        // traditional cast 传统方法
        Base b1 = new Derived();
        Derived d1 = (Derived) b1;
        d1.run();
        // new cast
        Class<Derived> cd1 = Derived.class;
        Derived d2 = cd1.cast(b1); 
        d2.run();
    }
}
/* Output
0
1
*/