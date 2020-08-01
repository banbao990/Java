/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.Arrays;

class MakeArray<T> {
    private Object[] obj;
    public MakeArray(int size) {
        this.obj = new Object[size];
    }
    @SuppressWarnings("unchecked")
    public T[] getArray() {
        return (T[]) obj;
    }
    public void set(T o, int index)  {
        this.obj[index] = o;
    }
} 

public class TestArrayAndGeneric {
    public static void main(String...args) {
        MakeArray<String> str = new MakeArray<String>(10);
        str.set("test", 0);
        // str.set(1, 0); // 错误: 不兼容的类型: int无法转换为String
        System.out.println(Arrays.deepToString(str.getArray()));
    }
}


/* Output
[null, null, null, null, null, null, null, null, null, null]
*/