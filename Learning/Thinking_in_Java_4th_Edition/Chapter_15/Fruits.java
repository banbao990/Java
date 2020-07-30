/**
 * @author banbao
 * @comment �޸���ʾ������
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

class Fruit {}
class Apple extends Fruit {}
public class Fruits {
    public static void main(String...args) {
        // 1. ����
        Fruit[] f = new Apple[10]; // OK
        // ����ʱ�����;��Ǿ���� Apple
        
        // 2. ����
        // List<Fruit> list = new ArrayList<Apple>();
        // ����: �����ݵ�����: ArrayList<Apple>�޷�ת��ΪList<Fruit>
        
        // 3. ͨ���
        List<? extends Fruit> list = new ArrayList<Apple>();
        // ??? CE
        // list.add(new Apple()); // ��������Ϊ E(����ռλ��)
        // list.add(new Fruit());
        // list.add(new Object());
        list.add(null); // û��
        
        // 4. ����Ϊ Object
        List<? extends Fruit> flist =
            Arrays.asList(new Apple());
        Apple a = (Apple)flist.get(0);  // No warning
        flist.contains(new Apple());    // Argument is 'Object'
        flist.indexOf(new Apple());     // Argument is 'Object'
    }
}