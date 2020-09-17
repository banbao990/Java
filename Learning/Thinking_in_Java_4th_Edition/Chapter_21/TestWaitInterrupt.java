/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.*;

class TWIRunnable implements Runnable {
    @Override
    public void run() {
        try{
            test();
        } catch(InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
    }
    
    private synchronized void test() 
        throws InterruptedException {
        wait();
    }
}

public class TestWaitInterrupt {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new TWIRunnable());
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow(); // Interrupt all tasks
    }
} 
/* Output
Exiting via interrupt
*/
