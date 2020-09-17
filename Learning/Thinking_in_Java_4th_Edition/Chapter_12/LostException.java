/**
 * @author banbao
 */
import java.io.*;
public class LostException{
    public static void main(String...args){
        try {
            test();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    static void test() { 
        try {
            throw new Exception();
        } finally{
            return;
        }
    }
}
/*
*/
