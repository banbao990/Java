/**
 * @author banbao
 */

class Base {}
class Derived extends Base {}

public class TestGenericClassNewInstance {
    public static void main(String...args) throws Exception {
        // ����
        Class<Derived> cl1 = Derived.class;
        Derived instance1 = cl1.newInstance();
        
        // ��ʹ�÷���
        Class cl2 = Derived.class;
        // Derived instance2 = cl2.newInstance();
        // ����: �����ݵ�����: Object�޷�ת��ΪDerived
        
        // ģ��������,�����Ǿ����
        Class<? super Derived> cl3 = cl1.getSuperclass();
        // Class<Base> cl4 = cl1.getSuperclass();
        /* ����: �����ݵ�����: Class<CAP#1>�޷�ת��ΪClass<Base>
         * Class<Base> cl4 = cl1.getSuperclass();
         * ����, CAP#1�������ͱ���:
         * CAP#1��? super Derived�Ĳ�����չObject �� Derived
         */
    }
}
