/**
 * @author banbao
 */
import java.io.*;
public class TestExceptionFunc{
    public static void main(String...args){
        try{
            test1();
        } catch (Exception e){
            System.out.println("    e.printStackTrace():");
            e.printStackTrace();
            System.out.println("    e.getMessage():\n"
                + e.getMessage());
            System.out.println("    e.getLocalizedMessage():\n"
                + e.getLocalizedMessage());
            System.out.println("    e.toString():\n" + e);
        }
    }

    static void test1() throws IOException{
        throw new IOException("IOException from test1().");
    }
}

/*
    e.printStackTrace():
java.io.IOException: IOException from test1().
    at TestExceptionFunc.test1(TestExceptionFunc.java:21)
    at TestExceptionFunc.main(TestExceptionFunc.java:8)
    e.getMessage():
IOException from test1().
    e.getLocalizedMessage():
IOException from test1().
    e.toString():
java.io.IOException: IOException from test1().
*/
