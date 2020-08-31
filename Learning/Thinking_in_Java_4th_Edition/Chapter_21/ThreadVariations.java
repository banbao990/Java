/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.util.concurrent.*;
public class ThreadVariations {
    // 内部类
    private class InnerThread implements Runnable {
        @Override
        public void run() {
            System.out.println("InnerClass Runnable: " + Thread.currentThread());
        }
    }
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        // 匿名类实现 Runnable
        exec.execute(new Runnable(){
            @Override
            public void run() {
                System.out.println("Anonymous Runnable: " + Thread.currentThread());
            }
        });
        // 内部类实现
        exec.execute((new ThreadVariations()).new InnerThread());
        // shutdown
        exec.shutdown();
    }
} 
/* Output
Anonymous Runnable: Thread[pool-1-thread-1,5,main]
InnerClass Runnable: Thread[pool-1-thread-1,5,main]
*/