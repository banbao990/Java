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
        // ����: ���ܷ������ղ��� b
        b.a = 2; // OK, û�иı� b ָ��Ķ�������һ��,ֻ�Ǹı��� b ָ������ֵ
    }
}

public class TestFinalArg{
    public static void main(String[] args){
        B b = new B(1);
        A.testFinal(b);
        System.out.println(b.a);
    }
}