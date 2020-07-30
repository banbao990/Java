/**
 * @author banbao
 * @comment 修改自示例代码
 */

class ClassAsFactory<T> {
    T x;
    public ClassAsFactory(Class<T> kind) {
        try {
            x = kind.newInstance();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Employee {}

public class InstantiateGenericType {
    public static void main(String[] args) {
        ClassAsFactory<Employee> fe =
            new ClassAsFactory<Employee>(Employee.class);
        System.out.println("ClassAsFactory<Employee> succeeded");
        try {
            // 失败,因为 Integer 没有默认构造函数
            // 编译通过,因为这个不在编译器检查
            ClassAsFactory<Integer> fi =
                new ClassAsFactory<Integer>(Integer.class);
        } catch(Exception e) {
            System.out.println("ClassAsFactory<Integer> failed");
        }
    }
} 
/* Output:
ClassAsFactory<Employee> succeeded
ClassAsFactory<Integer> failed
*/
