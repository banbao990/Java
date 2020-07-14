/**
 * @author banbao
 */
import java.io.*;

class E1 extends Exception{}
class E2 extends Exception{
    E2(){}
    E2(Exception e){
        super(e);
    }
}

public class TestCause{
    public static void main(String...args){
        Exception[] es = new Exception[3];
        es[0] = new Exception();
        es[1] = new E1();
        es[2] = new E2();
        for(Exception e : es){
            try{
                test(e);
            }catch(Exception ec){
                ec.printStackTrace();
            }
            System.out.println("-------");
        }
    }
    static void test(Exception e) throws Exception{
        try {
            a1(e);
        }catch(Exception ec){
            System.out.println("~~~~~origin start~~~~~");
            ec.printStackTrace();
            System.out.println("~~~~~origin end~~~~~");
            if(ec instanceof E1){
                E1 en = new E1();
                en.initCause(ec);
                throw en;
            } else if(ec instanceof E2){
                throw new E2(ec);
            } else{
                throw new Exception(ec);
            }
        }
    }
    static void a1(Exception e) throws Exception { a2(e); }
    static void a2(Exception e) throws Exception { a3(e); }
    static void a3(Exception e) throws Exception {
        if(e instanceof E1){
            throw new E1();
        } else if(e instanceof E2){
            throw new E2();
        } else{
            throw new Exception();
        }
    }
}
/*
~~~~~origin start~~~~~
java.lang.Exception
    at TestCause.a3(TestCause.java:55)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    at TestCause.main(TestCause.java:22)
~~~~~origin end~~~~~
java.lang.Exception: java.lang.Exception
    at TestCause.test(TestCause.java:43)
    at TestCause.main(TestCause.java:22)
Caused by: java.lang.Exception
    at TestCause.a3(TestCause.java:55)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    ... 1 more
-------
~~~~~origin start~~~~~
E1
    at TestCause.a3(TestCause.java:51)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    at TestCause.main(TestCause.java:22)
~~~~~origin end~~~~~
E1
    at TestCause.test(TestCause.java:37)
    at TestCause.main(TestCause.java:22)
Caused by: E1
    at TestCause.a3(TestCause.java:51)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    ... 1 more
-------
~~~~~origin start~~~~~
E2
    at TestCause.a3(TestCause.java:53)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    at TestCause.main(TestCause.java:22)
~~~~~origin end~~~~~
E2: E2
    at TestCause.test(TestCause.java:41)
    at TestCause.main(TestCause.java:22)
Caused by: E2
    at TestCause.a3(TestCause.java:53)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    ... 1 more
-------
*/
