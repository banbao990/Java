/**
 * @author banbao
 */

class A{
    final int a;
    A(int a){
        // 在构造函数中为 final 变量 a 赋值
        this.a = a;
    }

    public void change(int a){
        // this.a = a;
        // 错误: 无法为最终变量 a 分配值
    }

    public void func(){
        System.out.println("a");
    }
}

class TestFinal{
    public static void main(String[] args){
        System.out.println(new A(10).a);
    }
}
