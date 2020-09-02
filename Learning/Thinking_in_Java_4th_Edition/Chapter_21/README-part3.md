# Chapter_21 并发(part3)

> [总目录](../README.md)

---

[TOC]

---

+ [part1](README.md)
+ [part2](README-part2.md)
+ [part3](README-part3.md)



## 21.5 线程之间的协作

+ 使用的特性：**互斥**



### 21.5.1 wait() 与 notifyAll()

+ 基类 `Object` 实现的
+ 在等待的时候进行空循环：**忙等待**
    + 不良的 `CPU` 周期使用方式
+ `wait()` 会挂起任务，在收到 `notify()/notifyAll()` 之后恢复
+ `sleep()/yield()` 不释放锁
+ `wait()` 释放锁
+ `wait(ms)`：
    + `wait()` 期间释放锁
    + 可以通过 `notify()/notifyAll()` 或者时间到期从 `wait()` 中恢复
+ `wait()` ：无限等待
+ 我们只能在同步控制方法或者同步控制块里调用 `wait(),notify(),notifyAll()`
    + 否则：通过编译但是有运行时异常
    + 说明在调用 `wait(),notify(),notifyAll()` 时必须拥有对象的锁

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

+ `sleep()` 可以在非同步方法里面调用，**因为不需要操作锁**
+ 一个简单的示例：打蜡后再抛光
+ [WaxOMatic](../Example_Code/concurrency/waxomatic/WaxOMatic.java)

```java
// 部分代码
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

+ `wait()` 的阻塞可以被 `shutdownNow()` 中断
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

+ **伪唤醒**
    + 另外一种从 `wait()` 中抽身的方式（**平台依赖**）
    + 一个线程可以过早的停止阻塞，而不需要 `notify()` 等方式
+ 使用一个感兴趣的条件包围 `wait()`
    + 多个任务都在等待着同一个任务（**未抢占到执行权的任务需要继续挂起线程**）
    + 先前有某个任务的执行导致当前任务需要继续被挂起
    + 因为 `notifyAll()` 导致不正确的唤醒
+ [练习21](Exe21.java)



#### 25.1.1.1 错失的信号

+ 死锁，例如在 [练习21](Exe21.java) 中的 `notify()`  缺失
    + 先进入了 `Exe21Run2` 的等待，然后进入 `Exe2Run1` 的 `run()` 方法



### 21.5.2 notify() 与 notifyAll()

+ 使用 `notify()` 是对 `notifyAll()` 的一种优化
+ 使用 `notify()` 时只会唤醒**一个**当前锁关联的任务
+ `notifyAll()` 将唤醒所有正在等待的任务（实施上是所有的关联当前锁的任务）
+ [TestNotifyAll](TestNotifyAll.java) 具体的测试上述说法的程序



### 21.5.3 生产者和消费者

+ 经典模型
+ [Restautant](../Example_Code/concurrency/Restaurant.java)
    + `interrupt()` 在发送给一个任务之后
        + 若该任务正在执行，不受影响（`Thread.interrupted()` 返回 `true`）
        + 若该人物处于可中断的阻塞之中，则抛出异常 `InterruptedException`
    + 见如下代码 `shutdownNow()` 部分

```java
// 示例代码
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

+ 对 `WaitPerson/Chef` 加锁



#### 21.5.3.1 使用显式的 Lock 和 Condition 对象

+ `await(),signal(),signalAll()`
    + `signalAll()` 相对于 `notifyAll()` 更加安全
    + 只影响该 `Condition` 上的并发
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



### 21.5.4 生产者-消费者与队列

+ `java.util,concurrent.BlockingQueue`
    + `LinkedBlockingQueue`：无界
    + `ArrayBlockingQueue`：有界
    + `SynchronousQueue`：`1`
+ [TestBlockingQueues](../Example_Code/concurrency/TestBlockingQueues.java)
    + `put()/take()` 并发不出问题
    + 通过使用队列的线程进行 `run()` 的调用，能够实现任务的执行顺序问题
        + 本质无并发



#### 21.5.1.1 吐司 BlockingQueue

+ [ToastOMatic](../Example_Code/concurrency/ToastOMatic.java)

    + 顺序：制作吐司 `->` 抹黄油 `->` 涂果酱
    + 维护几个队列

    ```java
    ToastQueue dryQueue = new ToastQueue(),
               butteredQueue = new ToastQueue(),
               finishedQueue = new ToastQueue();
    ```



### 21.5.5 任务间通过管道进行输入/输出

+ 通过输入输出在线程间进行通信通常很有用
+ 提供线程功能的类库以 ”管道“ 的形式对线程间的输入输出提供了支持
+ `PipedReader,PipedWriter`
+ [PipedIO](../Example_Code/concurrency/PipedIO.java)

```java
// 示例代码
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



## 21.6 死锁 deadlock

+ 任务互相等待，都不能执行
+ **哲学家就餐问题**（`Edsger Dijkstra`）
    + 状态：吃饭，等待
    + `5` 个人，只有 `5` 根筷子，只有拿到 `2` 根筷子才能吃到饭
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
// 部分代码
// 都是先拿起右边的筷子,再拿起左边的筷子
for(int i = 0; i < size; i++)
    exec.execute(new Philosopher(
        sticks[i], sticks[(i+1) % size], i, ponder));
```



+ 有可能会触发死锁，但是不一定
+ 死锁问题产生的条件（同时满足）
    + **互斥条件**：任务使用的资源中至少有一个是不能共享的
        + 一根筷子只能被一个哲学家使用
    + 至少有一个任务他必须持有一个资源并且等待获取一个当前被别的任务持有的资源
        + 哲学家必须持有一根筷子，在等待另外一根筷子
    + 资源不能被任务抢占，任务必须把资源释放当作普通事件
        + 哲学家不能从其他哲学家那里抢筷子
    + 必须有循环等待
        + 都是先拿右边的筷子,再拿左边的筷子
+ 死锁的解决：破坏条件 `4` ，让一个人先拿左边的筷子



## 21.7 新类库中的构件

### 21.7.1 CountDownLatch

+ 用来同步或者多个任务，强制他们等待由其他任务执行的一组操作的完成
+ `await()` 方法会一直阻塞，直到计数为 `0`
+ 计数值不能重置，`countDown()` 可以减少计数值
+ `CyclicBarrier` 可以重置计数值
+ 典型用法如下
    + [CountDownLatchDemo](../Example_Code/concurrency/CountDownLatchDemo.java)
        + 将程序拆解成 `n` 个可相互独立解决的任务，创建值为 `n` 的 `CountDownLatch`
        + 当每个任务完成时，都会在这个锁存器上调用 `countDown()`
        + 等待问题被解决的任务再这个锁存器上调用 `await()`

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



#### 21.7.1.1 类库中的线程安全

+ `Random.nextInt()` 线程安全（可以去除 `static` 使其安全）
+ `Math.random()` 线程安全
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

+ 可以重置，类似于 `CountDownLatch`
    + 栅栏动作：`new CyclicBarrier(int, Runnable)`，计数为 `0` 时自动执行
    + 当关联的线程（指定个数`int`）都到达阻塞之后，自动调用 `Runnable`
+ [HorseRace](../Example_Code/concurrency/HorseRace.java)
+ [GetChildrenBack2](GetChildrenBack2.java)



### 21.7.3 DelayQueue

+ 无界的 `BlockingQueue` ，用于防止实现了 `Delayed` 接口的对象

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

+ `poll()` 若队头元素还未延迟到期，返回 `null`
+ `take()` 返回队头元素，若无到期，则等待至到期
+ 是一个优先队列（延迟到期时间越长越先出队列）

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
// 部分代码
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

+ 一个可以用于并发编程的优先队列



### 21.7.5 使用 ScheduledExecutor 的温室控制器

+ `ScheduledTHreadPoolExecutor`
    + `schedule()` 运行一次任务
    + `scheduleAtFixedRate()` 每个规则的时间重复执行任务
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

+ **计数信号量**
    + 允许 `n` 个对象同时访问这个资源
    + `acquire(),release()`
+ [Pool](../Example_Code/concurrency/Pool.java) 对象池
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

+ 例如一个餐馆只有 `25` 个服务员，这便适用于以上场景



### 21.7.7 Exchanger

+ `Exchanger` 是在两个任务之间交换对象的栅栏
    + 两个任务进出栅栏前后，拥有对象互换
+ 典型应用场景：
    + 一个任务在创建对象，这些对象的生产代价十分高昂，因此另一个任务负责消费这些对象
    + 通过这种方式，可以有耕读的对象在被创建的同时被消费
    + 两个 `List` 互换（空换有）
+ `CopyOnWriteArrayList` 允许在遍历的时候调用 `remove()`

```java
for(T x : holder) {
    value = x; // Fetch out value
    holder.remove(x); // OK for CopyOnWriteArrayList
}
```

+ [ExchangerDemo](../Example_Code/concurrency/ExchangerDemo.java)
+ 调用 `Exchanger.exchange()` 的时候，它将阻塞直至对方任务调用自己的 `exchange()` 方法
    + 都完成后，二者互换
+ [ExchangerDemo](ExchangerDemo.java)
    + 一个很简单的示例

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



## 21.8 仿真

### 21.8.1 银行出纳员仿真

+ 场景描述
    + 对象随机的出现
    + 数量有限的服务器提供随机数量的服务时间
+ [BankTellerSimulation](../Example_Code/concurrency/BankTellerSimulation.java)
+ [BankerTellerDemo](BankerTellerDemo.java)
    + 在中断 `I/O` 阻塞上还是做得不太好



### 21.8.2 饭店仿真

+ [RestaurantWithQueues](../Example_Code/concurrency/restaurant2/RestaurantWithQueues.java)
+ 通过 **队列** 进行任务间通信



### 21.8.3 分发工作

+ [CarBuilder](../Example_Code/concurrency/CarBuilder.java)
    + 这个示例代码还是比较棒的
    + 创建底盘，安装发动机、车厢、轮子（剩余 `3` 个任务无序）

```java
// 部分代码
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
        hire(robotType, d); // Try again, recursively 重启
    }
    public synchronized void release(Robot r) { add(r); }
}
```



## 21.9 性能调优

### 21.9.1 比较各类互斥技术

+ `synchronized,Lock,Atomic`
+ [SimpleMicroBenchmark](../Example_Code/concurrency/SimpleMicroBenchmark.java)

```output
我的机子 : Lock/synchronized = 0.879
书上数据 : Lock/synchronized = 3.834
```

+ 存在的问题
    + 测试为单线程
    + 不知道编译器底层是否进行优化
+ 复杂的测试，让编译器减少优化（难以预测），多线程
+ [SynchronizationComparisons](../Example_Code/concurrency/SynchronizationComparisons.java)
    + 这个测试直接炸了，数组越界（还是存在多线程导致的竞争问题）
        + 直接数组往大了开（`SIZE*2`）
    + **模板设计方法**

```output
我的机子 : Atomic < Lock < synchronized
书上数据 : Atomic < synchronized < Lock
```



### 21.9.2 免锁容器

+ 修改和读取可以同时发生
+ `CopyOnWriteArrayList`
    + 允许使用迭代器时进行 `remove()`
    + 写入则导致创建整个底层数组的副本
+ `ConcurentHashMap,ConcurrentLinkedQueue` 类似计数，但是是部分而不是整个数组



#### 21.9.2.1 乐观锁

+ [Tester](../Example_Code/concurrency/Tester.java)
+ [ListComparisons](../Example_Code/concurrency/ListComparisons.java)
    + [ListComparisonsOutput](ListComparisonsOutput.txt)
+ 结果显示当不怎么写入，甚至是使用少量写入时
    + `CopyOnWriteArrayList` 效率都还是比 `synchronized ArrayList` 要高
+ 使用 `Tester` 测试框架可以比较 `Map`
    + [MapComparisons](../Example_Code/concurrency/MapComparisons.java)



### 21.9.3 乐观加锁

+ `Atomic`
    + `decrementAndGet()`：原子性操作
    + `compareANdSet()` ：提交新值和旧值，判断是否发生了并发错误
        + 失败会发生啥？该技术的问题所在
        + 需要有一个合理的解决方案
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

+ **少写多读** 应用场景
    + 无写者：可以多读
    + 有写者：不能读
+ 是否有性能提升还是取决于应用场景
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



## 21.10 活动对象

+ 多线程编程模型本身是面向过程的
+ 可替换的方式：**活动对象/行动者**
    + 每一个对象都维护着自己的工作器线程和消息队列
+ `Future` 类在这种设计模式中很有用
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

+ 通过 `Future` 获取线程执行的结果
    + `submit(),get()`



## 21.11 总结

### 21.11.1 进阶读物

+ `Java Concuurency in Practice`
+ `Concurrent Programming in Java`

