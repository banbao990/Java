/**
 * @author banbao
 */

/** 
 * # windows 下的编译与运行命令
 * # 编译 Work 类
 * javac Work.java
 * # 运行测试函数
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
