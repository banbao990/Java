/**
 * @author banbao
 */

public class TestRecursion {
    @Override
    public String toString(){
        // this ����� toString() �������µݹ�
        // return "Addr:" + this;
        return "Addr:" + super.toString();
    }
    
    public static void main(String...arg){
        TestRecursion tr = new TestRecursion();
        System.out.println(tr.toString());
    }
}
/*
Addr:TestRecursion@15db9742
*/