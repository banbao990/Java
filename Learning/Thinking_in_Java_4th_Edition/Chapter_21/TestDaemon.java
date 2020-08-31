/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
public class TestDaemon implements Runnable {
    @Override
    public void run() {
        System.out.println(
            Thread.currentThread().toString() + " Daemon:"
            + Thread.currentThread().isDaemon()
        );
    }
    public static void main(String...args) {
        Thread daemonThread = new Thread(new TestDaemon());
        // daemonThread.setDaemon(false);
        daemonThread.setDaemon(true);
        daemonThread.start();
    }
}


/* Output(true)
*/

/* Output(false)
Thread[Thread-0,5,main]
false
*/