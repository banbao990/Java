/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.lang.reflect.Constructor;
class ZeroOrOne {
    // One
    public ZeroOrOne(int One) {
        System.out.println("one arg!");
    }
    // Zero
    public ZeroOrOne() {
        System.out.println("zero args!");
    }
}

public class TestConstructor {
    public static void main(String[] args) 
        throws Exception {
        Class cl = ZeroOrOne.class;
        // Class.newInstance() 
        ZeroOrOne zoo = (ZeroOrOne)cl.newInstance();
        
        // Constructor.newInstance()
        // zero args
        zoo = (ZeroOrOne)cl
                .getDeclaredConstructor()
                .newInstance();
        // one arg
        zoo = (ZeroOrOne)cl
                .getDeclaredConstructor(
                    new Class[]{int.class}
                ).newInstance(
                    new Object[]{5}
                );
    }
} 

/* Output:
zero args!
zero args!
one arg!
*/
