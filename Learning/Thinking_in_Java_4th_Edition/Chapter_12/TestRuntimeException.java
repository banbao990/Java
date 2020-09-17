/**
 * @author banbao
 */
import java.io.*;
public class TestRuntimeException{
    public static void main(String...args){
        a1();
    }

    static void a1() { a2(); }
    static void a2() { a3(); }
    static void a3() {
        throw new RuntimeException();
    }
}

/*
Exception in thread "main" java.lang.RuntimeException
    at TestRuntimeException.a3(TestRuntimeException.java:13)
    at TestRuntimeException.a2(TestRuntimeException.java:11)
    at TestRuntimeException.a1(TestRuntimeException.java:10)
    at TestRuntimeException.main(TestRuntimeException.java:7)
*/
