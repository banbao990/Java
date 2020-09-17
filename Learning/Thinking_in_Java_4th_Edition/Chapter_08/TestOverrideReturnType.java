/**
 * @author banbao
 */

import static java.lang.System.out;

class Base1{}
class Derived1 extends Base1{}

class Base2{
    public Base1 func(){
        out.println("Base2");
        return new Base1();
    }
}
class Derived2 extends Base1{
    public Derived1 func(){
        out.println("Derived2");
        return new Derived1();
    }
}

public class TestOverrideReturnType{
    public static void main(String...args){
        new Derived2().func();
    }
}

/*
 * output:
 * 
 * Derived2
 *
 */
