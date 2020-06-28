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
         * TestOverride.java:26: 错误: 对于fun(float), 找不到合适的方法
         * 方法 A.fun(int)不适用
         * (参数不匹配; 从float转换到int可能会有损失)
         * 方法 A.fun(Test)不适用
         * (参数不匹配; float无法转换为Test)
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