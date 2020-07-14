/**
 * @author banbao
 */
import java.io.*;

class E1 extends Exception{
    @Override
    public String toString(){ return "Important!"; }
}

class E2 extends Exception{
    @Override
    public String toString(){ return "Negligible!"; }
}

public class LostException2{
    public static void main(String...args){
        try {
            test();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    static void test() throws Exception { 
        try {
            throw new E1();
        } finally{
            throw new E2();
        }
    }
}
/*
Negligible!
*/