/**
 * @author banbao
 */

public class TestStatic{
    public static void main(String[] args){
        System.out.println("-----1");
        // A.c = 0; // ע�����(1)
        // ע�����(1)��֤���� static ���������û������������ɶ��ᴥ����ļ���
        System.out.println("-----2");
        new A(); // ���ȼ������ static ����
        System.out.println("-----3");
        new A(); // û�����, static �����ֻ��ִ�г�ʼ��һ��
    }
}

class B{
    static{
        System.out.println("B:static test!"); // ���˳��: 1
    }
}

class A extends B{
    static{
        System.out.println("A:static test 1!"); // ���˳��: 2
    }
    static{
        System.out.println("A:static test 2!"); // ���˳��: 3(˳�����)
    }
    static int c = 0;
    static {
        int d = c + 1;
        // int b = a + 1; // ����: �Ƿ�ǰ������
    }
    static int a = 0;
    static int d = func(); // static �����ļ������� static �ֶ�
    static int func(){
        return 0;
    }
}

/* 
 * output:
 *
 *
 * �� "ע�����(1)" ע��
 * -----1
 * -----2
 * B:static test!
 * A:static test 1!
 * A:static test 2!
 * -----3
 *
 *
 * ���� "ע�����(1)" ע��
 * -----1
 * B:static test!
 * A:static test 1!
 * A:static test 2!
 * -----2
 * -----3
 */