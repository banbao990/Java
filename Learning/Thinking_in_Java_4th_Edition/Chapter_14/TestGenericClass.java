/**
 * @author banbao
 */

public class TestGenericClass {
    public static void main(String...args) {
        Class cl1 = int.class;
        cl1 = double.class; // OK
        
        Class<Integer> cl2 = int.class;
        // cl2 = double.class; // ERROR
        
        // Class<Number> cl3 = int.class; // ERROR 
        // Integer 是 Number 的子类
        // 但是 Class<Integer> 不是 Class<Number>的子类
        
        Class<?> cl3 = int.class; // 通配符(和 Class 等价, 但是更优, 对维护而言)
        cl3 = double.class;
        
        Class<? extends Number> cl4 = int.class; // 限定范围(对比cl3)
        cl4 = double.class;
    }
}
