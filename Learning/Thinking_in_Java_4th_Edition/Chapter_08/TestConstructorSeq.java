/**
 * @author banbao
 */

import static java.lang.System.out;

class Base{
    Base(){
        sayHi();
    }
    
    void sayHi(){
        out.println("Base:Hi");
    }
}

class Derived extends Base{
    int kind = 1;
    Derived(){}
    void sayHi(){
        out.println("Derived:Hi, my kind is " + kind);
    }
}

public class TestConstructorSeq{
    public static void main(String...args){
        new Derived();
    }
}

/*
 * output:
 * 
 * Derived:Hi, my kind is 0
 *
 */
