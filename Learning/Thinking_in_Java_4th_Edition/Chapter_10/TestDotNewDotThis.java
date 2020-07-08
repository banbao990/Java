/**
 * @author banbao
 * @comment .this/.new/Ƕ����
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
    
    // ��̬�ڲ���(Ƕ����)
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
        Outer.Inner inner = outer.new Inner(); // private Inner ���޷�ʹ��
        inner.print();
        System.out.println("-----");
        // Outer.Inner inner2 = new Outer.Inner(); 
        // ����: ��Ҫ����Outer.Inner�ķ��ʵ��
        // static
        System.out.println("-----");
        Outer.StaticInner staticInner = new Outer.StaticInner();
        staticInner.print();
    }
}