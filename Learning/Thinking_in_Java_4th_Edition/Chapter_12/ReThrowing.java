/**
 * @author banbao
 */

import java.io.*;

class E1 extends Exception {}
class E2 extends Exception {}

public class ReThrowing{
    public static void main(String...args){
        try{
            a0(1);
        } catch(Exception e){
            for(StackTraceElement ele : e.getStackTrace())
                System.out.println(ele.getMethodName());
        }
        ////////////////////////////////////////////////
        System.out.println("---------");
        try{
            a0(2);
        } catch(Exception e){
            for(StackTraceElement ele : e.getStackTrace())
                System.out.println(ele.getMethodName());
        }
    }

    static void a0(int kind) throws Exception { 
        try{
            a1(kind);
        } catch(E1 e){
            System.out.println("E1 is caught. ReThrowing!");
            throw e;
        } catch(E2 e){
            System.out.println("E1 is caught. FillInStackTrace!");
            e.fillInStackTrace();
            throw e;
        }
    }
    static void a1(int kind) throws Exception { a2(kind); }
    static void a2(int kind) throws Exception { a3(kind); }
    static void a3(int kind) throws Exception {
        if(kind == 1){
            throw new E1();
        } else if(kind == 2){
            throw new E2();
        }
    }
}

/*
E1 is caught. ReThrowing!
a3
a2
a1
a0
main
---------
E1 is caught. FillInStackTrace!
a0
main
*/