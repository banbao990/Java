/**
 * @author banbao
 * @comment �޸���ʾ������
 */

import java.util.List;
import java.util.ArrayList;

class Fruit {}
class Apple extends Fruit {}
class RedApple extends Apple {}

public class Fruits2 {
    public static void main(String...args) {
        test(new ArrayList<Apple>());
    }
    
    public static void test(List<? super Apple> list) {
        list.add(new RedApple());
        list.add(new Apple());
        // list.add(new Fruit());
        return;
    }
}
