/**
 * @author banbao
 */

class A{
    char a = 'A';
    void func(){
        System.out.println("A");
    }
}

class B extends A{
    char a = 'B';
    
    @Override
    void func(){
        System.out.println("B");
    }
}

class TestBind{
    public static void main(String...args){
        func(new A());
        System.out.println("-----");
        func(new B());
        System.out.println("-----");
        A a = new B();
        a.func();
    }

    public static void func(A a){
        System.out.println(a.a);
        a.func();
    }
}

/*
 * output 
 * 
 * A
 * A
 * -----
 * A
 * B
 * -----
 * B
 */
