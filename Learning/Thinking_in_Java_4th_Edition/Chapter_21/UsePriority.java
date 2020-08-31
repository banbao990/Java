/**
 * @author banbao
 * @comment �޸���ʾ������
 */
import java.util.Random;
import java.util.concurrent.*;
public class UsePriority implements Runnable {
    private static Random rand = new Random(47);
    private static final int range = Thread.MAX_PRIORITY - Thread.MIN_PRIORITY + 1;
    private static int getRandomPriority() {
        return rand.nextInt(range) + Thread.MIN_PRIORITY;
    }
    private final int priority;
    private int cnt = 5;
    private volatile double d; // ��֤û�б������Ż�
    public UsePriority(int priority) {
        this.priority = priority;
    }
    @Override
    public String toString() {
        return Thread.currentThread() + "-" + cnt;
    }
    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
        Thread.yield();
        while(true) {
            // ���ӵ�����
            // ����ʱ���㹻���������ֳ��̵߳������Ľ���
            for(int i = 1; i < 10000000; i++) {
                d += (Math.PI + Math.E) / ((double)i) + 0.1;
                if(i % 1000 == 0)
                Thread.yield();
            }
            System.out.println(this);
            if(--cnt == 0) {
                return;
            }
        }
    }
    public static void main(String...args) {
        // ��ȡ��ǰ�߳����ȼ�
        // Thread.currentThread().getPriority();
        System.out.println(Thread.currentThread());
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0;i < 5; ++i) {
            // ����������ȼ�
            exec.execute(new UsePriority((i==4)?10:1));
            // exec.execute(new UsePriority(1));
        }
        exec.shutdown();
    }
}
/* Output
Thread[main,5,main]
Thread[pool-1-thread-5,10,main]-5
Thread[pool-1-thread-1,1,main]-5
Thread[pool-1-thread-4,1,main]-5
Thread[pool-1-thread-3,1,main]-5
Thread[pool-1-thread-2,1,main]-5
Thread[pool-1-thread-4,1,main]-4
Thread[pool-1-thread-5,10,main]-4
Thread[pool-1-thread-1,1,main]-4
Thread[pool-1-thread-3,1,main]-4
Thread[pool-1-thread-2,1,main]-4
Thread[pool-1-thread-3,1,main]-3
Thread[pool-1-thread-5,10,main]-3
Thread[pool-1-thread-4,1,main]-3
Thread[pool-1-thread-1,1,main]-3
Thread[pool-1-thread-2,1,main]-3
Thread[pool-1-thread-3,1,main]-2
Thread[pool-1-thread-4,1,main]-2
Thread[pool-1-thread-5,10,main]-2
Thread[pool-1-thread-2,1,main]-2
Thread[pool-1-thread-1,1,main]-2
Thread[pool-1-thread-3,1,main]-1
Thread[pool-1-thread-5,10,main]-1
Thread[pool-1-thread-4,1,main]-1
Thread[pool-1-thread-2,1,main]-1
Thread[pool-1-thread-1,1,main]-1
*/