/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;

public class GenericWriting {
    static List<Apple> apples = new ArrayList<Apple>();
    static List<Fruit> fruit = new ArrayList<Fruit>();
    
    // 泛型
    static <T> void writeExact(List<T> list, T item) {
        list.add(item);
    }
    static void f1() {
        writeExact(apples, new Apple());
        // writeExact(apples, new Fruit()); // ERROR
        /* 
         * 需要: List<T>,T
         * 找到: List<Apple>,Fruit
         * 原因: 推论变量T具有不兼容的限制范围
         *   等式约束条件: Apple
         *   下限: Fruit
         */
        writeExact(fruit, new Apple()); // Java8 OK
        writeExact(fruit, new Fruit());
    }
    
    // 超类型通配符
    static <T> void writeWithWildcard(
        List<? super T> list, T item) {
        list.add(item);
    }
    static void f2() {
        writeWithWildcard(apples, new Apple());
        // writeWithWildcard(apples, new Fruit()); // ERROR
        /*
         * 需要: List<? super T>,T
         * 找到: List<Apple>,Fruit
         * 原因: 推断类型不符合上限
         *   推断: Fruit
         *   上限: Apple,Object
         */
        writeWithWildcard(fruit, new Apple());
        writeWithWildcard(fruit, new Fruit());
    }
    public static void main(String[] args) { f1(); f2(); }
}
