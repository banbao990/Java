/**
 * @author banabo
 * @comment �޸���ʾ������
 */

class WithInner{
    int testOuter = 11;
    class Inner{
        int testInner = 22;
    }
}

public class InheritInner extends WithInner.Inner{
    // InheritInner(){}
    // ����: ��Ҫ����WithInner.Inner�ķ��ʵ��
    InheritInner(WithInner wi){
        wi.super(); // ��Ҫ��ôʹ�ò��ܹ�ʵ�ּ̳��ڲ���
    }
    public static void main(String...args){
        WithInner wi = new WithInner();
        InheritInner ii = new InheritInner(wi);
        // System.out.println(ii.testOuter);
        // ����: �Ҳ�������
        // System.out.println(((WithInner.Inner)ii).testOuter);
        // ����: �Ҳ�������
        System.out.println(ii.testInner);
    }
}