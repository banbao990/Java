/**
 * @author banbao
 * @comment �޸���ʾ������
 */
import java.util.concurrent.*;
public class ThreadVariations {
    // �ڲ���
    private class InnerThread implements Runnable {
        @Override
        public void run() {
            System.out.println("InnerClass Runnable: " + Thread.currentThread());
        }
    }
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        // ������ʵ�� Runnable
        exec.execute(new Runnable(){
            @Override
            public void run() {
                System.out.println("Anonymous Runnable: " + Thread.currentThread());
            }
        });
        // �ڲ���ʵ��
        exec.execute((new ThreadVariations()).new InnerThread());
        // shutdown
        exec.shutdown();
    }
} 
/* Output
Anonymous Runnable: Thread[pool-1-thread-1,5,main]
InnerClass Runnable: Thread[pool-1-thread-1,5,main]
*/