/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;

class CBIRunnable implements Runnable {
    private Lock lock = new ReentrantLock();
    public CBIRunnable() {
        lock.lock();
    }
    public void run() {
        try {
            lock.lockInterruptibly();
            System.out.println("Run");
        } catch(InterruptedException e) {
            System.out.println("Interrupted while waiting!");
        }
    }
}

public class CanBeInterrupted {
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        Thread t = new Thread(new CBIRunnable());
        exec.execute(t);
        try{
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        exec.shutdownNow();
    }
}
/* Output 
Interrupted while waiting!
*/