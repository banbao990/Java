/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
class DaemonDoNotRunFinally1 implements Runnable {
    @Override
    public void run() {
        try{
            Thread.sleep(1000);
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            System.out.println("Daemon run finally!");
        }
    }
}
public class DaemonDoNotRunFinally {
    public static void main(String...args) {
        Thread daemonThread = new Thread(new DaemonDoNotRunFinally1());
        daemonThread.setDaemon(true);
        daemonThread.start();
    }
}


/* Output
*/