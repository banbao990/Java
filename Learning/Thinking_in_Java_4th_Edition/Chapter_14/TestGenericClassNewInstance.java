/**
 * @author banbao
 */

class Base {}
class Derived extends Base {}

public class TestGenericClassNewInstance {
    public static void main(String...args) throws Exception {
        // 泛型
        Class<Derived> cl1 = Derived.class;
        Derived instance1 = cl1.newInstance();
        
        // 不使用泛型
        Class cl2 = Derived.class;
        // Derived instance2 = cl2.newInstance();
        // 错误: 不兼容的类型: Object无法转换为Derived
        
        // 模糊的类型,而不是具体的
        Class<? super Derived> cl3 = cl1.getSuperclass();
        // Class<Base> cl4 = cl1.getSuperclass();
        /* 错误: 不兼容的类型: Class<CAP#1>无法转换为Class<Base>
         * Class<Base> cl4 = cl1.getSuperclass();
         * 其中, CAP#1是新类型变量:
         * CAP#1从? super Derived的捕获扩展Object 超 Derived
         */
    }
}
