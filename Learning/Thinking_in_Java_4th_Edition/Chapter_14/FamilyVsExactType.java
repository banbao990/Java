/**
 * @author banbao
 * @comment 修改自示例代码
 */

class Base {}
class Derived extends Base {}

public class FamilyVsExactType {
    static void print(String...args) {
        for(String x : args)
            System.out.print(x);
        System.out.println();
    }
    
    static void test(Object x) {
        print("Testing x of type " + x.getClass());
        print("x instanceof Base " + (x instanceof Base));
        print("x instanceof Derived "+ (x instanceof Derived));
        print("Base.isInstance(x) "+ Base.class.isInstance(x));
        print("Derived.isInstance(x) " +
            Derived.class.isInstance(x));
        print("x.getClass() == Base.class " +
            (x.getClass() == Base.class));
        print("x.getClass() == Derived.class " +
            (x.getClass() == Derived.class));
        print("x.getClass().equals(Base.class)) "+
            (x.getClass().equals(Base.class)));
        print("x.getClass().equals(Derived.class)) " +
            (x.getClass().equals(Derived.class)));
        print();
    }
    
    public static void main(String[] args) {
        test(new Base());
        test(new Derived());
    }

} 
/* Output:
Testing x of type class Base
x instanceof Base true
x instanceof Derived false
Base.isInstance(x) true
Derived.isInstance(x) false
x.getClass() == Base.class true
x.getClass() == Derived.class false
x.getClass().equals(Base.class)) true
x.getClass().equals(Derived.class)) false

Testing x of type class Derived
x instanceof Base true
x instanceof Derived true
Base.isInstance(x) true
Derived.isInstance(x) true
x.getClass() == Base.class false
x.getClass() == Derived.class true
x.getClass().equals(Base.class)) false
x.getClass().equals(Derived.class)) true

*/
