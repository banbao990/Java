/**
 * @author banbao
 */
import java.io.*;
public class TestFinally{
    public static void main(String...args){
        boolean[] test = {true, false};
        /////////// Exception //////////
        for(boolean t : test){
            System.out.println("-------");
            try {
                a1(t);
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                System.out.println("Reach \"finally\"!");
            }
        }
    }

    static void a1(boolean t) throws Exception { a2(t); }
    static void a2(boolean t) throws Exception { a3(t); }
    static void a3(boolean t) throws Exception {
        System.out.println("Exception:" + t);
        if(t){
            throw new Exception();
        }
    }
}

/*
-------
Exception:true
java.lang.Exception
    at TestFinally.a3(TestFinally.java:26)
    at TestFinally.a2(TestFinally.java:22)
    at TestFinally.a1(TestFinally.java:21)
    at TestFinally.main(TestFinally.java:12)
Reach "finally"!
-------
Exception:false
Reach "finally"!
*/