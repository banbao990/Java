/**
 * @author banbao
 * @comment �޸���ʾ������
 */

public class Erased<T> {
    private final int SIZE = 100;
    public void f(Object arg) {

        // if(arg instanceof T) {}
        /*
         * ����: instanceof �ķ������Ͳ��Ϸ�
         *     if(arg instanceof T) {}
         *                       ^
        */

        // T var = new T();
        /*
         * ����: ���������
         * T var = new T();
         *             ^
        */

        // T[] array1 = new T[SIZE];
        /*
         * ����: ������������
         * T[] array1 = new T[SIZE];
         *              ^
        */


        T[] array2 = (T[])new Object[SIZE];
        //  ����: [unchecked] δ������ת��

    }
    public static void main(String...args) {
        new Erased<String>().f(new Object());
    }
}
/* Output
*/
