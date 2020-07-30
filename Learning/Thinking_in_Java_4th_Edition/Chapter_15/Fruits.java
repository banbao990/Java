/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

class Fruit {}
class Apple extends Fruit {}
public class Fruits {
    public static void main(String...args) {
        // 1. 数组
        Fruit[] f = new Apple[10]; // OK
        // 运行时刻类型就是具体的 Apple
        
        // 2. 泛型
        // List<Fruit> list = new ArrayList<Apple>();
        // 错误: 不兼容的类型: ArrayList<Apple>无法转换为List<Fruit>
        
        // 3. 通配符
        List<? extends Fruit> list = new ArrayList<Apple>();
        // ??? CE
        // list.add(new Apple()); // 参数类型为 E(泛型占位符)
        // list.add(new Fruit());
        // list.add(new Object());
        list.add(null); // 没用
        
        // 4. 参数为 Object
        List<? extends Fruit> flist =
            Arrays.asList(new Apple());
        Apple a = (Apple)flist.get(0);  // No warning
        flist.contains(new Apple());    // Argument is 'Object'
        flist.indexOf(new Apple());     // Argument is 'Object'
    }
}