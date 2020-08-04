/**
 * @author banbao
 * @comment �޸���ʾ������
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
            // ʧ��,��Ϊ Integer û��Ĭ�Ϲ��캯��
            // ����ͨ��,��Ϊ������ڱ��������
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