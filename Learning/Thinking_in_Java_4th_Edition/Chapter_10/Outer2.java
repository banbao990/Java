/**
 * @author banbao
 * @comment ������ڲ���͸�����ڲ���λ�ڲ�ͬ�������ռ�
 *          ������ڲ��಻�ܱ�����
 */

class Outer{
    // ���ڲ��Ե��ڲ���
    private Inner inner;
    Outer(){}
    public Outer(int nouse){
        inner = new Inner(0);
    }
    
    class Inner{
        Inner(){}
        Inner(int nouse){
            System.out.println("Outer.Inner");
        }
        public void test(){
            System.out.println("Outer.Inner:test");
        }
    }
    
    // ���Զ�̬
    public void test(){
        a();
    }
    // ��̬
    public void a(){
        System.out.println("Outer:a");
    }
}

public class Outer2 extends Outer{
    public Outer2(){}
    public Outer2(int nouse){
        super(nouse);
    }
    class Inner{
        public Inner(){}
        public Inner(int nouse){
            System.out.println("Outer2.Inner");
        }
        public void test(){
            System.out.println("Outer2.Inner:test");
        }
    }
    
    // ����
    @Override
    public void a(){
        System.out.println("Outer2:a");
    }
    public static void main(String...args){
        Outer2 o2 = new Outer2();
        // ��̬����
        System.out.println("-----");
        o2.test();
        // ���ܸ����ڲ������
        System.out.println("-----");
        new Outer2(0);
        // ӵ�ж��������ռ����
        System.out.println("-----");
        Outer o1 = new Outer();
        Outer.Inner i1 = o1.new Inner();
        Outer2.Inner i2 = o2.new Inner();
        i1.test();
        i2.test();
    }
}
/*
 * output
 * 
 * -----
 * Outer2:a
 * -----
 * Outer.Inner
 * -----
 * Outer.Inner:test
 * Outer2.Inner:test
 */