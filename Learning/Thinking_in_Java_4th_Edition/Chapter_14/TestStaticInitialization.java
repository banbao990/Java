/**
 * @author banbao
 * @comment ���� static ��ʼ���� final
 */

class Init1 {
    static int i;
    static { i = 10; }
    static { System.out.println("Init1 static"); }
    static final int ii = i + 1;
}

class Init2 {
    static int i;
    static final int ii = i + 1;
    static { i = 10; }
    static { System.out.println("Init static"); }
}

class Init3 {
    static int i;
    static { System.out.println("Init3 static"); }
    static final int ii = i + 1;
}

class Init4 {
    static int i;
    static { System.out.println("Init4 static"); }
    static final int ii = 1;
}

class Init5 {
    static int i;
    static { System.out.println("Init5 static"); }
    static int ii = 1; // non final
}

public class TestStaticInitialization {
    public static void seg() { System.out.println("~~~~~"); }
    public static void main(String...args) {
        seg(); System.out.println("Init1:" + Init1.ii); // ������ʼ��
        seg(); System.out.println("Init2:" + Init2.ii); // ������ʼ��
        seg(); System.out.println("Init3:" + Init3.ii); // ������ʼ��
        seg(); System.out.println("Init4:" + Init4.ii); // û�д�����ʼ��
        seg(); System.out.println("Init5:" + Init5.ii); // ������ʼ��
    }
}