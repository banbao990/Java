/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.lang.reflect.*;
import java.util.*;
import net.mindview.util.*; // 需要将 `ExampleCode` 加到 cp 中

enum Explore { HERE, THERE }

public class Reflection {
    public static Set<String> analyze(Class<?> enumClass) {
        System.out.println("----- Analyzing " + enumClass + " -----");
        System.out.println("Interfaces:");
        for(Type t : enumClass.getGenericInterfaces())
            System.out.println(t);
        System.out.println("Base: " + enumClass.getSuperclass());
        System.out.println("Enum: " 
            + Arrays.toString(enumClass.getEnumConstants()));
        System.out.println("Methods: ");
        Set<String> methods = new TreeSet<String>();
        for(Method m : enumClass.getMethods())
            methods.add(m.getName());
        System.out.println(methods);
        return methods;
    }
    public static void main(String[] args) {
        Set<String> exploreMethods = analyze(Explore.class);
        Set<String> enumMethods = analyze(Enum.class);
        System.out.println("\nExplore.containsAll(Enum)? " +
            exploreMethods.containsAll(enumMethods));
        System.out.print("Explore.removeAll(Enum): ");
        exploreMethods.removeAll(enumMethods);
        System.out.println(exploreMethods + "\n");
        // Decompile the code for the enum:
        OSExecute.command("javap -p Explore");
    }
} 
/* Output:
----- Analyzing class Explore -----
Interfaces:
Base: class java.lang.Enum
Enum: [HERE, THERE]
Methods: 
[compareTo, equals, getClass, getDeclaringClass, hashCode, name, notify, notifyAll, ordinal, toString, valueOf, values, wait]
----- Analyzing class java.lang.Enum -----
Interfaces:
java.lang.Comparable<E>
interface java.io.Serializable
Base: class java.lang.Object
Enum: null
Methods: 
[compareTo, equals, getClass, getDeclaringClass, hashCode, name, notify, notifyAll, ordinal, toString, valueOf, wait]

Explore.containsAll(Enum)? true
Explore.removeAll(Enum): [values]

Compiled from "Reflection.java"
final class Explore extends java.lang.Enum<Explore> {
  public static final Explore HERE;
  public static final Explore THERE;
  private static final Explore[] $VALUES;
  public static Explore[] values();
  public static Explore valueOf(java.lang.String);
  private Explore();
  static {};
}
*/
