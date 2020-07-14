/**
 * @author banbao
 */
import java.io.*;
public class TestStackTrace{
    public static void main(String...args){
        System.out.println("--------");
        a0();
        System.out.println("--------");
        a1();
        System.out.println("--------");
        a2();
        System.out.println("--------");
        a3();
        System.out.println("--------");
    }

    static void a0() { a1(); }
    static void a1() { a2(); }
    static void a2() { a3(); }
    static void a3() {
        try{
            throw new Exception("Exception from a3().");
        }catch(Exception e){
            for(StackTraceElement ele : e.getStackTrace())
                System.out.println(ele);
        }
    }
}

/*
--------
TestStackTrace.a3(TestStackTrace.java:23)
TestStackTrace.a2(TestStackTrace.java:20)
TestStackTrace.a1(TestStackTrace.java:19)
TestStackTrace.a0(TestStackTrace.java:18)
TestStackTrace.main(TestStackTrace.java:8)
--------
TestStackTrace.a3(TestStackTrace.java:23)
TestStackTrace.a2(TestStackTrace.java:20)
TestStackTrace.a1(TestStackTrace.java:19)
TestStackTrace.main(TestStackTrace.java:10)
--------
TestStackTrace.a3(TestStackTrace.java:23)
TestStackTrace.a2(TestStackTrace.java:20)
TestStackTrace.main(TestStackTrace.java:12)
--------
TestStackTrace.a3(TestStackTrace.java:23)
TestStackTrace.main(TestStackTrace.java:14)
--------
*/