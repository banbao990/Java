/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.util.Map;
import java.util.HashMap;

class Building {}
class House extends Building {}
class NoDefault{
    NoDefault(int noUse){}
}

public class ClassTypeCapture<T> {
    // type map
    private Map<String, Class<?>> map = new HashMap<>();
    public boolean addType(String typeName, Class<?> kind) {
        if(map.get(typeName) != null) {
            System.out.println(typeName + " is already exsited!");
            return false;
        } else {
            map.put(typeName, kind);
            System.out.println(typeName + " is added");
            return true;
        }
    }
    public Object createNew(String typeName) {
        if(map.get(typeName) == null) {
            System.out.println(typeName + " is not in the library!");
            return null;
        } else {
            Object ret = null;
            try {
                ret = map.get(typeName).newInstance();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                return ret;
            }
        }
    }
    private Class<T> kind;
    public ClassTypeCapture(Class<T> kind) {
        this.kind = kind;
    }
    public boolean f(Object arg) {
        return kind.isInstance(arg);
    }
    public static void main(String[] args) {
        ClassTypeCapture<Building> ctt1 =
            new ClassTypeCapture<Building>(Building.class);
        System.out.println(ctt1.f(new Building()));
        System.out.println(ctt1.f(new House()));
        ClassTypeCapture<House> ctt2 =
            new ClassTypeCapture<House>(House.class);
        System.out.println(ctt2.f(new Building()));
        System.out.println(ctt2.f(new House()));
        // add Building
        Class c1 = new Building().getClass();
        ctt1.addType(c1.getSimpleName(), c1);
        Building b1 = (Building)ctt1.createNew(c1.getSimpleName());
        System.out.println(b1);
        // String
        String s1 = (String)ctt1.createNew("String");
        System.out.println(s1);
        // NoDefault
        Class c2 = new NoDefault(0).getClass();
        ctt1.addType(c2.getSimpleName(), c2);
        NoDefault nd1 = (NoDefault)ctt1.createNew(c2.getSimpleName());
        System.out.println(nd1);
    }
}
/* Output:
true
true
false
true
Building is added
Building@15db9742
String is not in the library!
null
NoDefault is added
java.lang.InstantiationException: NoDefault
    at java.lang.Class.newInstance(Class.java:427)
    at ClassTypeCapture.createNew(ClassTypeCapture.java:34)
    at ClassTypeCapture.main(ClassTypeCapture.java:69)
Caused by: java.lang.NoSuchMethodException: NoDefault.<init>()
    at java.lang.Class.getConstructor0(Class.java:3082)
    at java.lang.Class.newInstance(Class.java:412)
    ... 2 more
null
*/
