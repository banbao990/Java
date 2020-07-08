// 匿名类

abstract class Base{
    // 无参构造函数
    Base(){ 
        System.out.println("No args!");
    }
    // 含参构造函数
    Base(int x){
        System.out.println("One arg!");
    }
    // 测试函数
    void func(){
        System.out.println("Blank!");
    }
}

class Outer{
    public Base func(){
        // 匿名类
        // 无参构造函数
        return new Base(){
            @Override
            public void func(){
                System.out.println("Func, no args!");
            }
        };
    }
    
    public Base func(int x){
        // 匿名类
        // 含参构造函数
        return new Base(x){
            public void func(){
                // x = x + 1;
                // 错误: 从内部类引用的本地变量必须是最终变量或实际上的最终变量
                System.out.println("Func, one arg!");
            }
        };
    }
}

public class TestAnonymousClassConstructor{
    public static void main(String...args){
        Base b = new Outer().func();
        b.func();
        b = new Outer().func(10);
        b.func();
    }
}

/*
 * output:
 * 
 * No args!
 * Func, no args!
 * One arg!
 * Func, one arg!
 * 
 */
