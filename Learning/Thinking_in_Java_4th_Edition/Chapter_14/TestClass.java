/**
 * @author banbao
 */
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TestClass {
    
    public void run() {
        TestClass.print("TestClass.run()");
    }
    
    public static void print(Object o) {
        System.out.println(o);
    }
    
    public static void main(String...args) {
        Class cl = null;
        try {
            cl = Class.forName("java.util.ArrayList");
        } catch (Exception e) {
            print(e);
            System.exit(1);
        }
        cl = ArrayList.class;
        // Field
        print("~~Filed~~");
        for(Field a : cl.getDeclaredFields())
            print(a);
        // Method : getDeclaredMethods()
        print("~~~~");
        print("Is enum:" + cl.isEnum());
        print("Is interface:" + cl.isInterface());
        //  局部类是指那些被定义在块中的类
        print("Is localClass:" + cl.isLocalClass()); 
        print("Superclass:" + cl.getSuperclass()); 
        print("getCanonicalName():" + cl.getCanonicalName());
        print("getName():" + cl.getName());
        print("getSimpleName():" + cl.getSimpleName());
        print("getTypeName():" + cl.getTypeName());
        print("toGenericString():" + cl.toGenericString());
        // newInstance
        Object obj = null;
        try {
            // Requires default constructor:
            obj = cl.newInstance();
        } catch(InstantiationException e) {
            print("Cannot instantiate");
            System.exit(1);
        } catch(IllegalAccessException e) {
            print("Cannot access");
            System.exit(1);
        }
    }
}

/* Output
~~Filed~~
private static final long java.util.ArrayList.serialVersionUID
private static final int java.util.ArrayList.DEFAULT_CAPACITY
private static final java.lang.Object[] java.util.ArrayList.EMPTY_ELEMENTDATA
private static final java.lang.Object[] java.util.ArrayList.DEFAULTCAPACITY_EMPTY_ELEMENTDATA
transient java.lang.Object[] java.util.ArrayList.elementData
private int java.util.ArrayList.size
private static final int java.util.ArrayList.MAX_ARRAY_SIZE
~~~~
Is enum:false
Is interface:false
Is localClass:false
Superclass:class java.util.AbstractList
getCanonicalName():java.util.ArrayList
getName():java.util.ArrayList
getSimpleName():ArrayList
getTypeName():java.util.ArrayList
toGenericString():public class java.util.ArrayList<E>
*/
