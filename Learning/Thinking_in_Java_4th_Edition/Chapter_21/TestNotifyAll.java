/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.*;

class TNARun1 implements Runnable {
    public final int ID;
    public final boolean shouldNotify;
    public TNARun1(int ID, boolean shouldNotify) {
        this.ID = ID; 
        this.shouldNotify = shouldNotify; 
    }
    @Override
    public void run() {
        synchronized(this) {
            try{
                if(shouldNotify) {
                    for(int i = 0;i < 5; i++) {
                        wait(1000);
                        notify();
                    }
                } else {
                    wait();
                }
                System.out.println(this + " Run!");
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public String toString() {
        return this.getClass().getName() + "@" + ID;
    }
}

class TNARun2 implements Runnable {
    TNARun1 run;
    public final int ID;
    public final boolean shouldNotify;
    public TNARun2(int ID, TNARun1 run, boolean shouldNotify){
        this.ID = ID;
        this.run = run;
        this.shouldNotify = shouldNotify; 
    }
    @Override
    public void run() {
        synchronized(run) {
            try{
                if(shouldNotify) {
                    run.wait(1000); // 表示让这个唤醒延迟在所有任务开始执行之后
                    run.notifyAll(); // notifyAll()
                } else {
                    run.wait();
                }
                System.out.println(this + " Run!");
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public String toString() {
        return this.getClass().getName() + "@" + ID;
    }
}

public class TestNotifyAll {
    public static void main(String...args) {
        // test1(); // notifyAll 测试只有和当前锁相关联的任务会被唤醒
        test2(); // notify 每次只唤醒一个任务
    }
    private static void test1() {
        ExecutorService exec = Executors.newCachedThreadPool();
        TNARun1 run1 = new TNARun1(1, false);
        TNARun1 run2 = new TNARun1(2, false);
        TNARun2 run3 = new TNARun2(3, run1, true);
        exec.execute(run1);
        exec.execute(run2);
        exec.execute(run3);
        exec.shutdown();
    }
    private static void test2() {
        ExecutorService exec = Executors.newCachedThreadPool();
        TNARun1 run1 = new TNARun1(1, true);
        TNARun2[] run2s = new TNARun2[] {
            new TNARun2(2, run1, false),
            new TNARun2(3, run1, false),
            new TNARun2(4, run1, false),
            new TNARun2(5, run1, false),
            new TNARun2(6, run1, false),
        };
        for(int i = 0;i < run2s.length; ++i) {
            exec.execute(run2s[i]);
        }
        try{
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        exec.execute(run1);
        exec.shutdown();
    }
}

/* Output (test1) (Sample)
TNARun2@3 Run!
TNARun1@1 Run!
*/

/* Output (test2) (Sample)
TNARun2@2 Run!
TNARun2@3 Run!
TNARun2@4 Run!
TNARun2@5 Run!
TNARun1@1 Run!
TNARun2@6 Run!
*/