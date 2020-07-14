/**
 * @author banbao
 */
import java.io.*;
public class TestMultiException{
    public static void main(String...args){
        try{
            test1();
        } catch (IOException e){
            System.out.println("IO:" + e);
        } catch (Exception e){
            System.out.println("E:" + e);
        }
    }

    static void test1() throws IOException{
        throw new IOException();
    }
}

/*
IO:java.io.IOException
*/