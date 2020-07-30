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
ע: VarArgs.javaʹ����δ�����򲻰�ȫ�Ĳ�����
ע: �й���ϸ��Ϣ, ��ʹ�� -Xlint:unchecked ���±��롣
*/

/* Compile(>javac -Xlint:unchecked VarArgs.java)
VarArgs.java:8: ����: [unchecked] ������ vararg ����T�Ķѿ���������Ⱦ
    public static <T> List makeList(T...args) {
                                        ^
  ����, T�����ͱ���:
    T��չ���ڷ��� <T>makeList(T...)��������Object
1 ������
*/

/* Output
6:[test, string, for, the, class, VarArgs.]
*/