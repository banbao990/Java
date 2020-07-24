/**
 * @author banbao
 */

class Init1 {
    static {
        System.out.println("Init1:static!");
    }
}
class Init2 {
    static {
        System.out.println("Init2:static!");
    }
}

public class TestClass2 {
    public static void main(String...args) throws Exception {
        // Init1.class
        Class init1 = Init1.class;
        System.out.println("After Init1.class");
        new Init1();
        System.out.println("new Init1()");
        
        System.out.println("~~~~~~~");
        
        // Class.forName("Init2")
        Class init2 = Class.forName("Init2");
        System.out.println("Class.forName(\"Init2\")");
        new Init2();
        System.out.println("new Init2()");
        
        System.out.println("~~~~~~~");
        System.out.println(Init3.ii);
    }
}
/* Output
After Init1.class
Init1:static!
new Init1()
~~~~~~~
Init2:static!
Class.forName("Init2")
new Init2()
*/