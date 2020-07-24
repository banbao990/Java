/**
 * @author banbao
 */
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

class Base {}
class A extends Base {}
class B extends Base {}
class C extends Base {}
class D extends Base {}

public class TestInstanceof {
    public static void main(String...args) throws Exception {
        Random rd = new Random(47);
        // map
        Map<Integer, Class<? extends Base>> map = new HashMap<>();
        map.put(0, A.class);
        map.put(1, B.class);
        map.put(2, C.class);
        map.put(3, D.class);
        
        // count
        Map<String, Integer> count = new HashMap<>();
        
        // instanceof
        System.out.println("instanceof");
        for(int i = 0;i < 10;++i) {
            int which = rd.nextInt(4);
            Base b = map.get(which).newInstance();
            System.out.format(i + ":(" + which + ")");
            if(b instanceof A) { System.out.println("A"); } 
            if(b instanceof B) { System.out.println("B"); } 
            if(b instanceof C) { System.out.println("C"); } 
            if(b instanceof D) { System.out.println("D"); } 
        }
        
        // Class.isInstance(Object)
        rd.setSeed(47);
        List<Class<? extends Base>> list = new ArrayList<>();
        Collections.addAll(list, A.class, B.class, C.class, D.class);
        System.out.println("Class.isInstance(Object)");
        for(int i = 0;i < 10;++i) {
            int which = rd.nextInt(4);
            Base b = map.get(which).newInstance();
            System.out.format(i + ":(" + which + ")");
            for(Class<? extends Base> tClass : list) {
                if(tClass.isInstance(b)) {
                    System.out.println(tClass.getName());
                    break;
                }
            }
        }
    }
}
/* Output(random) 同样的种子可以复现(47)
instanceof
0:(2)C
1:(1)B
2:(2)C
3:(0)A
4:(0)A
5:(2)C
6:(0)A
7:(1)B
8:(2)C
9:(2)C
Class.isInstance(Object)
0:(2)C
1:(1)B
2:(2)C
3:(0)A
4:(0)A
5:(2)C
6:(0)A
7:(1)B
8:(2)C
9:(2)C
*/
