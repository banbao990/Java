/**
 * @author banbao
 * @comment 修改自示例代码
 */

class Animal{}
class Dog extends Animal{}
class Cat extends Animal{}
public class TestArrayStoreException {
    public static void main(String...args) {
        Animal[] d = new Dog[10];
        // d[0] = new Animal(); // ArrayStoreException
        d[0] = new Dog();
        // d[0] = new Cat(); // ArrayStoreException
    }
}


/* Output
*/