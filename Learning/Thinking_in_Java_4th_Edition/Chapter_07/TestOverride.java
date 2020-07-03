/*
 * author : banbao
*/

class Test{}

class A{
    public void fun(int a){
        System.out.printf("A:%d\n", a);
    }
    public void fun(Test a){
        System.out.printf("Test\n");
    }
};

class B extends A{
    public void fun(float a){
        System.out.printf("B:%f\n", a);
    }
}

public class TestOverride{
    public static void main(String[] args){
        A testA = new A();
        B testB = new B();
        // testA.fun(1.0f);
        /*
         * TestOverride.java:26: ����: ����fun(float), �Ҳ������ʵķ���
         * ���� A.fun(int)������
         * (������ƥ��; ��floatת����int���ܻ�����ʧ)
         * ���� A.fun(Test)������
         * (������ƥ��; float�޷�ת��ΪTest)
         */ 
        testA.fun(1);
        testA.fun(new Test());
        
        System.out.printf("------\n");
        testB.fun(1.0f);
        testB.fun(1);
        testB.fun(new Test());
    }
}
/* 
 * output :
 * A:1
 * Test
 * ------
 * B:1.000000
 * A:1
 * Test
 */