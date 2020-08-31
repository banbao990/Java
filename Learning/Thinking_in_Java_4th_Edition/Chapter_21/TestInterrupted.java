/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.*;

public class TestInterrupted {
    public static void main(String...args) {
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    System.in.read();
                } catch(Exception e) {
                    System.out.println("Interrupted during tellers.put()");
                }
                System.out.println(Thread.interrupted());
                System.out.println(Thread.interrupted());
                System.out.println(Thread.interrupted());
            }
        });
        t.start();
        try{
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e){
            System.out.println("main:Sleep Interrupted(1)"); 
        }
        t.interrupt();
    }
}


/* Output
true
false
false
*/