// ������

abstract class Base{
    // �޲ι��캯��
    Base(){ 
        System.out.println("No args!");
    }
    // ���ι��캯��
    Base(int x){
        System.out.println("One arg!");
    }
    // ���Ժ���
    void func(){
        System.out.println("Blank!");
    }
}

class Outer{
    public Base func(){
        // ������
        // �޲ι��캯��
        return new Base(){
            @Override
            public void func(){
                System.out.println("Func, no args!");
            }
        };
    }
    
    public Base func(int x){
        // ������
        // ���ι��캯��
        return new Base(x){
            public void func(){
                // x = x + 1;
                // ����: ���ڲ������õı��ر������������ձ�����ʵ���ϵ����ձ���
                System.out.println("Func, one arg!");
            }
        };
    }
}

public class TestAnonymousClassConstructor{
    public static void main(String...args){
        Base b = new Outer().func();
        b.func();
        b = new Outer().func(10);
        b.func();
    }
}

/*
 * output:
 * 
 * No args!
 * Func, no args!
 * One arg!
 * Func, one arg!
 * 
 */
