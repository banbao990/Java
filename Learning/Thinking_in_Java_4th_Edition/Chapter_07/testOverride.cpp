/*
 * author : banbao
*/

#include <stdio.h>

class Test{

};

class A{
    public:
    void fun(int a){
        printf("A:%d\n", a);
    }
    void fun(Test a){
        printf("Test\n");
    }
};

class B : A{
    public:
    void fun(float a){
        printf("B:%f\n", a);
    }
};

int main() {
    A testA = A();
    B testB = B();
    testA.fun(1.0f); // warning C4244: '参数' : 从 'float' 转换到 'int', 可能丢失数据
    testA.fun(1);
    testA.fun(Test());

    printf("------\n");
    testB.fun(1.0f);
    testB.fun(1);
    // testB.fun(Test()); // error C2664: 'void B::fun(float)' : 无法将参数 1 从 'Test' 转换为 'float'

    return 0;
}
/*
 * output :
 * A:1
 * A:1
 * Test
 * ------
 * B:1.000000
 * B:1.000000
 */