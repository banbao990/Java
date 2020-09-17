/**
 * @author banbao
 * @comment .this/.new/嵌套类
 */

class Outer{
    final public int value = 10;
    public class Inner{
        final public int value = 100;
        public void print(){
            System.out.println(this.value);             // 100
            System.out.println(Outer.this.value);       // 10
        }
    }
    
    // 静态内部类(嵌套类)
    public static class StaticInner{
        private int value = 11;
        public void print(){
            System.out.println(value);
        }
    }
    
    public void print(){
        new Inner().print();
    }
}

public class TestDotNewDotThis{
    public static void main(String...args){
        // .this
        new Outer().print();
        System.out.println("-----");
        // .new 
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner(); // private Inner 则无法使用
        inner.print();
        System.out.println("-----");
        // Outer.Inner inner2 = new Outer.Inner(); 
        // 错误: 需要包含Outer.Inner的封闭实例
        // static
        System.out.println("-----");
        Outer.StaticInner staticInner = new Outer.StaticInner();
        staticInner.print();
    }
}
