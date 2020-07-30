/**
 * @author banbao
 * @comment �޸���ʾ������
 */

import java.util.*;

public class GenericWriting {
    static List<Apple> apples = new ArrayList<Apple>();
    static List<Fruit> fruit = new ArrayList<Fruit>();
    
    // ����
    static <T> void writeExact(List<T> list, T item) {
        list.add(item);
    }
    static void f1() {
        writeExact(apples, new Apple());
        // writeExact(apples, new Fruit()); // ERROR
        /* 
         * ��Ҫ: List<T>,T
         * �ҵ�: List<Apple>,Fruit
         * ԭ��: ���۱���T���в����ݵ����Ʒ�Χ
         *   ��ʽԼ������: Apple
         *   ����: Fruit
         */
        writeExact(fruit, new Apple()); // Java8 OK
        writeExact(fruit, new Fruit());
    }
    
    // ������ͨ���
    static <T> void writeWithWildcard(
        List<? super T> list, T item) {
        list.add(item);
    }
    static void f2() {
        writeWithWildcard(apples, new Apple());
        // writeWithWildcard(apples, new Fruit()); // ERROR
        /*
         * ��Ҫ: List<? super T>,T
         * �ҵ�: List<Apple>,Fruit
         * ԭ��: �ƶ����Ͳ���������
         *   �ƶ�: Fruit
         *   ����: Apple,Object
         */
        writeWithWildcard(fruit, new Apple());
        writeWithWildcard(fruit, new Fruit());
    }
    public static void main(String[] args) { f1(); f2(); }
}
