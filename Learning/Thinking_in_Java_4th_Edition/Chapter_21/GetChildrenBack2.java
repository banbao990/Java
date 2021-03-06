/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.*;

enum Place {
    DALABA, ZAOLANGCHI, PIAOLIU;
    public Place next() {
        if(this.ordinal() + 1 == Place.values().length) return null;
        return Place.values()[this.ordinal() + 1];
    }
}

class GCBChild2 implements Runnable {
    private static Random rand = new Random(47);
    private static int cnt = 0;
    final int ID = ++cnt;
    private CyclicBarrier cdl;
    public GCBChild2(CyclicBarrier cdl) {
        this.cdl = cdl;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                try{
                    TimeUnit.SECONDS.sleep(rand.nextInt(3));
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.format("Child %d is Waiting!\n", ID);
                cdl.await();
            }
        } catch(InterruptedException e) {
            // A legitimate way to exit
            System.out.format("Child %d is Lost!\n");
        } catch(BrokenBarrierException e) {
            // This one we want to know about
            throw new RuntimeException(e);
        }
    }
}

public class GetChildrenBack2 {
    public static void main(String...args) {
        new GetChildrenBack2(10, 3);
    }
    // constructor 
    public GetChildrenBack2(final int size, final int place) {
        ExecutorService exec = Executors.newCachedThreadPool();
        CyclicBarrier cdl = new CyclicBarrier(size, new Runnable() {
            Place place = Place.DALABA;
            @Override
            public void run() {
                Place newPlace = place.next();
                if(newPlace == null) {
                    exec.shutdownNow();
                    System.out.format("Boat : Coming Back!\n");
                } else {
                    System.out.format(
                        "Boat : " 
                        + place + " -> " + newPlace 
                        + "!\n");
                    place = newPlace;
                }
            }
        });
        for(int i = 0;i < size; ++i){
            exec.execute(new GCBChild2(cdl));
        }
    }
}


/* Output (Sample)
Child 9 is Waiting!
Child 3 is Waiting!
Child 7 is Waiting!
Child 5 is Waiting!
Child 10 is Waiting!
Child 2 is Waiting!
Child 6 is Waiting!
Child 8 is Waiting!
Child 1 is Waiting!
Child 4 is Waiting!
Boat : DALABA -> ZAOLANGCHI!
Child 4 is Waiting!
Child 3 is Waiting!
Child 9 is Waiting!
Child 7 is Waiting!
Child 1 is Waiting!
Child 10 is Waiting!
Child 2 is Waiting!
Child 6 is Waiting!
Child 8 is Waiting!
Child 5 is Waiting!
Boat : ZAOLANGCHI -> PIAOLIU!
Child 4 is Waiting!
Child 9 is Waiting!
Child 1 is Waiting!
Child 3 is Waiting!
Child 5 is Waiting!
Child 2 is Waiting!
Child 6 is Waiting!
Child 10 is Waiting!
Child 8 is Waiting!
Child 7 is Waiting!
Boat : Coming Back!
*/
