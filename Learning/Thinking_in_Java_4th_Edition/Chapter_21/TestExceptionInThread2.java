/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
public class TestExceptionInThread2 {
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            exec.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("ERROR");
            }});
        } catch (RuntimeException e) {
            System.err.println("Main Catch!");
        } finally {
            exec.shutdown();
        }
    }
}

/* Output
Exception in thread "pool-1-thread-1" java.lang.RuntimeException: ERROR
    at TestExceptionInThread2$1.run(TestExceptionInThread2.java:15)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
    at java.lang.Thread.run(Thread.java:748)
*/