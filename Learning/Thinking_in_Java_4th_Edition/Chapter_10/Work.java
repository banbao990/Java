/**
 * @author banbao
 */

/** 
 * # windows �µı�������������
 * # ���� Work ��
 * javac Work.java
 * # ���в��Ժ���
 * java Work$Tester
*/
public class Work{
    public void func(){
        System.out.println("test func()");
    }
    public static class Tester{
        public static void main(String...args){
            Work work = new Work();
            work.func();
        }
    }
}


/*
 * output:
 * 
 * test func()
 * 
*/