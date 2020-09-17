/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
class TDUEH1 implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException("RTException from TDUEH1");
    }
}

class TDUEH2 implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException("RTException from TDUEH2");
    }
}

class MyUncaughtExceptionHandler1 implements
    Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("default(Handler1) : " + e);
    }
}

class MyUncaughtExceptionHandler2 implements
    Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("Handler2 : " + e);
    }
}

public class TestDefaultUncaughtExceptionHandler {
    public static void main(String...args) {
        // set deault
        Thread.setDefaultUncaughtExceptionHandler(
            new MyUncaughtExceptionHandler1());
        // 有专有的 Exception Handler
        Thread t1 = new Thread(new TDUEH1());
        t1.setUncaughtExceptionHandler(
            new MyUncaughtExceptionHandler2());
        t1.start();
        // 没有专有的 Exception Handler
        new Thread(new TDUEH2()).start();
    }
}


/* Output
Handler2 : java.lang.RuntimeException: RTException from TDUEH1
default(Handler1) : java.lang.RuntimeException: RTException from TDUEH2
*/
