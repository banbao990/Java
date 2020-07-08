/**
 * @author banbao
 * @comment 匿名类
 */

interface Base{
    void func();
}

class Outer{
    public Base func(){
        // 匿名类
        // 自动创建一个继承自Base(实现Base接口)的类
        // 并且向上转型至Base
        return new Base(){
            public void func(){
                System.out.println("Inner1:func");
            }
        };
    }
}

public class TestAnonymousClass{
    public static void main(String...args){
        Base b = new Outer().func();
        b.func();
    }
}

/* 
 * output : 
 * 
 * Inner1:func
 * 
 */