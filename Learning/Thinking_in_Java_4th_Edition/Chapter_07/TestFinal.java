/**
 * @author banbao
 */

class A{
    final int a;
    A(int a){
        // �ڹ��캯����Ϊ final ���� a ��ֵ
        this.a = a;
    }

    public void change(int a){
        // this.a = a;
        // ����: �޷�Ϊ���ձ��� a ����ֵ
    }

    public void func(){
        System.out.println("a");
    }
}

class TestFinal{
    public static void main(String[] args){
        System.out.println(new A(10).a);
    }
}
