/**
 * @author banbao
 */

interface Base{
    void func();
}

class Outer{
    private class Inner1 implements Base{
        public void func(){
            System.out.println("Inner1:func");
        }
    }
    
    public class Inner2 implements Base{
        public void func(){
            System.out.println("Inner2:func");
        }
    }
}

public class TestPrivateInnerClass{
    public static void print(){
        System.out.println("-----");
    }
    
    public static void main(String...args){
        Outer outer = new Outer();
        
        // Outer.Inner1 inner1 = outer.new Inner1();
        // 错误: Outer.Inner1 在 Outer 中是 private 访问控制
        
        Outer.Inner2 inner2 = outer.new Inner2();
        
        // Base inner3 = outer.new Inner1();
        // 错误: Outer.Inner1 在 Outer 中是 private 访问控制
        
        Base inner4 = outer.new Inner2();
        print();
        // inner1.func();
        print();
        inner2.func();
        print();
        // inner3.func();
        print();
        inner4.func();
        print();
    }
}
/* 
 *output:
 *
 * -----
 * -----
 * Inner2:func
 * -----
 * -----
 * Inner2:func
 * -----
*/