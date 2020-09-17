/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.*;
class Exe21Run1 implements Runnable {
    public volatile boolean shouldNotify = false;
    @Override
    public void run(){
        synchronized(this) {
            while(!shouldNotify) {
                try{
                    shouldNotify = true;
                    System.out.println("Waiting!");
                    notify(); // necessary
                    wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println("Running!");
        }
    }
}

class Exe21Run2 implements Runnable {
    public Exe21Run1 run;
    public Exe21Run2(Exe21Run1 run) {
        this.run = run;
    }
    @Override
    public void run(){
        // try{
            // TimeUnit.SECONDS.sleep(1);
        // } catch (InterruptedException e){
            // e.printStackTrace();
        // }
        synchronized(run) {
            while(!run.shouldNotify) {
                try{
                    run.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println("Excecute notifyAll()!");
            run.notifyAll();
            System.out.println("Excecute notifyAll() successfully!");
        }
    }
}

public class Exe21 {
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        Exe21Run1 run1 = new Exe21Run1();
        Exe21Run2 run2 = new Exe21Run2(run1);
        exec.execute(run2);
        exec.execute(run1);
        exec.shutdown();
    }
}


/* Output
*/
