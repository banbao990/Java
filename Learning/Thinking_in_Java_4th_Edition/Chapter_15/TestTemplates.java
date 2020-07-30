/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.util.Arrays;
class Templates<T> {
    private T obj;
    public Templates(T obj) { this.obj = obj; }
    public void test() { 
        // obj.f(); 
        // 错误: 找不到符号
    }
}

class Templates2<T extends HasF> {
    private T obj;
    public Templates2(T obj) { this.obj = obj; }
    public void test() { 
        obj.f(); 
    }
}

class Templates3 {
    private HasF obj;
    public Templates3(HasF obj) { this.obj = obj; }
    public void test() { 
        obj.f(); 
    }
}

class HasF {
    public void f() { 
        System.out.println("HasF:f()"); 
    }
}

class NotHasF {}

public class TestTemplates {
    public static void main(String...args) {
        // HasF
        Templates<HasF> hasF = new Templates<>(new HasF());
        hasF.test();
        // ? extends HasF
        Templates2<HasF> hasF2 = new Templates2<>(new HasF());
        hasF2.test();
        // HasF
        Templates3 hasF3 = new Templates3(new HasF());
        hasF3.test();
        // 测试
        System.out.println(Arrays.toString(
            hasF.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(
            hasF2.getClass().getTypeParameters()));
    }
}
/* Output
HasF:f()
HasF:f()
[T]
[T]
*/