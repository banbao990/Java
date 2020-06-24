/**
 * @author banbao
 */

class Tank{

    // �Ƿ�ҩ����
    private boolean full;

    // ���캯��
    Tank(boolean full){
        this.full = full;
    }

    // ����
    public boolean attack(){
        if(!this.full){
            System.out.println("ERROR:The tank is lack of ammunition!");
            return false;
        }
        this.full = false;
        return true;
    }

    // finalize
    @Override
    protected void finalize(){
        if(this.full)
            System.out.println("ERROR:The tank has enough ammunition!");
        // super.finalize(); // Object
        System.out.println("GC");
    }
}


public class Test01{
    public static void main(String[] args){
        // test01();
        test02();
    }

    public static void test01(){
        System.out.println("---test01---");
        Tank tank1 = new Tank(true);
        Tank tank2 = new Tank(false);
        Tank tank3 = new Tank(true);
        Tank tank4 = new Tank(false);
        tank1.attack();
        tank2.attack();
        System.gc();
    }

    public static void test02(){
        System.out.println("---test02---");
        new Tank(true).attack();
        new Tank(false).attack();
        new Tank(true);
        new Tank(false);
        System.gc();
    }

}

/*
 * 
 * output:
 * �ֱ�ע�� test01,test02
 * case1 : ע�� test02, ֻ�� test01(��ǰ�޷�ȷ��tank1,2,3,4�Ƿ�Ϊ����)
 * ---test01---
 * ERROR:The tank is lack of ammunition!
 * 
 * case2 : ע�� test01, ֻ�� test02
 * ---test02---
 * ERROR:The tank is lack of ammunition!
 * GC
 * ERROR:The tank has enough ammunition!
 * GC
 * GC
 * GC
 * 
 */