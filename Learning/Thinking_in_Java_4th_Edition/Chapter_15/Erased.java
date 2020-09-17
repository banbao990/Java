/**
 * @author banbao
 * @comment 修改自示例代码
 */

public class Erased<T> {
    private final int SIZE = 100;
    public void f(Object arg) {

        // if(arg instanceof T) {}
        /*
         * 错误: instanceof 的泛型类型不合法
         *     if(arg instanceof T) {}
         *                       ^
        */

        // T var = new T();
        /*
         * 错误: 意外的类型
         * T var = new T();
         *             ^
        */

        // T[] array1 = new T[SIZE];
        /*
         * 错误: 创建泛型数组
         * T[] array1 = new T[SIZE];
         *              ^
        */


        T[] array2 = (T[])new Object[SIZE];
        //  警告: [unchecked] 未经检查的转换

    }
    public static void main(String...args) {
        new Erased<String>().f(new Object());
    }
}
/* Output
*/
