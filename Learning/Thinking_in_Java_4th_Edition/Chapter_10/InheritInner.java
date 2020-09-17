/**
 * @author banabo
 * @comment 修改自示例代码
 */

class WithInner{
    int testOuter = 11;
    class Inner{
        int testInner = 22;
    }
}

public class InheritInner extends WithInner.Inner{
    // InheritInner(){}
    // 错误: 需要包含WithInner.Inner的封闭实例
    InheritInner(WithInner wi){
        wi.super(); // 需要这么使用才能够实现继承内部类
    }
    public static void main(String...args){
        WithInner wi = new WithInner();
        InheritInner ii = new InheritInner(wi);
        // System.out.println(ii.testOuter);
        // 错误: 找不到符号
        // System.out.println(((WithInner.Inner)ii).testOuter);
        // 错误: 找不到符号
        System.out.println(ii.testInner);
    }
}
