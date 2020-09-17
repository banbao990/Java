/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;

class ThreadFromDaemonThread1 implements Runnable {
    @Override
    public void run() {
        System.out.println(
            Thread.currentThread() + " "
            + Thread.currentThread().isDaemon()
        );
    }
}

public class ThreadFromDaemonThread implements Runnable {
    @Override
    public void run() {
        new Thread(new ThreadFromDaemonThread1()).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String...args) {
        Thread daemonThread = new Thread(new ThreadFromDaemonThread());
        daemonThread.setDaemon(true);
        daemonThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


/* Output
Thread[Thread-1,5,main] true
*/
