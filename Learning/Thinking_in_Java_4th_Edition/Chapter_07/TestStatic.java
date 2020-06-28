/**
 * @author banbao
 */

public class TestStatic{
    public static void main(String[] args){
        System.out.println("-----1");
        // A.c = 0; // 注释语句(1)
        // 注释语句(1)验证对于 static 变量的引用或者类对象的生成都会触发类的加载
        System.out.println("-----2");
        new A(); // 会先检查基类的 static 代码
        System.out.println("-----3");
        new A(); // 没有输出, static 代码块只会执行初始化一次
    }
}

class B{
    static{
        System.out.println("B:static test!"); // 输出顺序: 1
    }
}

class A extends B{
    static{
        System.out.println("A:static test 1!"); // 输出顺序: 2
    }
    static{
        System.out.println("A:static test 2!"); // 输出顺序: 3(顺序输出)
    }
    static int c = 0;
    static {
        int d = c + 1;
        // int b = a + 1; // 错误: 非法前向引用
    }
    static int a = 0;
    static int d = func(); // static 方法的加载早于 static 字段
    static int func(){
        return 0;
    }
}

/* 
 * output:
 *
 *
 * 将 "注释语句(1)" 注释
 * -----1
 * -----2
 * B:static test!
 * A:static test 1!
 * A:static test 2!
 * -----3
 *
 *
 * 不将 "注释语句(1)" 注释
 * -----1
 * B:static test!
 * A:static test 1!
 * A:static test 2!
 * -----2
 * -----3
 */