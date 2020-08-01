/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.util.Arrays;
class ClassPara<T> {
    public T[] f(T[] arg) { return arg; }
}
class MethodPara {
    public static <T> T[] f(T[] arg) { return arg; }
}
public class ParameterizedArrayType {
    public static void main(String...args) {
        Integer[] int1 = {1,2,3};
        String[] str1 = {"a","b","c"};
        // class
        Integer[] int2 = new ClassPara<Integer>().f(int1);
        String[] str2 = new ClassPara<String>().f(str1);
        // method
        Integer[] int3 = MethodPara.f(int1);
        String[] str3 = MethodPara.f(str1);
        System.out.println(Arrays.toString(int1));
        System.out.println(Arrays.toString(int2));
        System.out.println(Arrays.toString(int3));
        System.out.println(Arrays.toString(str1));
        System.out.println(Arrays.toString(str2));
        System.out.println(Arrays.toString(str3));
    }
}


/* Output
[1, 2, 3]
[1, 2, 3]
[1, 2, 3]
[a, b, c]
[a, b, c]
[a, b, c]
*/