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
        // Integer �� Number ������
        // ���� Class<Integer> ���� Class<Number>������
        
        Class<?> cl3 = int.class; // ͨ���(�� Class �ȼ�, ���Ǹ���, ��ά������)
        cl3 = double.class;
        
        Class<? extends Number> cl4 = int.class; // �޶���Χ(�Ա�cl3)
        cl4 = double.class;
    }
}
