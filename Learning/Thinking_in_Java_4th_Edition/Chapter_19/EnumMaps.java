/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;
import animals.*;
import static animals.Animal.*;
import static animals.Animal2.*;

// Animal : HORSE, COW, BIRD, DEER, GOOSE, 
public class EnumMaps {
    public static void main(String[] args) {
        EnumMap<Animal, Command> em =
            new EnumMap<Animal, Command>(Animal.class);
        em.put(HORSE, new Command() {
            public void action() { 
                System.out.println("Horse Running!"); 
            }
        });
        em.put(BIRD, new Command() {
            public void action() { 
                System.out.println("Bird Flying!"); 
            }
        });
        for(Map.Entry<Animal,Command> e : em.entrySet()) {
            System.out.print(e.getKey() + ": ");
            e.getValue().action();
        }
        try { // If there's no value for a particular key:
            em.get(COW).action();
            em.get(DUCK).action();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
/* Output
HORSE: Horse Running!
BIRD: Bird Flying!
java.lang.NullPointerException
*/