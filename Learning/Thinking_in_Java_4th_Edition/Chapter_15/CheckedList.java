/**
 * @author banbao
 * @comment �޸���ʾ������
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
        test1(); // ���Ա���,���������׳��쳣
        test2(); // ���Ա���,���������׳��쳣
    }
    
    public static void test2() {
        List<Dog> dogs = Collections.checkedList(
            new ArrayList<>(), Dog.class);
        oldStyleMethod(dogs);
        // ��һ���׳��쳣
        // ClassCastException: Attempt to insert class Cat 
        //     element into collection with element type class Dog
        for(Dog d : dogs) { 
            d.bark();
        }
    }
    
    public static void test1() {
        List<Dog> dogs = new ArrayList<>();
        // dogs.add(new Cat()); // ����: ����add(Cat), �Ҳ������ʵķ���
        oldStyleMethod(dogs);
        // ClassCastException: Cat cannot be cast to Dog
        // ��һ���׳��쳣
        for(Dog d : dogs) { 
            d.bark();
        }
    }
}
/* Output
*/