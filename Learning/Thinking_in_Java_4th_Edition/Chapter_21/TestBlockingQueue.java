/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.*;
class TBQTask implements Runnable {
    public final int id;
    public TBQTask(int id) { this.id = id; }
    @Override
    public void run() {
        System.out.println("Task " + id + " Run Begin!");
        try{
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Task " + id + " Run End!");
    }
}

class TBQQueue implements Runnable {
    public BlockingQueue<TBQTask> bq;
    public TBQQueue(BlockingQueue<TBQTask> bq) {
        this.bq = bq;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                TBQTask task = bq.take();
                task.run();
            }
        } catch(InterruptedException e) {
            System.out.println("Interrupted!");
        }
    }
    public void add(TBQTask task) {
        try {
            bq.put(task);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class TestBlockingQueue {
    public static void main(String...args) {
        TBQQueue bq = new TBQQueue(new ArrayBlockingQueue<>(3));
        Thread t = new Thread(bq);
        t.start();
        for(int i = 0;i < 5; ++i) {
            bq.add(new TBQTask(i));
        }
        try{
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        t.interrupt();
    }
}


/* Output
*/