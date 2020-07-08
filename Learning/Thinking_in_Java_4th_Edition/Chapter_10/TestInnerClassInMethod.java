/**
 * @author banbao
 */

interface Base{
    void func();
}

class Outer{
    public Base func(){
        // ���������ӷ���Ȩ���޶���( public,private ������)
        class Inner implements Base{
            public void func(){
                System.out.println("Inner1:func");
            }
        }
        return new Inner();
    }
    
    public void test(){
        // new Inner().func();
        // ����: �Ҳ�������
    }
}

public class TestInnerClassInMethod{
    public static void print(){
        System.out.println("-----");
    }
    
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