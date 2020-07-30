/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.ArrayList;
public class ErasedTypeEquivalence {
    public static void main(String...args) {
        Class c1 = new ArrayList<Integer>().getClass();
        Class c2 = new ArrayList<String>().getClass();
        System.out.println(c1 == c2);
    }
}
/* Output
true
*/