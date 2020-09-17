/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Animal {}
class Cat extends Animal {}
class Dog extends Animal {
    public void bark() {
        System.out.println("Dog Bark!");
    }
}

public class CheckedList {
    @SuppressWarnings("unchecked")
    public static void oldStyleMethod(List dogs) {
        dogs.add(new Cat());
    }
    
    public static void main(String...args) {
        test1(); // 可以编译,但是运行抛出异常
        test2(); // 可以编译,但是运行抛出异常
    }
    
    public static void test2() {
        List<Dog> dogs = Collections.checkedList(
            new ArrayList<>(), Dog.class);
        oldStyleMethod(dogs);
        // 上一行抛出异常
        // ClassCastException: Attempt to insert class Cat 
        //     element into collection with element type class Dog
        for(Dog d : dogs) { 
            d.bark();
        }
    }
    
    public static void test1() {
        List<Dog> dogs = new ArrayList<>();
        // dogs.add(new Cat()); // 错误: 对于add(Cat), 找不到合适的方法
        oldStyleMethod(dogs);
        // ClassCastException: Cat cannot be cast to Dog
        // 下一行抛出异常
        for(Dog d : dogs) { 
            d.bark();
        }
    }
}
/* Output
*/
