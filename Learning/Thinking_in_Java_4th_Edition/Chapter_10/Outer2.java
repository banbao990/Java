/**
 * @author banbao
 * @comment 子类的内部类和父类的内部类位于不同的命名空间
 *          父类的内部类不能被覆盖
 */

class Outer{
    // 用于测试的内部类
    private Inner inner;
    Outer(){}
    public Outer(int nouse){
        inner = new Inner(0);
    }
    
    class Inner{
        Inner(){}
        Inner(int nouse){
            System.out.println("Outer.Inner");
        }
        public void test(){
            System.out.println("Outer.Inner:test");
        }
    }
    
    // 测试多态
    public void test(){
        a();
    }
    // 多态
    public void a(){
        System.out.println("Outer:a");
    }
}

public class Outer2 extends Outer{
    public Outer2(){}
    public Outer2(int nouse){
        super(nouse);
    }
    class Inner{
        public Inner(){}
        public Inner(int nouse){
            System.out.println("Outer2.Inner");
        }
        public void test(){
            System.out.println("Outer2.Inner:test");
        }
    }
    
    // 覆盖
    @Override
    public void a(){
        System.out.println("Outer2:a");
    }
    public static void main(String...args){
        Outer2 o2 = new Outer2();
        // 多态测试
        System.out.println("-----");
        o2.test();
        // 不能覆盖内部类测试
        System.out.println("-----");
        new Outer2(0);
        // 拥有独立命名空间测试
        System.out.println("-----");
        Outer o1 = new Outer();
        Outer.Inner i1 = o1.new Inner();
        Outer2.Inner i2 = o2.new Inner();
        i1.test();
        i2.test();
    }
}
/*
 * output
 * 
 * -----
 * Outer2:a
 * -----
 * Outer.Inner
 * -----
 * Outer.Inner:test
 * Outer2.Inner:test
 */
