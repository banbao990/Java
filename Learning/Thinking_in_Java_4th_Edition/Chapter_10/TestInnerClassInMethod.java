/**
 * @author banbao
 */

interface Base{
    void func();
}

class Outer{
    public Base func(){
        // 不允许增加访问权限限定词( public,private 都不行)
        class Inner implements Base{
            public void func(){
                System.out.println("Inner1:func");
            }
        }
        return new Inner();
    }
    
    public void test(){
        // new Inner().func();
        // 错误: 找不到符号
    }
}

public class TestInnerClassInMethod{
    public static void print(){
        System.out.println("-----");
    }
    
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
