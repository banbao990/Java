/**
 * @author banbao
 */

class B{
    public int a;
    public B(int a){
        this.a = a;
    }
}

class A{
    public static void testFinal(final B b){
        // b = new B(10);
        // 错误: 不能分配最终参数 b
        b.a = 2; // OK, 没有改变 b 指向的对象是哪一个,只是改变了 b 指向对象的值
    }
}

public class TestFinalArg{
    public static void main(String[] args){
        B b = new B(1);
        A.testFinal(b);
        System.out.println(b.a);
    }
}
