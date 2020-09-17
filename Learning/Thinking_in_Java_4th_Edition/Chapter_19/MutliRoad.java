/**
 * @author banbao
 * @comment 修改自示例代码
 */

public class MutliRoad {
    public static void test(Number num1, Number num2) {
        num1.test(num2);
    }
    public static void test(NNumber num1, NNumber num2) {
        num1.test(num2);
    }
    
    public static void test2(Number num1, Number num2) {
        if(num2 instanceof Number1) num1.test((Number1)num2);
        else if(num2 instanceof Number2) num1.test((Number2)num2);
        else if(num2 instanceof Number) num1.test((Number)num2);
    }
    
    public static void testMulti() {
        System.out.println("~~~~~ testMulti ~~~~~");
        (new Number1()).test(new Number2());                       // Number1 Number2
        ((Number)(new Number1())).test(new Number2());             // Number1 Number2
        (new Number1()).test((Number)(new Number2()));             // Number1 Number
        ((Number)(new Number1())).test((Number)(new Number2()));   // Number1 Number
    }
    
    public static void main(String...args) {
        System.out.println("~~~~~ test ~~~~~");
        test(new Number(), new Number());
        test(new Number(), new Number1());
        test(new Number(), new Number2());
        test(new Number1(), new Number());
        test(new Number1(), new Number1());
        test(new Number1(), new Number2());
        test(new Number2(), new Number());
        test(new Number2(), new Number1());
        test(new Number2(), new Number2());
        
        System.out.println("~~~~~ test2 ~~~~~");
        test2(new Number1(), new Number2());
        testMulti();
        // new test
        System.out.println("~~~~~ test ~~~~~");
        test(new NNumber(), new NNumber());
        test(new NNumber(), new NNumber1());
        test(new NNumber(), new NNumber2());
        test(new NNumber1(), new NNumber());
        test(new NNumber1(), new NNumber1());
        test(new NNumber1(), new NNumber2());
        test(new NNumber2(), new NNumber());
        test(new NNumber2(), new NNumber1());
        test(new NNumber2(), new NNumber2());
        
    }
}
/* Output
~~~~~ test ~~~~~
Number Number
Number Number
Number Number
Number1 Number
Number1 Number
Number1 Number
Number2 Number
Number2 Number
Number2 Number
~~~~~ test2 ~~~~~
Number1 Number2
~~~~~ testMulti ~~~~~
Number1 Number2
Number1 Number2
Number1 Number
Number1 Number
~~~~~ test ~~~~~
reverse
reverse reverse
NNumber NNumber
reverse
reverse reverse
NNumber NNumber1
reverse
reverse reverse
NNumber NNumber2
reverse
reverse reverse
NNumber NNumber
reverse
reverse reverse
NNumber1 NNumber1
reverse
reverse reverse
NNumber1 NNumber2
reverse
reverse reverse
NNumber NNumber
reverse
reverse reverse
NNumber2 NNumber1
reverse
reverse reverse
NNumber2 NNumber2
*/
