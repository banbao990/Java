/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.*;

class GCBChild implements Runnable {
    private static Random rand = new Random(47);
    private static int cnt = 0;
    final int ID = ++cnt;
    private CountDownLatch cdl;
    public GCBChild(CountDownLatch cdl) {
        this.cdl = cdl;
    }
    @Override
    public void run() {
        try{
            TimeUnit.SECONDS.sleep(rand.nextInt(10));
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.format("Child %d is Coming Back!\n", ID);
        cdl.countDown();
    }
}

class GCBBoat implements Runnable {
    private static int cnt = 0;
    final int ID = ++cnt;
    private CountDownLatch cdl;
    public GCBBoat(CountDownLatch cdl) {
        this.cdl = cdl;
    }
    @Override
    public void run() {
        try{
            cdl.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.format("Boat %d is Coming Back!\n", ID);
    }
}

public class GetChildrenBack {
    public static void main(String...args) {
        int size = 10;
        ExecutorService exec = Executors.newCachedThreadPool();
        CountDownLatch cdl = new CountDownLatch(size);
        for(int i = 0;i < size; ++i){
            exec.execute(new GCBChild(cdl));
        }
        exec.execute(new GCBBoat(cdl));
        exec.shutdown();
    }
}


/* Output (Sample)
Child 8 is Coming Back!
Child 4 is Coming Back!
Child 5 is Coming Back!
Child 9 is Coming Back!
Child 3 is Coming Back!
Child 2 is Coming Back!
Child 10 is Coming Back!
Child 1 is Coming Back!
Child 7 is Coming Back!
Child 6 is Coming Back!
Boat 1 is Coming Back!
*/