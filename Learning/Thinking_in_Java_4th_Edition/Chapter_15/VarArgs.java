/**
 * @author banbao
 */

import java.util.List;
import java.util.ArrayList;

public class VarArgs {
    public static <T> List makeList(T...args) {
        List<T> list = new ArrayList<>();
        for(T t : args) {
            list.add(t);
        }
        return list;
    }
    public static void main(String...args) {
        List list = VarArgs.makeList(
            "test string for the class VarArgs.".split(" ")
        );
        System.out.println(list.size() + ":" + list);
    }
}
/* Compile(javac VarArgs.java)
注: VarArgs.java使用了未经检查或不安全的操作。
注: 有关详细信息, 请使用 -Xlint:unchecked 重新编译。
*/

/* Compile(>javac -Xlint:unchecked VarArgs.java)
VarArgs.java:8: 警告: [unchecked] 参数化 vararg 类型T的堆可能已受污染
    public static <T> List makeList(T...args) {
                                        ^
  其中, T是类型变量:
    T扩展已在方法 <T>makeList(T...)中声明的Object
1 个警告
*/

/* Output
6:[test, string, for, the, class, VarArgs.]
*/
