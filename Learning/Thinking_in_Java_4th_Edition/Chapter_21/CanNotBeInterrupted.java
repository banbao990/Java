/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;

class CNBIRunnable implements Runnable {
    private Lock lock = new ReentrantLock();
    public CNBIRunnable() {
        lock.lock();
    }
    public void run() {
        lock.lock();
        System.out.println("Run");
    }
}

public class CanNotBeInterrupted {
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new CNBIRunnable());
        exec.shutdownNow();
    }
}