/**
 * @author banbao
 */

class Tank{

    // 是否弹药充足
    private boolean full;

    // 构造函数
    Tank(boolean full){
        this.full = full;
    }

    // 进攻
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
 * 分别注释 test01,test02
 * case1 : 注释 test02, 只有 test01(当前无法确认tank1,2,3,4是否为垃圾)
 * ---test01---
 * ERROR:The tank is lack of ammunition!
 * 
 * case2 : 注释 test01, 只有 test02
 * ---test02---
 * ERROR:The tank is lack of ammunition!
 * GC
 * ERROR:The tank has enough ammunition!
 * GC
 * GC
 * GC
 * 
 */
