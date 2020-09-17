/**
 * @author banbao
 */
 
class Base{
    public Base(){
        System.out.println("Base constructor");
    }
    
    public Base(int a){
        System.out.println("Base constructor with an integer arg");    
    }
}

class Derived extends Base{
    public Derived(){
        super();
        System.out.println("Derived constructor");
    }
    
    public Derived(int a){
        super(a);
        System.out.println("Derived constructor with an integer arg");
    }
    
}

class Derived2 extends Base{
    
}

public class TestExtends{
    public static void main(String[] args){
        System.out.println("----------");
        new Derived();
        System.out.println("----------");
        new Derived(1);
        System.out.println("----------");
        new Derived2();
    }
}
/* 
 * output:
 * 
 * ----------
 * Base constructor
 * Derived constructor
 * ----------
 * Base constructor with an integer arg
 * Derived constructor with an integer arg
 * ----------
 * Base constructor
*/
