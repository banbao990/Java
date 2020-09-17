/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.lang.reflect.Method;
import java.lang.NoSuchMethodException;

class Dog {
    public void speak() {
        System.out.println("Dog:speak()");
    }
    public void sit() {
        System.out.println("Dog:sit()");
    }
}

class Robot {
    public void speak() {
        System.out.println("Robot:speak()");
    }
    public void work() {
        System.out.println("Robot:work()");
    }
}

class PseudoLatent {
    public static void perform(Object obj) {
        Class<?> cl = obj.getClass();
        // speak
        try {
            Method speakMethod = cl.getMethod("speak");
            speakMethod.invoke(obj);
        } catch(NoSuchMethodException e) {
            System.out.println("The class don't have the method speak()!");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        // work
        try {
            Method speakMethod = cl.getMethod("work");
            speakMethod.invoke(obj);
        } catch(NoSuchMethodException e) {
            System.out.println("The class don't have the method work()!");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}

public class LatentReflection {
    public static void main(String...args) {
        PseudoLatent.perform(new Dog());
        PseudoLatent.perform(new Robot());
    }
}


/* Output
Dog:speak()
The class don't have the method work()!
Robot:speak()
Robot:work()
*/
