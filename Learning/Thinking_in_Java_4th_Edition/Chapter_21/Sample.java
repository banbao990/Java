/**
 * @author banbao
 * @comment modified from example code
 */

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.io.*;

public class Sample {
    public static void main(String...args) {
        try{
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e){
            System.out.println("main:Sleep Interrupted");
        }
    }
}


/* Output
*/
