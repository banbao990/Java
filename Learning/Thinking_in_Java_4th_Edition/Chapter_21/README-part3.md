# Chapter_21 ����(part3)

> [��Ŀ¼](../README.md)

---

[TOC]

---

+ [part1](README.md)
+ [part2](README-part2.md)
+ [part3](README-part3.md)



## 21.5 �߳�֮���Э��

+ ʹ�õ����ԣ�**����**



### 21.5.1 wait() �� notifyAll()

+ ���� `Object` ʵ�ֵ�
+ �ڵȴ���ʱ����п�ѭ����**æ�ȴ�**
    + ������ `CPU` ����ʹ�÷�ʽ
+ `wait()` ������������յ� `notify()/notifyAll()` ֮��ָ�
+ `sleep()/yield()` ���ͷ���
+ `wait()` �ͷ���
+ `wait(ms)`��
    + `wait()` �ڼ��ͷ���
    + ����ͨ�� `notify()/notifyAll()` ����ʱ�䵽�ڴ� `wait()` �лָ�
+ `wait()` �����޵ȴ�
+ ����ֻ����ͬ�����Ʒ�������ͬ�����ƿ������ `wait(),notify(),notifyAll()`
    + ����ͨ�����뵫��������ʱ�쳣
    + ˵���ڵ��� `wait(),notify(),notifyAll()` ʱ����ӵ�ж������

```java
public class Sample {
    public static void main(String...args) {
        try{
            new Sample().wait();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
```

```output
Exception in thread "main" java.lang.IllegalMonitorStateException
    at java.lang.Object.wait(Native Method)
    at java.lang.Object.wait(Object.java:502)
    at Sample.main(Sample.java:4)
```

+ `sleep()` �����ڷ�ͬ������������ã�**��Ϊ����Ҫ������**
+ һ���򵥵�ʾ�������������׹�
+ [WaxOMatic](../Example_Code/concurrency/waxomatic/WaxOMatic.java)

```java
// ���ִ���
class Car {
    private boolean waxOn = false;
    public synchronized void waxed() {
        waxOn = true; // Ready to buff
        notifyAll();
    }
    public synchronized void buffed() {
        waxOn = false; // Ready for another coat of wax
        notifyAll();
    }
    public synchronized void waitForWaxing()
        throws InterruptedException {
        while(waxOn == false)
            wait();
    }
    public synchronized void waitForBuffing()
        throws InterruptedException {
        while(waxOn == true)
            wait();
    }
}

class WaxOn implements Runnable {
    private Car car;
    public WaxOn(Car c) { car = c; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                printnb("Wax On! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        } catch(InterruptedException e) {
            print("Exiting via interrupt");
        }
        print("Ending Wax On task");
    }
}
```

+ `wait()` ���������Ա� `shutdownNow()` �ж�
+ [TestWaitInterrupt](TestWaitInterrupt.java)

```java
import java.util.concurrent.*;
import java.util.*;

class TWIRunnable implements Runnable {
    @Override
    public void run() {
        try{
            test();
        } catch(InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
    }

    private synchronized void test()
        throws InterruptedException {
        wait();
    }
}

public class TestWaitInterrupt {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new TWIRunnable());
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow(); // Interrupt all tasks
    }
}
/* Output
Exiting via interrupt
*/
```

+ **α����**
    + ����һ�ִ� `wait()` �г���ķ�ʽ��**ƽ̨����**��
    + һ���߳̿��Թ����ֹͣ������������Ҫ `notify()` �ȷ�ʽ
+ ʹ��һ������Ȥ��������Χ `wait()`
    + ��������ڵȴ���ͬһ������**δ��ռ��ִ��Ȩ��������Ҫ���������߳�**��
    + ��ǰ��ĳ�������ִ�е��µ�ǰ������Ҫ����������
    + ��Ϊ `notifyAll()` ���²���ȷ�Ļ���
+ [��ϰ21](Exe21.java)



#### 25.1.1.1 ��ʧ���ź�

+ ������������ [��ϰ21](Exe21.java) �е� `notify()`  ȱʧ
    + �Ƚ����� `Exe21Run2` �ĵȴ���Ȼ����� `Exe2Run1` �� `run()` ����



### 21.5.2 notify() �� notifyAll()

+ ʹ�� `notify()` �Ƕ� `notifyAll()` ��һ���Ż�
+ ʹ�� `notify()` ʱֻ�ỽ��**һ��**��ǰ������������
+ `notifyAll()` �������������ڵȴ�������ʵʩ�������еĹ�����ǰ��������
+ [TestNotifyAll](TestNotifyAll.java) ����Ĳ�������˵���ĳ���



### 21.5.3 �����ߺ�������

+ ����ģ��
+ [Restautant](../Example_Code/concurrency/Restaurant.java)
    + `interrupt()` �ڷ��͸�һ������֮��
        + ������������ִ�У�����Ӱ�죨`Thread.interrupted()` ���� `true`��
        + �������ﴦ�ڿ��жϵ�����֮�У����׳��쳣 `InterruptedException`
    + �����´��� `shutdownNow()` ����

```java
// ʾ������
import java.util.concurrent.*;
import static net.mindview.util.Print.*;

class Meal {
    private final int orderNum;
    public Meal(int orderNum) { this.orderNum = orderNum; }
    public String toString() { return "Meal " + orderNum; }
}

class WaitPerson implements Runnable {
    private Restaurant restaurant;
    public WaitPerson(Restaurant r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized(this) {
                    while(restaurant.meal == null)
                        wait(); // ... for the chef to produce a meal
                }
                print("Waitperson got " + restaurant.meal);
                synchronized(restaurant.chef) {
                    restaurant.meal = null;
                    restaurant.chef.notifyAll(); // Ready for another
                }
            }
        } catch(InterruptedException e) {
            print("WaitPerson interrupted");
        }
    }
}

class Chef implements Runnable {
    private Restaurant restaurant;
    private int count = 0;
    public Chef(Restaurant r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized(this) {
                    while(restaurant.meal != null)
                        wait(); // ... for the meal to be taken
                }
                if(++count == 10) {
                    print("Out of food, closing");
                    restaurant.exec.shutdownNow();
                }
                printnb("Order up! ");
                synchronized(restaurant.waitPerson) {
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch(InterruptedException e) {
            print("Chef interrupted");
        }
    }
}

public class Restaurant {
    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);
    public Restaurant() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }
    public static void main(String[] args) {
        new Restaurant();
    }
}
```

```output
Order up! Waitperson got Meal 1
Order up! Waitperson got Meal 2
Order up! Waitperson got Meal 3
Order up! Waitperson got Meal 4
Order up! Waitperson got Meal 5
Order up! Waitperson got Meal 6
Order up! Waitperson got Meal 7
Order up! Waitperson got Meal 8
Order up! Waitperson got Meal 9
Out of food, closing
WaitPerson interrupted
Order up! Chef interrupted
```

+ �� `WaitPerson/Chef` ����



#### 21.5.3.1 ʹ����ʽ�� Lock �� Condition ����

+ `await(),signal(),signalAll()`
    + `signalAll()` ����� `notifyAll()` ���Ӱ�ȫ
    + ֻӰ��� `Condition` �ϵĲ���
+ [WaxOMatic2](../Example_Code/concurrency/waxomatic2/WaxOMatic2.java)

```java
package concurrency.waxomatic2;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import static net.mindview.util.Print.*;

class Car {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean waxOn = false;
    public void waxed() {
        lock.lock();
        try {
            waxOn = true; // Ready to buff
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public void buffed() {
        lock.lock();
        try {
            waxOn = false; // Ready for another coat of wax
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public void waitForWaxing() throws InterruptedException {
        lock.lock();
        try {
            while(waxOn == false)
                condition.await();
        } finally {
            lock.unlock();
        }
    }
    public void waitForBuffing() throws InterruptedException{
        lock.lock();
        try {
            while(waxOn == true)
                condition.await();
        } finally {
            lock.unlock();
        }
    }
}

class WaxOn implements Runnable {
    private Car car;
    public WaxOn(Car c) { car = c; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                printnb("Wax On! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        } catch(InterruptedException e) {
            print("Exiting via interrupt");
        }
        print("Ending Wax On task");
    }
}
```



### 21.5.4 ������-�����������

+ `java.util,concurrent.BlockingQueue`
    + `LinkedBlockingQueue`���޽�
    + `ArrayBlockingQueue`���н�
    + `SynchronousQueue`��`1`
+ [TestBlockingQueues](../Example_Code/concurrency/TestBlockingQueues.java)
    + `put()/take()` ������������
    + ͨ��ʹ�ö��е��߳̽��� `run()` �ĵ��ã��ܹ�ʵ�������ִ��˳������
        + �����޲���



#### 21.5.1.1 ��˾ BlockingQueue

+ [ToastOMatic](../Example_Code/concurrency/ToastOMatic.java)

    + ˳��������˾ `->` Ĩ���� `->` Ϳ����
    + ά����������

    ```java
    ToastQueue dryQueue = new ToastQueue(),
               butteredQueue = new ToastQueue(),
               finishedQueue = new ToastQueue();
    ```



### 21.5.5 �����ͨ���ܵ���������/���

+ ͨ������������̼߳����ͨ��ͨ��������
+ �ṩ�̹߳��ܵ������ ���ܵ��� ����ʽ���̼߳����������ṩ��֧��
+ `PipedReader,PipedWriter`
+ [PipedIO](../Example_Code/concurrency/PipedIO.java)

```java
// ʾ������
import java.util.concurrent.*;
import java.io.*;
import java.util.*;
import static net.mindview.util.Print.*;

class Sender implements Runnable {
    private Random rand = new Random(47);
    private PipedWriter out = new PipedWriter();
    public PipedWriter getPipedWriter() { return out; }
    public void run() {
        try {
            while(true)
                for(char c = 'A'; c <= 'z'; c++) {
                    out.write(c);
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
                }
        } catch(IOException e) {
            print(e + " Sender write exception");
        } catch(InterruptedException e) {
            print(e + " Sender sleep interrupted");
        }
    }
}

class Receiver implements Runnable {
    private PipedReader in;
    public Receiver(Sender sender) throws IOException {
        in = new PipedReader(sender.getPipedWriter());
    }
    public void run() {
        try {
            while(true) {
                // Blocks until characters are there:
                printnb("Read: " + (char)in.read() + ", ");
            }
        } catch(IOException e) {
            print(e + " Receiver read exception");
        }
    }
}

public class PipedIO {
    public static void main(String[] args) throws Exception {
        Sender sender = new Sender();
        Receiver receiver = new Receiver(sender);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(sender);
        exec.execute(receiver);
        TimeUnit.SECONDS.sleep(4);
        exec.shutdownNow();
    }
}
```



## 21.6 ���� deadlock

+ ������ȴ���������ִ��
+ **��ѧ�ҾͲ�����**��`Edsger Dijkstra`��
    + ״̬���Է����ȴ�
    + `5` ���ˣ�ֻ�� `5` �����ӣ�ֻ���õ� `2` �����Ӳ��ܳԵ���
    + [Chopstick](../Example_Code/concurrency/Chopstick.java)
    + [Philosopher](../Example_Code/concurrency/Philosopher.java)
    + [DeadLockingDiningPhilosophers](../Example_Code/concurrency/DeadLockingDiningPhilosophers.java)

```java
import java.util.concurrent.*;
import java.util.*;
import static net.mindview.util.Print.*;

public class Philosopher implements Runnable {
    private Chopstick left;
    private Chopstick right;
    private final int id;
    private final int ponderFactor;
    private Random rand = new Random(47);
    private void pause() throws InterruptedException {
        if(ponderFactor == 0) return;
        TimeUnit.MILLISECONDS.sleep(
            rand.nextInt(ponderFactor * 250));
    }
    public Philosopher(Chopstick left, Chopstick right,
                       int ident, int ponder) {
        this.left = left;
        this.right = right;
        id = ident;
        ponderFactor = ponder;
    }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                print(this + " " + "thinking");
                pause();
                // Philosopher becomes hungry
                print(this + " " + "grabbing right");
                right.take();
                print(this + " " + "grabbing left");
                left.take();
                print(this + " " + "eating");
                pause();
                right.drop();
                left.drop();
            }
        } catch(InterruptedException e) {
            print(this + " " + "exiting via interrupt");
        }
    }
    public String toString() { return "Philosopher " + id; }
}
```

```java
public class Chopstick {
    private boolean taken = false;
    public synchronized
        void take() throws InterruptedException {
        while(taken)
            wait();
        taken = true;
    }
    public synchronized void drop() {
        taken = false;
        notifyAll();
    }
}
```

```java
// ���ִ���
// �����������ұߵĿ���,��������ߵĿ���
for(int i = 0; i < size; i++)
    exec.execute(new Philosopher(
        sticks[i], sticks[(i+1) % size], i, ponder));
```



+ �п��ܻᴥ�����������ǲ�һ��
+ �������������������ͬʱ���㣩
    + **��������**������ʹ�õ���Դ��������һ���ǲ��ܹ����
        + һ������ֻ�ܱ�һ����ѧ��ʹ��
    + ������һ���������������һ����Դ���ҵȴ���ȡһ����ǰ�����������е���Դ
        + ��ѧ�ұ������һ�����ӣ��ڵȴ�����һ������
    + ��Դ���ܱ�������ռ������������Դ�ͷŵ�����ͨ�¼�
        + ��ѧ�Ҳ��ܴ�������ѧ������������
    + ������ѭ���ȴ�
        + ���������ұߵĿ���,������ߵĿ���
+ �����Ľ�����ƻ����� `4` ����һ����������ߵĿ���



## 21.7 ������еĹ���

### 21.7.1 CountDownLatch

+ ����ͬ�����߶������ǿ�����ǵȴ�����������ִ�е�һ����������
+ `await()` ������һֱ������ֱ������Ϊ `0`
+ ����ֵ�������ã�`countDown()` ���Լ��ټ���ֵ
+ `CyclicBarrier` �������ü���ֵ
+ �����÷�����
    + [CountDownLatchDemo](../Example_Code/concurrency/CountDownLatchDemo.java)
        + ��������� `n` �����໥������������񣬴���ֵΪ `n` �� `CountDownLatch`
        + ��ÿ���������ʱ������������������ϵ��� `countDown()`
        + �ȴ����ⱻ���������������������ϵ��� `await()`

```java
//: concurrency/CountDownLatchDemo.java
import java.util.concurrent.*;
import java.util.*;
import static net.mindview.util.Print.*;

// Performs some portion of a task:
class TaskPortion implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private static Random rand = new Random(47);
    private final CountDownLatch latch;
    TaskPortion(CountDownLatch latch) {
        this.latch = latch;
    }
    public void run() {
        try {
            doWork();
            latch.countDown();
        } catch(InterruptedException ex) {
            // Acceptable way to exit
        }
    }
    public void doWork() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
        print(this + "completed");
    }
    public String toString() {
        return String.format("%1$-3d ", id);
    }
}

// Waits on the CountDownLatch:
class WaitingTask implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private final CountDownLatch latch;
    WaitingTask(CountDownLatch latch) {
        this.latch = latch;
    }
    public void run() {
        try {
            latch.await();
            print("Latch barrier passed for " + this);
        } catch(InterruptedException ex) {
            print(this + " interrupted");
        }
    }
    public String toString() {
        return String.format("WaitingTask %1$-3d ", id);
    }
}

public class CountDownLatchDemo {
    static final int SIZE = 100;
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        // All must share a single CountDownLatch object:
        CountDownLatch latch = new CountDownLatch(SIZE);
        for(int i = 0; i < 10; i++)
            exec.execute(new WaitingTask(latch));
        for(int i = 0; i < SIZE; i++)
            exec.execute(new TaskPortion(latch));
        print("Launched all tasks");
        exec.shutdown(); // Quit when all tasks complete
    }
}
```

+ [GetChildrenBack](GetChildrenBack.java)



#### 21.7.1.1 ����е��̰߳�ȫ

+ `Random.nextInt()` �̰߳�ȫ������ȥ�� `static` ʹ�䰲ȫ��
+ `Math.random()` �̰߳�ȫ
+

```java
// java.util.Random.java
protected int next(int bits) {
    long oldseed, nextseed;
    AtomicLong seed = this.seed;
    do {
        oldseed = seed.get();
        nextseed = (oldseed * multiplier + addend) & mask;
    } while (!seed.compareAndSet(oldseed, nextseed));
    return (int)(nextseed >>> (48 - bits));
}
```



### 21.7.2 CyclicBarrier

+ �������ã������� `CountDownLatch`
    + դ��������`new CyclicBarrier(int, Runnable)`������Ϊ `0` ʱ�Զ�ִ��
    + ���������̣߳�ָ������`int`������������֮���Զ����� `Runnable`
+ [HorseRace](../Example_Code/concurrency/HorseRace.java)
+ [GetChildrenBack2](GetChildrenBack2.java)



### 21.7.3 DelayQueue

+ �޽�� `BlockingQueue` �����ڷ�ֹʵ���� `Delayed` �ӿڵĶ���

```java
// java.util.concurrent.Delayed.java
public interface Delayed extends Comparable<Delayed> {
    /**
     * Returns the remaining delay associated with this object, in the
     * given time unit.
     *
     * @param unit the time unit
     * @return the remaining delay; zero or negative values indicate
     * that the delay has already elapsed
     */
    long getDelay(TimeUnit unit);
}
```

+ `poll()` ����ͷԪ�ػ�δ�ӳٵ��ڣ����� `null`
+ `take()` ���ض�ͷԪ�أ����޵��ڣ���ȴ�������
+ ��һ�����ȶ��У��ӳٵ���ʱ��Խ��Խ�ȳ����У�

```java
public class DelayQueue<E extends Delayed> extends AbstractQueue<E>
    implements BlockingQueue<E> {

    private final transient ReentrantLock lock = new ReentrantLock();
    private final PriorityQueue<E> q = new PriorityQueue<E>();
    // ...
}
```

+ [DelayQueueDemo](../Example_Code/concurrency/DelayQueueDemo.java)

```java
// ���ִ���
class DelayedTaskConsumer implements Runnable {
    private DelayQueue<DelayedTask> q;
    public DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
        this.q = q;
    }
    public void run() {
        try {
            while(!Thread.interrupted())
                q.take().run(); // Run task with the current thread
        } catch(InterruptedException e) {
            // Acceptable way to exit
        }
        print("Finished DelayedTaskConsumer");
    }
}
```



### 21.7.4 PriorityBlockingQueue

+ һ���������ڲ�����̵����ȶ���



### 21.7.5 ʹ�� ScheduledExecutor �����ҿ�����

+ `ScheduledTHreadPoolExecutor`
    + `schedule()` ����һ������
    + `scheduleAtFixedRate()` ÿ�������ʱ���ظ�ִ������
+ [GreenHouseScheduler](../Example_Code/concurrency/GreenHouseScheduler.java)

```java
// defination
ScheduledThreadPoolExecutor scheduler =
    new ScheduledThreadPoolExecutor(10);

// schedule
scheduler.schedule(event,delay,TimeUnit.MILLISECONDS);

// scheduleAtFixedRate
scheduler.scheduleAtFixedRate(
    event, initialDelay, period, TimeUnit.MILLISECONDS);
```



### 21.7.6 Semaphore

+ **�����ź���**
    + ���� `n` ������ͬʱ���������Դ
    + `acquire(),release()`
+ [Pool](../Example_Code/concurrency/Pool.java) �����
    + [Fat](../Example_Code/concurrency/Fat.java)
    + [SemaphoreDemo](../Example_Code/concurrency/SemaphoreDemo.java)

```java
//: concurrency/Pool.java
import java.util.concurrent.*;
import java.util.*;

public class Pool<T> {
    private int size;
    private List<T> items = new ArrayList<T>();
    private volatile boolean[] checkedOut;
    private Semaphore available;
    public Pool(Class<T> classObject, int size) {
        this.size = size;
        checkedOut = new boolean[size];
        available = new Semaphore(size, true);
        // Load pool with objects that can be checked out:
        for(int i = 0; i < size; ++i)
            try {
                // Assumes a default constructor:
                items.add(classObject.newInstance());
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
    }
    public T checkOut() throws InterruptedException {
        available.acquire();
        return getItem();
    }
    public void checkIn(T x) {
        if(releaseItem(x))
            available.release();
    }
    private synchronized T getItem() {
        for(int i = 0; i < size; ++i)
            if(!checkedOut[i]) {
                checkedOut[i] = true;
                return items.get(i);
            }
        return null; // Semaphore prevents reaching here
    }
    private synchronized boolean releaseItem(T item) {
        int index = items.indexOf(item);
        if(index == -1) return false; // Not in the list
        if(checkedOut[index]) {
            checkedOut[index] = false;
            return true;
        }
        return false; // Wasn't checked out
    }
}
```

+ ����һ���͹�ֻ�� `25` ������Ա��������������ϳ���



### 21.7.7 Exchanger

+ `Exchanger` ������������֮�佻�������դ��
    + �����������դ��ǰ��ӵ�ж��󻥻�
+ ����Ӧ�ó�����
    + һ�������ڴ���������Щ�������������ʮ�ָ߰��������һ��������������Щ����
    + ͨ�����ַ�ʽ�������и����Ķ����ڱ�������ͬʱ������
    + ���� `List` �������ջ��У�
+ `CopyOnWriteArrayList` �����ڱ�����ʱ����� `remove()`

```java
for(T x : holder) {
    value = x; // Fetch out value
    holder.remove(x); // OK for CopyOnWriteArrayList
}
```

+ [ExchangerDemo](../Example_Code/concurrency/ExchangerDemo.java)
+ ���� `Exchanger.exchange()` ��ʱ����������ֱ���Է���������Լ��� `exchange()` ����
    + ����ɺ󣬶��߻���
+ [ExchangerDemo](ExchangerDemo.java)
    + һ���ܼ򵥵�ʾ��

```java
import java.util.concurrent.*;
import java.util.*;

class EDProducerHolder {}
class EDConsumerHolder {}

class EDProducer implements Runnable {
    private Exchanger<Object> ex;
    private Object holder = new EDProducerHolder();
    public EDProducer(Exchanger<Object> ex) {
        this.ex = ex;
    }
    @Override
    public void run() {
        try {
            holder = ex.exchange(holder);
        } catch(InterruptedException e) {
          // OK to terminate this way.
        }
        System.out.println(
            this.getClass().getName()
            + " holds "
            + holder.getClass().getName()
        );
    }
}

class EDConsumer implements Runnable {
    private Exchanger<Object> ex;
    private Object holder = new EDConsumerHolder();
    public EDConsumer(Exchanger<Object> ex) {
        this.ex = ex;
    }
    @Override
    public void run() {
        try {
            holder = ex.exchange(holder);
        } catch(InterruptedException e) {
          // OK to terminate this way.
        }
        System.out.println(
            this.getClass().getName()
            + " holds "
            + holder.getClass().getName()
        );
    }
}

public class ExchangerDemo {
    public static void main(String...args) {
        Exchanger<Object> ex = new Exchanger<>();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new EDProducer(ex));
        exec.execute(new EDConsumer(ex));
        exec.shutdown();
    }
}
/* Output
EDConsumer holds EDProducerHolder
EDProducer holds EDConsumerHolder
*/
```



## 21.8 ����

### 21.8.1 ���г���Ա����

+ ��������
    + ��������ĳ���
    + �������޵ķ������ṩ��������ķ���ʱ��
+ [BankTellerSimulation](../Example_Code/concurrency/BankTellerSimulation.java)
+ [BankerTellerDemo](BankerTellerDemo.java)
    + ���ж� `I/O` �����ϻ������ò�̫��



### 21.8.2 �������

+ [RestaurantWithQueues](../Example_Code/concurrency/restaurant2/RestaurantWithQueues.java)
+ ͨ�� **����** ���������ͨ��



### 21.8.3 �ַ�����

+ [CarBuilder](../Example_Code/concurrency/CarBuilder.java)
    + ���ʾ�����뻹�ǱȽϰ���
    + �������̣���װ�����������ᡢ���ӣ�ʣ�� `3` ����������

```java
// ���ִ���
class RobotPool {
    // Quietly prevents identical entries:
    private Set<Robot> pool = new HashSet<Robot>();
    public synchronized void add(Robot r) {
        pool.add(r);
        notifyAll();
    }
    public synchronized void
        hire(Class<? extends Robot> robotType, Assembler d)
        throws InterruptedException {
        for(Robot r : pool)
            if(r.getClass().equals(robotType)) {
                pool.remove(r);
                r.assignAssembler(d);
                r.engage(); // Power it up to do the task
                return;
            }
        wait(); // None available
        hire(robotType, d); // Try again, recursively ����
    }
    public synchronized void release(Robot r) { add(r); }
}
```



## 21.9 ���ܵ���

### 21.9.1 �Ƚϸ��໥�⼼��

+ `synchronized,Lock,Atomic`
+ [SimpleMicroBenchmark](../Example_Code/concurrency/SimpleMicroBenchmark.java)

```output
�ҵĻ��� : Lock/synchronized = 0.879
�������� : Lock/synchronized = 3.834
```

+ ���ڵ�����
    + ����Ϊ���߳�
    + ��֪���������ײ��Ƿ�����Ż�
+ ���ӵĲ��ԣ��ñ����������Ż�������Ԥ�⣩�����߳�
+ [SynchronizationComparisons](../Example_Code/concurrency/SynchronizationComparisons.java)
    + �������ֱ��ը�ˣ�����Խ�磨���Ǵ��ڶ��̵߳��µľ������⣩
        + ֱ�����������˿���`SIZE*2`��
    + **ģ����Ʒ���**

```output
�ҵĻ��� : Atomic < Lock < synchronized
�������� : Atomic < synchronized < Lock
```



### 21.9.2 ��������

+ �޸ĺͶ�ȡ����ͬʱ����
+ `CopyOnWriteArrayList`
    + ����ʹ�õ�����ʱ���� `remove()`
    + д�����´��������ײ�����ĸ���
+ `ConcurentHashMap,ConcurrentLinkedQueue` ���Ƽ����������ǲ��ֶ�������������



#### 21.9.2.1 �ֹ���

+ [Tester](../Example_Code/concurrency/Tester.java)
+ [ListComparisons](../Example_Code/concurrency/ListComparisons.java)
    + [ListComparisonsOutput](ListComparisonsOutput.txt)
+ �����ʾ������ôд�룬������ʹ������д��ʱ
    + `CopyOnWriteArrayList` Ч�ʶ����Ǳ� `synchronized ArrayList` Ҫ��
+ ʹ�� `Tester` ���Կ�ܿ��ԱȽ� `Map`
    + [MapComparisons](../Example_Code/concurrency/MapComparisons.java)



### 21.9.3 �ֹۼ���

+ `Atomic`
    + `decrementAndGet()`��ԭ���Բ���
    + `compareANdSet()` ���ύ��ֵ�;�ֵ���ж��Ƿ����˲�������
        + ʧ�ܻᷢ��ɶ���ü�������������
        + ��Ҫ��һ������Ľ������
+ [FastSimulation](../Example_Code/concurrency/FastSimulation.java)

```java
if(!GRID[element][i].compareAndSet(oldvalue, newvalue)) {
    // Policy here to deal with failure. Here, we
    // just report it and ignore it; our model
    // will eventually deal with it.
    print("Old value changed from " + oldvalue);
}
```



### 21.9.4 ReadWriteLock

+ **��д���** Ӧ�ó���
    + ��д�ߣ����Զ��
    + ��д�ߣ����ܶ�
+ �Ƿ���������������ȡ����Ӧ�ó���
+ [ReaderWriterList](../Example_Code/concurrency/ReaderWriterList.java)

```java
private ReentrantReadWriteLock lock =
    new ReentrantReadWriteLock(true);
// ...
public T set(int index, T element) {
    Lock wlock = lock.writeLock();
    wlock.lock();
    try {
        return lockedList.set(index, element);
    } finally {
        wlock.unlock();
    }
}
// ...
public T get(int index) {
    Lock rlock = lock.readLock();
    rlock.lock();
    try {
        // Show that multiple readers
        // may acquire the read lock:
        if(lock.getReadLockCount() > 1)
            print(lock.getReadLockCount());
        return lockedList.get(index);
    } finally {
        rlock.unlock();
    }
}
```



## 21.10 �����

+ ���̱߳��ģ�ͱ�����������̵�
+ ���滻�ķ�ʽ��**�����/�ж���**
    + ÿһ������ά�����Լ��Ĺ������̺߳���Ϣ����
+ `Future` �����������ģʽ�к�����
+ [ActiveObjectDemo](../Example_Code/concurrency/ActiveObjectDemo.java)

```java
import java.util.concurrent.*;
import java.util.*;
import static net.mindview.util.Print.*;

public class ActiveObjectDemo {
    private ExecutorService ex =
        Executors.newSingleThreadExecutor();
    private Random rand = new Random(47);
    // Insert a random delay to produce the effect
    // of a calculation time:
    private void pause(int factor) {
        try {
            TimeUnit.MILLISECONDS.sleep(
                100 + rand.nextInt(factor));
        } catch(InterruptedException e) {
            print("sleep() interrupted");
        }
    }
    public Future<Integer>
        calculateInt(final int x, final int y) {
        return ex.submit(new Callable<Integer>() {
            public Integer call() {
                print("starting " + x + " + " + y);
                pause(500);
                return x + y;
            }
        });
    }
    public Future<Float>
        calculateFloat(final float x, final float y) {
        return ex.submit(new Callable<Float>() {
            public Float call() {
                print("starting " + x + " + " + y);
                pause(2000);
                return x + y;
            }
        });
    }
    public void shutdown() { ex.shutdown(); }
    public static void main(String[] args) {
        ActiveObjectDemo d1 = new ActiveObjectDemo();
        // Prevents ConcurrentModificationException:
        List<Future<?>> results =
            new CopyOnWriteArrayList<Future<?>>();
        for(float f = 0.0f; f < 1.0f; f += 0.2f)
            results.add(d1.calculateFloat(f, f));
        for(int i = 0; i < 5; i++)
            results.add(d1.calculateInt(i, i));
        print("All asynch calls made");
        while(results.size() > 0) {
            for(Future<?> f : results)
                if(f.isDone()) {
                    try {
                        print(f.get());
                    } catch(Exception e) {
                        throw new RuntimeException(e);
                    }
                    results.remove(f);
                }
        }
        d1.shutdown();
    }
}
```

+ ͨ�� `Future` ��ȡ�߳�ִ�еĽ��
    + `submit(),get()`



## 21.11 �ܽ�

### 21.11.1 ���׶���

+ `Java Concuurency in Practice`
+ `Concurrent Programming in Java`

