/**
 * @author banbao
 * @comment ������
 */

interface Base{
    void func();
}

class Outer{
    public Base func(){
        // ������
        // �Զ�����һ���̳���Base(ʵ��Base�ӿ�)����
        // ��������ת����Base
        return new Base(){
            public void func(){
                System.out.println("Inner1:func");
            }
        };
    }
}

public class TestAnonymousClass{
    public static void main(String...args){
        Base b = new Outer().func();
        b.func();
    }
}

/* 
 * output : 
 * 
 * Inner1:func
 * 
 */