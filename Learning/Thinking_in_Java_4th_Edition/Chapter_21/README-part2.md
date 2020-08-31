# Chapter_21 并发(part2)

> [总目录](../README.md)

---

[TOC]

---

+ [part1](README.md)
+ [part2](README-part2.md)
+ [part3](README-part3.md)



## 21.3 共享受限资源

### 21.3.1 不正确的访问资源

+ 示例代码
    + [IntGenerator](../Example_Code/concurrency/IntGenerator.java)
    + [EvenChecker](../Example_Code/concurrency/EvenChecker.java)
    + [EvenGenerator](../Example_Code/concurrency/EvenGenerator.java)
+ 竞争冲突 **race**

```java
// 示例代码 
// concurrency/EvenGenerator.java 
public int next() {
    // 所有的线程共享一个 Generator
    ++currentEvenValue; // 可能引发竞争(race)
    ++currentEvenValue;
    return currentEvenValue;
}
```

+ 注意递增本身也不是原子操作，也不是线程安全的



### 21.3.2 解决共享资源竞争

+ 一种解决方法：**加锁**
+ **序列化访问共享资源**
+ 互斥量：`mutex`
+ 现实例子：单人浴室
+ 优先级 `setPriority()`，让出执行权 `yield()` 像线程调度器提建议
    + 在没有锁（允许获得）的时候，可以让指定线程率先获取执行权/让出执行权
    + 只是建议，具体效果取决于具体平台和 `JVM` 实现
+ 共享资源一般十一内存形式存在的内存片段
    + 文件、`I/O`端口 ······
+ 关键字：`synchronized`
    + 方法
    + 代码片段

```java
// 上述示例代码
// 加上 synchronized 关键字之后就不会引发冲突
public synchronized int next() {
    ++currentEvenValue;
    ++currentEvenValue;
    return currentEvenValue;
}
```

+ 所有的对象都自动含有单一的锁

+ 当在对象上调用器任意 `synchronized` 方法的时候，此对象被加锁

    + 此时该对象的其他 `synchronized` 方法只有等到

        前一个 `synchronized` 方法调用完毕并释放锁之后才能被调用

+ **同一个对象的所有 `synchronized` 方法共享一个锁**

+ 注意使用并发时，将域设置为 `private` 是十分重要的

    + 否则 `synchronized` 关键字就不能防止其他任务直接访问域，产生冲突

+ 一个任务可以多次获得对象的锁

    + 例如在一个 `synchronized` 方法中调用另外一个 `synchronized` 方法
    + `JVM` 负责计数
        + 没有占用：`0`，初次获得锁：`1`
    + 只有首先获得了锁的任务才运行继续获取多个锁

```java
import java.util.concurrent.*;
public class WillNotDeadlock implements Runnable {
    public synchronized void f() { g(); }
    public synchronized void g() {}
    @Override
    public void run(){
        f();
    }
    public static void main(String...args) {
        new Thread(new WillNotDeadlock()).start();
    }
}
/* Output
*/
```

+ 针对每一个类，也都有一把锁（作为 `Class` 类的一部分）

    + `synchronized static`  可以在类的范围内防止对  `static` 数据的并发访问

+ `Brain` 的同步规则

    + 如果你正在写一个变量，它可能接下来被另一个线程读取，

        或者正在读取一个上一次已经被另一个线程写过的变量，那么你必须使用同步，

        而且，读写都必须使用相同的监视器锁同步。

+ 如果在你的类中有超过一个方法在处理临界数据，那么你必须同步**所有**相关的方法
+ 每个访问临界共享资源的方法都必须被同步



#### 21.3.2.1 同步控制 EvenGenerator

+ `synchronized` 关键字

```java
public synchronized int next() {
    ++currentEvenValue;
    ++currentEvenValue;
    return currentEvenValue;
}
```



#### 21.3.2.2 使用显式的 Lock 对象

```java
// import java.util.concurrent.locks.*;
// ...
private Lock lock = new ReentrantLock();
public int next() {
    lock.lock();
    try {
        ++currentEvenValue;
        ++currentEvenValue;
        return currentEvenValue;
    } finally {
        lock.unlock();
    }
}
```

+ 注意 `return` 语句的位置，如果放在 `try-catch` 语句外面则还会引发问题
    + `return` 语句进入到另外一个线程的 `try-catch` 语句块中

```java
// java.util.concurrent.locks.Lock.java
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
```

+ `tryLock` 示例

```java
// tryLock 示例
Lock lock = ...;
if (lock.tryLock()) {
    try {
        // manipulate protected state
    } finally {
        lock.unlock();
    }
} else {
    // perform alternative actions
}
```

+ 显式使用 `Lock` 的好处
    + 能够做清理工作，维护系统，`synchronized` 只会抛出异常
    + 有更加细粒度的操作（上面的方法）
+ 但是一般情况下我们使用 `synchronized` 关键字就够了
+ 细粒度操作示例代码
+ [AttemptLocking](../Example_Code/concurrency/AttemptLocking.java)
    + `win10` 中 `Thread.yield()` 建议好像不太好使
    + 用的 `Thread.sleep(1000)`



### 21.3.3 原子性与易变性

+ `Goetz` 测试（一个玩笑）

+ **原子操作**：指不能被线程调度机制中断的操作
  
    + 一旦操作开始，那么他一定能在有可能发生的上下文切换之前执行完毕
    
+ 原子性可以应用于除 `long/double`  之外的所有基本类型之上的简单操作

+ `JVM` 可以将 `64` 位的`long/double`  的都如何写入当作两个分离的 `32` 位操作来执行

    + 可能产生 **字撕裂**（部分被修改）
    + 可以使用 `volatile` 关键字获得原子性

+ 可视性

    + 一个任务做出的修改，即使在不中断的意义上讲是原子性的，对其他任务也可能是不可视的
    + 缓存机制

+ 同步机制强制在处理器系统中，一个任务做出的修改必须在应用中是可视的

+ `volatile` 关键字还确保了应用中的可视性

    + 例如一个域被声明为 `volatile` 
        + 那么一旦对这个域产生了写操作，这个修改将对所有的读操作可见

+ 在非 `volatile` 域上的原子操作不必刷新到主存里去

    + 也就是说其他读取该域的任务也不必看到这个新值
    + **同步也会导致向主存中刷新**
    + 如果一个域完全由 `synchronized` 方法或者语句块来防护，那么就不必设置为 `volatile`

+ 一个任务所做的写操作对于这个任务内部都是可视的

    + 因此若只需要在这个任务内部可视，无需设置为 `volatile`

+ **当一个域的值依赖于他之前的值的时候，`volatile` 就无法工作了**

    + 例如递增计数器

+ **如果某个域的值受到其他域的值的限制，`volatile` 也无法工作**

    + 例如 `Range` 类的边界需要满足限制 `lower<=upper`
    + 例如判断后赋值

    ```java
    import java.util.concurrent.*;
    class TDOB1 implements Runnable {
        private static volatile int cnt = 0;
        private final int upper = 100;
        private boolean first = true;
        @Override
        public void run() {
            // synchronized(this) {
            while(cnt < upper) {
                Thread.yield(); // 这里执行权被抢占
                ++cnt;
            }
            System.out.println(cnt);
            // }
        }
    }
    
    public class TestDependingOnBound {
        public static void main(String...args) {
            TDOB1 td = new TDOB1();
            for(int i = 0;i < 2; ++i) {
                new Thread(td).start();
            }
        }
    }
    
    
    /* Output1
    100
    100
    */
    
    /* Output2
    101
    101
    */
    ```

    

+ 使用 `volatile` 而不是 `synchronized` 的唯一安全情况是类中只有一个可变的域

    + **第一选择是 `synchronized`**

```java
// 以下都是非原子操作
++i;
i++;
i+=2;
```

```assembly

2: getfield      #2                  // Field i:I
5: iconst_1
6: iadd
7: putfield      #2                  // Field i:I

# ...

2: getfield      #2                  // Field i:I
5: iconst_1
6: iadd
7: putfield      #2                  // Field i:I

# ...

2: getfield      #2                  // Field i:I
5: iconst_2
6: iadd
7: putfield      #2                  // Field i:I
```

+ `volatile` 原则
    + 基本上，如果一个域可能会被多个任务同时访问，或者这些任务中至少有一个是写入任务，
    + 那么就应该将这个域设置为 `volatile`



### 21.3.4 原子类

+ `java.util.concurrent.atomic.*`

    + `AtomicInteger` ···

    ```java
    boolean compareAndSet(expectedValue, updateValue);
    ```

    + 这些类被调整为可以使用在某些现代处理器上的可获得的，并且是机器级别上的原子性
    + 用于性能调优

```java
// java.util.concurrent.atomic.AtomicInteger

// ~~~~~non-static method~~~~~ //
public final int get()
public String toString()
public int intValue()
public long longValue()
public float floatValue()
public double doubleValue()
public final void set(int)
public final void lazySet(int)
public final int getAndAdd(int)
public final int accumulateAndGet(int,IntBinaryOperator)
public final int addAndGet(int)
public final boolean compareAndSet(int,int)
public final int decrementAndGet()
public final int getAndAccumulate(int,IntBinaryOperator)
public final int getAndDecrement()
public final int getAndIncrement()
public final int getAndSet(int)
public final int getAndUpdate(IntUnaryOperator)
public final int incrementAndGet()
public final int updateAndGet(IntUnaryOperator)
public final boolean weakCompareAndSet(int,int)
public byte byteValue()
public short shortValue()
public final void wait() throws InterruptedException
public final void wait(long,int) throws InterruptedException
public final native void wait(long) throws InterruptedException
public boolean equals(Object)
public native int hashCode()
public final native Class getClass()
public final native void notify()
public final native void notifyAll()

// ~~~~~constructor~~~~~ //
public AtomicInteger(int)
public AtomicInteger()
```



+ [AtomicIntegerTest](../Example_Code/concurrency/AtomicIntegerTest.java)

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.*;

public class AtomicIntegerTest implements Runnable {
    private AtomicInteger i = new AtomicInteger(0);
    public int getValue() { return i.get(); }
    private void evenIncrement() { i.addAndGet(2); }
    public void run() {
        while(true)
            evenIncrement();
    }
    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {
            public void run() {
                System.err.println("Aborting");
                System.exit(0);
            }
        }, 5000); // Terminate after 5 seconds
        ExecutorService exec = Executors.newCachedThreadPool();
        AtomicIntegerTest ait = new AtomicIntegerTest();
        exec.execute(ait);
        while(true) {
            int val = ait.getValue();
            if(val % 2 != 0) {
                System.out.println(val);
                System.exit(0);
            }
        }
    }
}
```

+ 通常还是使用 `synchronized` 或者 `Lock`，原子类的使用需要一定经验



### 21.3.5 临界区

+ 只希望防止多个线程同时访问方法内部的部分代码而不是访问整个方法
    + 通过这种方法分离出来的代码段称为**临界区**（`critical section`）
    + 也被称为 **同步控制块**

```java
// 方法1
synchronized(syncObject) {
    // Code
}

// 方法2
lock.lock();
try {
    // Code
} finally {
    lock.unlock();
}
```

+ `synchronized` 用来指定某个对象，此对象的锁被用来对花括号内的代码进行同步控制
+ [CriticalSection](../Example_Code/concurrency/CriticalSection.java)
    + **一般使用同步控制块的方式进行同步控制，所以对象不加锁的时间更长**
    + 在安全的情况下让尽可能更多的线程访问
+ 设计方法
    + 别人给了你一个非线程安全的 `Pair`，你想要在一个多线程的环境中使用
    + 可以创建一个 `PairManager` 用于控制对 `Pair` 的访问（`synchronized`）
+ **模板方法**：一种设计模式
    + 抽象出相同部分作为基类（可以是 `abstract`）
    + 然后继承实现



### 21.3.6 在其它对象上同步

+ 一般 `synchronized(this)`
+ 可以只对某个对象加锁，这样能够减小同步的范围，减少不必要的等待
+ [SyncOtherObj](SyncOtherObj.java)
    + `f,g` 一把锁，`h` 一把锁

```java
public class SyncOtherObj {
    private Object obj = new Object();
    public synchronized void f() {
        for(int i = 0;i < 10; ++i) {
            System.out.println("f()");
        }        
    }
    public void g() {
        synchronized(obj) {
            for(int i = 0;i < 10; ++i) {
                System.out.println("g()");
            }        
        }
    }
    public void h() {
        synchronized(obj) {
            for(int i = 0;i < 10; ++i) {
                System.out.println("h()");
            }        
        }
    }
}
```



### 21.3.7 线程本地存储

+ 防止任务在共享资源上所产生冲突的第二种方式是根除对变量的共享
+ 线程本地存储是一种自动化机制，可以为使用相同变量的不同线程都创建不同的存储
+ 创建和管理线程共享本地存储可以由 `java.lang.ThreadLocal` 类实现
+ 示例代码 [ThreadLocalVariableHolder](../Example_Code/concurrency/ThreadLocalVariableHolder.java)

```java
public class ThreadLocalVariableHolder {
    private static ThreadLocal<Integer> value =
        new ThreadLocal<Integer>() {
        private Random rand = new Random(47);
        protected synchronized Integer initialValue() {
            return rand.nextInt(10000);
        }
    };
    public static void increment() {
        value.set(value.get() + 1);
    }
    public static int get() { return value.get(); }
    // ...
}
```

+ 相对于非 `static` 变量而言，具有更小的创建新对象的开销
    + 当创建对象开销巨大时尤其适用  `ThreadLocal`



## 21.4 终结任务

### 21.4.1 装饰性花园

+ 一个示例 [OrnamentalGarden](../Example_Code/concurrency/OrnamentalGarden.java)
    + 计数每一个门进来的人数，计数进入花园的总人数

```java
//: concurrency/OrnamentalGarden.java
import java.util.concurrent.*;
import java.util.*;
import static net.mindview.util.Print.*;

class Count {
    private int count = 0;
    private Random rand = new Random(47);
    // Remove the synchronized keyword to see counting fail:
    public synchronized int increment() {
        int temp = count;
        if(rand.nextBoolean()) // Yield half the time
            Thread.yield();
        return (count = ++temp);
    }
    public synchronized int value() { return count; }
}

class Entrance implements Runnable {
    private static Count count = new Count();
    private static List<Entrance> entrances =
        new ArrayList<Entrance>();
    private int number = 0;
    // Doesn't need synchronization to read:
    private final int id;
    private static volatile boolean canceled = false;
    // Atomic operation on a volatile field:
    public static void cancel() { canceled = true; }
    public Entrance(int id) {
        this.id = id;
        // Keep this task in a list. Also prevents
        // garbage collection of dead tasks:
        entrances.add(this);
    }
    public void run() {
        while(!canceled) {
            synchronized(this) {
                ++number;
            }
            print(this + " Total: " + count.increment());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch(InterruptedException e) {
                print("sleep interrupted");
            }
        }
        print("Stopping " + this);
    }
    public synchronized int getValue() { return number; }
    public String toString() {
        return "Entrance " + id + ": " + getValue();
    }
    public static int getTotalCount() {
        return count.value();
    }
    public static int sumEntrances() {
        int sum = 0;
        for(Entrance entrance : entrances)
            sum += entrance.getValue();
        return sum;
    }
}

public class OrnamentalGarden {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 5; i++)
            exec.execute(new Entrance(i));
        // Run for a while, then stop and collect the data:
        TimeUnit.SECONDS.sleep(3);
        Entrance.cancel();
        exec.shutdown();
        if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
            print("Some tasks were not terminated!");
        print("Total: " + Entrance.getTotalCount());
        print("Sum of Entrances: " + Entrance.sumEntrances());
    }
}
```

```java
ExecutorService exec = Executors.newCachedThreadPool();
// 设置超时时间,若时间内所有任务结束返回 true, 否则返回false, 终止所有任务
exec.awaitTermination(250, TimeUnit.MILLISECONDS)
```



### 21.4.2 在阻塞时终结

#### 21.4.2.1 线程状态

+ **新建** `new`
    + 当线程被创建时，它只会短暂的处于这种状态，
    + 此时它已经分配了必需的系统资源，并执行了初始化，
    + 此时线程已经有资格获得 `CPU` 时间了，
    + 之后调度器将把这个线程装变为可运行状态或阻塞状态
+ **就绪** `runnable`
    + 在这种状态下，只要调度器把时间片分配给线程，线程就可以运行。
    + 也就是说，在任意时刻，线程可以运行也可以不运行。
    + 只要调度器分配时间片给线程，它就可以运行
    + **这不同于死亡和阻塞状态**
+ **阻塞** `blocked`
    + 线程能够运行，但有某个条件阻止它的运行。
    + 当线程处于阻塞状态时，调度器将忽略线程，不会分配给线程任何 `CPU` 时间。
    + 直到线程重新进入了就绪状态，他才有可能执行操作
+ **死亡** `dead`
    + 处于死亡或者终止状态的线程将不再是可调度的，并且再也不会得到 `CPU` 时间
    + 任务死亡的通常方式通常是从 `run()` 方法返回，**但是任务的线程还可以被中断**



#### 21.4.2.2 进入阻塞状态

+ 进入阻塞状态的可能原因 `4`
    + 通过调用 `sleep(milliseconds)` **休眠**
        + 直到指定时间结束
    + 通过调用  `wait()` 挂起线程
        + 直到线程得到了 `notify()/notifyAll()` 或者等价的 `signal()/signalAll()`
    + 任务在等待某个输入输出完成 `I/O`
    + 调用同步控制方法时对象锁不可用（被另一个任务获得锁）
+ 旧式风格的代码可能会通过 `suspend()/resume()` 方法来阻塞和唤醒线程
    + 现代代码比较少用，可能会 **死锁** （`deadlock`）
    + `stop()` 方法也被废止
        + 因为其不释放线程获得的锁
        + 线程处于不一致的状态时（受损状态），其他任务可以浏览修改他们
        + 有微妙的错误



### 21.4.3 中断

+ 当你打断被阻塞的任务时，可能需要清理资源
    + 因此在任务的 `run()` 方法中间打断，更像是抛出异常
+ `Thread.interrupt()` 方法可以终止被阻塞的任务
    + 会设置线程的中断状态 
        + 如果一个线程已经被阻塞，或者试图执行一个阻塞操作
        + 那么设置这个线程的中断状态将抛出异常 `InterruptedException`
        + 当抛出该异常或者该任务调用 `Thread.interrupted()` 时，中断状态将被复位
+ `Executor.shutdownNow()`
    + 发送一个 `interrupt()` 调用给它启动的所有线程
+ `Executor.submit()`
    + 返回一个泛型 `Future<?>` 
    + 其中有一个未修饰的参数，因此 `get()` 方法不会被调用
    + 有 `cancel()` 方法
        + 如果将 `ture` 作为参数传递给 `cancel()` ，
        + 就会拥有在该线程上调用 `interrupt()` 的权限
+ 示例代码 [Interrupting](../Example_Code/concurrency/Interrupting.java)

```java
static void test(Runnable r) throws InterruptedException{
    Future<?> f = exec.submit(r);
    TimeUnit.MILLISECONDS.sleep(100);
    print("Interrupting " + r.getClass().getName());
    f.cancel(true); // Interrupts if running
    print("Interrupt sent to " + r.getClass().getName());
}
```

+ 示例代码中包括可中断的阻塞（休眠），不可中断的阻塞（`I/O`，`synchronized` 代码块）
    + 使用了不通过异常处理不可中断的阻塞的方式
+ 从输出中可以看到，我们能够中断对 `sleep()` 的调用
    + 或者任何要求抛出 `InterruptedException` 的调用
+ `Executor.shutdownNow()` 不能中断正在试图获取 `synchronized` 锁或者试图执行 `I/O` 操作的线程
+ 将示例代码 [Interrupting](../Example_Code/concurrency/Interrupting.java)  `main()` 修改如下

```java
public static void main(String[] args) throws Exception {
    ExecutorService exec = Executors.newCachedThreadPool();
    exec.execute(new SynchronizedBlocked());
    TimeUnit.SECONDS.sleep(3);
    exec.shutdownNow();
    TimeUnit.SECONDS.sleep(3);
    print("Aborting with System.exit(0)");
    System.exit(0); // ... since last 2 interrupts failed
}
/* Output
Trying to call f()
Aborting with System.exit(0)
*/
```

+ 将示例代码 [Interrupting](../Example_Code/concurrency/Interrupting.java)  `main()` 修改如下

```java
public static void main(String[] args) throws Exception {
    exec.execute(new IOBlocked(System.in));
    exec.shutdownNow();
    TimeUnit.SECONDS.sleep(3);
    print("Aborting with System.exit(0)");
    System.exit(0); // ... since last 2 interrupts failed
}
/* Output
Waiting for read():
Aborting with System.exit(0)
*/
```

+ 解决方案，关闭任务在其上发生阻塞的底层资源
+ [CloseResource](../Example_Code/concurrency/CloseResource.java)

```java
import java.net.*;
import java.util.concurrent.*;
import java.io.*;
import static net.mindview.util.Print.*;

public class CloseResource {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(8080);
        InputStream socketInput =
            new Socket("localhost", 8080).getInputStream();
        exec.execute(new IOBlocked(socketInput));
        exec.execute(new IOBlocked(System.in));
        TimeUnit.MILLISECONDS.sleep(100);
        print("Shutting down all threads");
        exec.shutdownNow();
        TimeUnit.SECONDS.sleep(1);
        print("Closing " + socketInput.getClass().getName());
        socketInput.close(); // Releases blocked thread
        TimeUnit.SECONDS.sleep(1);
        print("Closing " + System.in.getClass().getName());
        System.in.close(); // Releases blocked thread
    }
} 
```

+ 在输入流上调用 `close()` 之后，任务将解除阻塞
+ `nio` 类提供了更为人性化的 `I/O` 中断
+ [NIOInterruption](../Example_Code/concurrency/NIOInterruption.java)

```java
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.*;
import java.io.*;
import static net.mindview.util.Print.*;

class NIOBlocked implements Runnable {
    private final SocketChannel sc;
    public NIOBlocked(SocketChannel sc) { this.sc = sc; }
    public void run() {
        try {
            print("Waiting for read() in " + this);
            sc.read(ByteBuffer.allocate(1));
        } catch(ClosedByInterruptException e) {
            print("ClosedByInterruptException");
        } catch(AsynchronousCloseException e) {
            print("AsynchronousCloseException");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        print("Exiting NIOBlocked.run() " + this);
    }
}

public class NIOInterruption {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(8080);
        InetSocketAddress isa =
            new InetSocketAddress("localhost", 8080);
        SocketChannel sc1 = SocketChannel.open(isa);
        SocketChannel sc2 = SocketChannel.open(isa);
        Future<?> f = exec.submit(new NIOBlocked(sc1));
        exec.execute(new NIOBlocked(sc2));
        exec.shutdown();
        TimeUnit.SECONDS.sleep(1);
        // Produce an interrupt via cancel:
        f.cancel(true);
        TimeUnit.SECONDS.sleep(1);
        // Release the block by closing the channel:
        exec.shutdownNow(); // 被阻塞的 I/O 也能响应 interrupt
        // sc2.close();
    }
}
```



#### 21.4.2.3 被互斥所阻塞 

+ 一个对象可以多次获得锁（计数）
+ [MultiLock](../Example_Code/concurrency/MultiLock.java)

```java
public synchronized void f() { g(); }
public synchronized void g() {}
```

+ 无论在任何时刻，只要任务以不可中断方式被阻塞，那么就有潜在的会锁住程序的可能

+ `Java SE5` 并发类库新特性
  
    ```java
    // java.util.concurrent.locks.ReentrantLock.java
    public void lockInterruptibly() 
        throws InterruptedException{/*...*/}
    ```
    
    + 在 `ReentrantLock` 上阻塞的任务具备可以被中断的能力
    + 与 `synchronized` 方法或者临界区的任务不同
    
+ [CanNotBeInteruppted](CanNotBeInteruppted.java)

```java
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;

class CNBIRunnable implements Runnable {
    private Lock lock = new ReentrantLock();
    public CNBIRunnable() {
        lock.lock();
    }
    public void run() {
        lock.lock();
        System.out.println("Run");
    }
}

public class CanNotBeInterrupted {
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new CNBIRunnable());
        exec.shutdownNow();
    }
}
```

+ [CanBeInteruppted](CanBeInteruppted.java)

```java
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;

class CBIRunnable implements Runnable {
    private Lock lock = new ReentrantLock();
    public CBIRunnable() {
        lock.lock();
    }
    public void run() {
        try {
            lock.lockInterruptibly();
            System.out.println("Run");
        } catch(InterruptedException e) {
            System.out.println("Interrupted while waiting!");
        }
    }
}

public class CanBeInterrupted {
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        Thread t = new Thread(new CBIRunnable());
        exec.execute(t);
        try{
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        exec.shutdownNow();
    }
}
/* Output 
Interrupted while waiting!
*/
```

+ `Thread.interrupt()` 方法可以打断由互斥引起的阻塞
    + 必须对应 `Thread.start()`
    + 不能响应 `ExecutorService.execute(Thread)`

```java
Thread t = new Thread(new Runnable(){
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println(sc.next());
    }
});
t.start();
try{
    TimeUnit.SECONDS.sleep(1);
} catch (InterruptedException e){
    e.printStackTrace();
}
t.interrupt(); // 不能中断 I/O 引发的阻塞
```

```java
Thread t = new Thread(new CBIRunnable()); // 上面的类
t.start();
try{
    TimeUnit.SECONDS.sleep(1);
} catch (InterruptedException e){
    e.printStackTrace();
}
t.interrupt(); // 能够中断互斥引发的阻塞(ReentrantLock.lockInterruptibly())
```



### 21.4.4 检查中断

```java
// java.lang.Thread,java
// 检查当前线程是否被中断过
public static boolean interrupted(){/*...*/}
```

+ [InterruptingIdiom](../Example_Code/concurrency/InterruptingIdiom.java)
    + 惯用法，在实例化一个对象之后马上进入 `try-finally` 块保证清理工作的进行

```java
public void run() {
    try {
        while(!Thread.interrupted()) {
            // point1
            NeedsCleanup n1 = new NeedsCleanup(1);
            // Start try-finally immediately after definition
            // of n1, to guarantee proper cleanup of n1:
            try {
                print("Sleeping");
                TimeUnit.SECONDS.sleep(1);
                // point2
                NeedsCleanup n2 = new NeedsCleanup(2);
                // Guarantee proper cleanup of n2:
                try {
                    print("Calculating");
                    // A time-consuming, non-blocking operation:
                    for(int i = 1; i < 2500000; i++)
                        d = d + (Math.PI + Math.E) / d;
                    print("Finished time-consuming operation");
                } finally {
                    n2.cleanup();
                }
            } finally {
                n1.cleanup();
            }
        }
        print("Exiting via while() test");
    } catch(InterruptedException e) {
        print("Exiting via InterruptedException");
    }
}
```

