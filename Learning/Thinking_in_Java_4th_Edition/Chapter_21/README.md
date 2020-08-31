# Chapter_21 并发(part1)

> [总目录](../README.md)

---

[TOC]

---

+ [part1](README.md)
+ [part2](README-part2.md)
+ [part3](README-part3.md)



+ **顺序编程** `vs` **并发编程**
+ 并发编程：快，充分利用计算资源
+ 多线程
    + `Web`
    + `GUI`



## 21.1 并发的多面性

+ 速度，设计可管理性



### 21.1.1 更快的执行

+ 并发通常是提高运行在单处理器上的程序的性能
    + **上下文切换** 需要时间（任务切换）
    + **阻塞**：由于某些原因导致任务不能再继续执行（通常是 `I/O`）
    + 由于阻塞的存在，若有并发则可以继续执行程序的其他部分，若无并发则只能停下来
+ 常见示例：**事件驱动的编程**
+ 实现并发最直接的方式是在操作系统级别使用 **进程**
    + 进程地址空间相互独立，通信困难
+ `Java`采取了更加传统的方式，在顺序性语言的基础上提供对**线程**的支持
    + 在单一进程中创建任务
    + 一方面是为了保证“编写一次，到处运行”（有些操作系统不支持多进程）



### 21.1.2 改进代码的设计

+ 单 `CPU` 上的并发程序在任意时刻只会执行一个任务
    + 因此理论上讲，可以不用任何任务编写出相同效果的程序
    + 但是并发提供了一个重要的组织结构上的好处：简化程序设计
    + 而且很多程序，缺少并发的支持，比较难解决（例如仿真）
+ 完整的仿真可能会设计大量的任务，大量元素都需要独立动作
    + **在 `java` 中，我们通常要假定你不会获得足够的线程**
    + 解决方案是使用 **协作多线程**
+ `java` 的线程机制是 **抢占式** 的
    + 调度机制会周期性的中断线程，将上下文切换到另一个线程，
    + 从而为每一个线程都提供时间片，使得每个线程都会分配到数量合理的时间取驱动它的任务
+ **协作式系统** 中
    + 每个系统都会自动的放弃控制，
    + 这要求程序员要有意识的在每个任务中插入某种类型的让步语句
    + 好处
        + 上下文切换的开销通常比抢占式系统低廉
        + 同时执行的线程数量在理论上没有任何限制
+ 但是注意，某些协作式系统并未设计可以在多个处理器之间分布任务



## 21.2 基本的线程机制

+ 并发编程可以将程序划分为多个分离的、独立运行的任务。
+ 通过多线程机制，这些独立任务（子任务）中的每一个都将由**执行线程**来驱动
+ 一个线程就是在进程中的一个单一的顺序控制流
+ 底层机制是切分 `CPU` 时间
+ 当使用线程时，`CPU` 将轮流给每个人物分配其占用时间
    + `windows` 使用 **时间切片机制**
+ 线程的一个好处就是，无须知道当前机器是否存在多个 `CPU`，让程序员从这个层次中抽身出来



### 21.2.1 定义任务

+ 实现 `Runnable` 接口即可

```java
package java.lang;
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
```

```java
public class LiftOff implements Runnable {
    @Override
    public void run() { /* ... */ }
    /* ... */
}
```

+ 对 **线程调度器** 的一种**建议**，建议将执行权让给其他线程
+ 只是单纯的实现  `Runnable` 接口并不能产生任何内在的线程能力
    + 要实现线程行为，必须将其显式的将一个任务附着到线程上



### 21.2.2 Thread 类

```java
Thread t = new Thread(new LiftOff());
t.start(); // 开启一个线程执行run()方法,原始线程继续执行接下来的代码
```

+ 多线程引发的问题
    + `++cnt` 不是线程安全的

```java
public class Shine implements Runnable {
    static int cnt = 0;
    static final int N = 10000;
    @Override
    public void run() {
        ++cnt;
        if(cnt == N) {
            System.out.format("reach \'N\'(%d)\n", N);
        }
    }
    public static void main(String...args) throws Exception {
        for(int i = 0;i < N; ++i) {
            new Thread(new Shine()).start();
        }
    }
}

/* Output1
*/

/* Output2
reach 'N'(10000)
*/
```

+ [LiftOff](../Example_Code/concurrency/LiftOff.java)

```java
//: concurrency/LiftOff.java
public class LiftOff implements Runnable {
    /* ... */
    public void run() {
        while(countDown-- > 0) {
            System.out.print(status());
            Thread.yield(); // 建议让出执行权，便于看出来线程调度器的调度
        }
    }
}
```

+ [MoreBasicThreads](../Example_Code/concurrency/MoreBasicThreads.java)

```java
//: concurrency/MoreBasicThreads.java
public class MoreBasicThreads {
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++)
            new Thread(new LiftOff()).start();
        System.out.println("Waiting for LiftOff");
    }
}
/* Output(某一次执行的结果)
#0(9), #0(8), #0(7), #0(6), #0(5), #0(4), #0(3), #0(2), #0(1), #3(9), Waiting for LiftOff
#2(9), #2(8), #1(9), #1(8), #2(7), #2(6), #3(8), #0(Liftoff!), #4(9), #4(8), #4(7), #4(6), #4(5), #3(7), #2(5), #2(4), #1(7), #1(6), #2(3), #2(2), #3(6), #4(4), #3(5), #3(4), #3(3), #3(2), #3(1), #3(Liftoff!), #2(1), #2(Liftoff!), #1(5), #4(3), #1(4), #4(2), #4(1), #4(Liftoff!), #1(3), #1(2), #1(1), #1(Liftoff!),
*/
```

+ 线程调度器自动对线程进行调度
    + `MoreBasicThreads` 便是一个例子
        + 每次运行结果可能不一致，因为线程调度机制是非确定性的
    + 如果运行的机器上存在多个处理器，线程调度器将会在这些处理器之间默默分发线程
+ 不同版本的 `JDK` 也不太一致
    + 较早的 `JDK` 不会频繁的对时间切片
    + 较晚的 `JDK` 看起来会产生更好的时间切片行为
+ **在编写使用线程的代码的时候，尽可能的保守**
+ 当 `main()` 创建 `Thread` 线程的时候，并没有捕获任何对这些对象的引用
    + 每个 `Thread` 都注册了他自己，因此确实有一个对它的引用
    + 而且在它的任务退出 `run()` 之前不会被垃圾回收器清除



### 21.2.3 使用 Executor

+ `java.util.concurrent.Executor`
+ [JustRun](JustRun.java)

```java
public class JustRun implements Runnable {
    static int cnt = 0;
    private int id = ++cnt;
    @Override
    public void run() {
        Thread.yield();
        System.out.format("%d@run()\n", id);
    }
}
```

+ [UseJustRunExecutor](UseJustRunExecutor.java)

```java
import java.util.concurrent.*;
public class UseJustRunExecutor {
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0;i < 5; ++i) {
            exec.execute(new JustRun());
        }
        exec.shutdown(); // 拒绝之后台添加新的任务到线程池中
        // exec.execute(new JustRun());
        // java.util.concurrent.RejectedExecutionException
    }
}
```

+ 可以将 `newCachedThreadPool` 替换为其他线程池
    + `newCachedThreadPool`
        + 在程序执行过程中通常会创建和所需数量相同的线程，
        + 然后在他回收旧线程时停止创建新线程，
        + 因此是 `Executor` 的首选，只有引发问题时候，才开始选择 `FixedThreadPool`
    + `newFixedThreadPool`
        + 通过一开始执行开销较大的线程分配，也可以控制线程数量
        + 因为有限的大小，因此不会出现滥用可获得的资源的情况
    + `newSingleThreadExecutor`
        + 就像是线程数量为 `1` 的 `FixedThreadPool`
        + 这对于你希望在另一个线程中连续运行的任务（长期存活的任务）很有用
            + 例如监听的 `socket` 连接
        + 提交了多个任务则会排队（按照提交的顺序排队）
            + 自动实现序列化，自动维护悬挂任务队列

```java
public class Executors {
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
    public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
}
```



### 21.2.4 从任务中产生返回值

+ `Runnable` 不返回任何值，`Callable` 在任务完成时返回一个值

```java
package java.util.concurrent;
@FunctionalInterface
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```

+ `Future<T>.get()` **阻塞**

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
public class ImCallable implements Callable<String> {
    static int cnt = 0;
    private int id = ++cnt;
    @Override
    public String call() {
        Thread.yield();
        String ret = String.format("%d@run()", id);
        System.out.println(ret);
        return ret;
    }
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<String>> result = new ArrayList<>();
        for(int i = 0;i < 5; ++i) {
            result.add(exec.submit(new ImCallable()));
        }
        System.out.println("~~~~~~~~~~");
        for(Future<String> fs : result) {
            try {
                // get() 阻塞
                System.out.println(fs.get());
            } catch(InterruptedException e) {
                System.out.println(e);
                return;
            } catch(ExecutionException e) {
                System.out.println(e);
            } finally {
                exec.shutdown();
            }
        }
    }
}
/* Output(one possible output)
~~~~~~~~~~
3@run()
2@run()
1@run()
5@run()
4@run()
1@run()
2@run()
3@run()
4@run()
5@run()
*/
```

+ `submit` 产生一个 `Future` 对象
+ `Future` 对象的 `get()` 方法是 **阻塞** 的
    + `isDone()` 看是否完成



### 21.2.5 休眠

```java
try {
    // sleep
    // Thread.sleep(100); // old-style
    TimeUnit.MILLISECONDS.sleep(100); // Java SE 5-6 style
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

+ [JustRunSleepVersion](JustRunSleepVersion.java)

```java
import java.util.concurrent.TimeUnit;
public class JustRunSleepVersion implements Runnable {
    static int cnt = 0;
    private int id = ++cnt;
    @Override
    public void run() {
        try {
            // sleep
            // Thread.sleep(100); // old-style
            TimeUnit.MILLISECONDS.sleep(100); // Java SE 5-6 style
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.format("%d@run()\n", id);
    }
    public static void main(String...args) {
        for(int i = 0;i < 5; ++i) {
            new Thread(new JustRunSleepVersion()).start();
        }
    }
}
```

+ 注意这里的 `run()` 不允许抛出异常（因为覆盖的父类方法没有抛出异常）
    + 因此必须在 `run()` 内部处理
+ 也不能跨线程抛给 `main()`



### 21.2.6 优先级

+ 调度器倾向于让优先级最高的线程先执行
    + 不会引发死锁

```java
/**
 * The minimum priority that a thread can have.
 */
public final static int MIN_PRIORITY = 1;

/**
 * The default priority that is assigned to a thread.
 */
public final static int NORM_PRIORITY = 5;

/**
 * The maximum priority that a thread can have.
 */
public final static int MAX_PRIORITY = 10;
```

+ 线程的默认优先级和创建这个线程的线程优先级相同
    + 可以通过 `setPriority(int)` 设置
    + 注意不能在构造器中设置，需要在 `run()` 中设置
        + 否则设置的优先级影响的是调用构造函数的线程
        + 例子 [PriorityInConstructor](PriorityInConstructor.java)

```java
// java.lang.Thread.java
public String toString() {
    ThreadGroup group = getThreadGroup();
    if (group != null) {
        return "Thread[" + getName() + "," + getPriority() + "," +
            group.getName() + "]";
    } else {
        return "Thread[" + getName() + "," + getPriority() + "," +
            "" + "]";
    }
}
```

+ [UsePriority](UsePriority.java)
    + 优先级不是太明显，操作时间不是太长
    + 另外一方面映射也不是很好，尽量只使用 `MAX_PRIORITY`，`NORM_PRIORITY`，`MIN_PRIORITY`
+ 通过 `Thread.currentThread()` 来获取当前线程

```java
/**
 * Returns a reference to the currently executing thread object.
 * @return  the currently executing thread.
 */
public static native Thread currentThread();
```



### 21.2.7 让步

```java
Thread.yield() // 只是一种暗示
```



### 21.2.8 后台线程

+ `daemon`
    + 后台线程
    + 指在程序运行的时候在后台提供一种通用服务的线程，
    + 并且这种线程并不属于程序中不可或缺的部分
+ **当所有的非后台线程结束时，程序也就终止了，同时会杀死进程中的所有后台线程**
    + 反之，若还有非后台线程运行，程序就不会终止
    + 执行 `main()` 的就是一个非后台进程
+ 必须在线程启动之前设置为后台进程 `setDaemon(true)`

+ 使用 `ThreadFactory`

```java
// 示例代码
import java.util.concurrent.ThreadFactory;
public class DaemonThreadFactory implements ThreadFactory {
    // 生成一个后台线程
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }
}
```

```java
// 示例代码
import java.util.concurrent.*;
public class DaemonFromFactory implements Runnable {
    public void run() {
        try {
            while(true) {
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(Thread.currentThread() + " " + this);
            }
        } catch(InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
    public static void main(String[] args) throws Exception {
        // 使用 ThreadFactory
        ExecutorService exec = Executors.newCachedThreadPool(
            new DaemonThreadFactory());
        for(int i = 0; i < 10; i++)
            exec.execute(new DaemonFromFactory());
        System.out.println("All daemons started");
        TimeUnit.MILLISECONDS.sleep(500);
    }
}
```

+ 将 `ThreadPoolExcecutor` 进行包装

```java
// java.util.concurrent.ThreadPoolExecutor
// 一些缺省参数可以查看 JDK
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler) {
    // ...
}
```

```java
// 示例代码
package net.mindview.util;
import java.util.concurrent.*;
public class DaemonThreadPoolExecutor
    extends ThreadPoolExecutor {
    public DaemonThreadPoolExecutor() {
        super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
              new SynchronousQueue<Runnable>(),
              new DaemonThreadFactory());
    }
}
```

+ 后台线程创建的任意线程都是后台线程
    + [ThreadFromDaemonThread](ThreadFromDaemonThread.java)
+ 后台线程可能不会执行 `finally` 子句
    + [DaemonDoNotRunFinally](DaemonDoNotRunFinally.java)
    + 当最后一个非后台线程终止的时候，后台线程会 “突然” 终止
+ `main()` 一旦退出，`JVM` 马上关闭所有后台进程，而不希望你有任何的确认方式



### 21.2.9 编码的变体

+ 继承自 `Thread`，而且需要覆盖 `run()` 方法

```java
// 示例代码
public class SimpleThread extends Thread {
    private int countDown = 5;
    private static int threadCount = 0;
    public SimpleThread() {
        // Store the thread name:
        super(Integer.toString(++threadCount));
        start();
    }
    @Override
    public String toString() {
        return "#" + getName() + "(" + countDown + "), ";
    }
    @Override
    public void run() {
        while(true) {
            System.out.print(this);
            if(--countDown == 0)
                return;
        }
    }
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++)
            new SimpleThread();
    }
}
```

```java
// 原始的 run() 方法
// java.lang.Thread.java
public class Thread implements Runnable {
    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }
    // ...
}
```

+ 自管理的 `Runnable`
    + 这种方式只是实现了一个接口，因此可以继承自其他类

```java
private Thread t = new Thread(this);
```

```java
// 示例代码
public class SelfManaged implements Runnable {
    private int countDown = 5;
    private Thread t = new Thread(this);
    public SelfManaged() { t.start(); }
    public String toString() {
        return Thread.currentThread().getName() +
            "(" + countDown + "), ";
    }
    public void run() {
        while(true) {
            System.out.print(this);
            if(--countDown == 0)
                return;
        }
    }
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++)
            new SelfManaged();
    }
}
```

+ 注意这里的 `start()` 是在构造器里面调用的
    + 这个例子比较简单，因此可能是正确的
    + 但是在构造器中启动线程可能会有问题
        + 可能这个时候构造器还未构造完成
        + 这意味着这个线程能够访问不稳定状态的对象
+ 优先选择 `Executor` ，而不是显示的创建 `Thread` 对象
+ 内部类实现，匿名类实现
    + `Java SE8` 可以用 `lambda` 表达式

```java
import java.util.concurrent.*;
public class ThreadVariations {
    // 内部类
    private class InnerThread implements Runnable {
        @Override
        public void run() {
            System.out.println("InnerClass Runnable: " + Thread.currentThread());
        }
    }
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        // 匿名类实现 Runnable
        exec.execute(new Runnable(){
            @Override
            public void run() {
                System.out.println("Anonymous Runnable: " + Thread.currentThread());
            }
        });
        // 内部类实现
        exec.execute((new ThreadVariations()).new InnerThread());
        // shutdown
        exec.shutdown();
    }
}
/* Output
Anonymous Runnable: Thread[pool-1-thread-1,5,main]
InnerClass Runnable: Thread[pool-1-thread-1,5,main]
*/
```

+ 内部类和匿名类配合上述方式实现
    + 示例代码 [ThreadVariations](../Example_Code/concurrency/ThreadVariations.java)



### 21.2.10 术语

+ `java` 中，`Thread` 类本身并不执行任何操作，他只是驱动赋予他的任务
+ 表述
    + 任务：将要执行的工作
    + 线程：驱动机制



### 21.2.1 加入一个线程

+ 一个线程可以在其他线程之上调用 `join()` 方法
    + 效果是在等待一段时间，直到第二个线程结束再继续执行
    + 具体说明
        + 如果线程 `a` 在另一个线程 `t` 上调用 `t.join()`，
        + 那么 `a` 被挂起，直到目标线程 `t` 结束之后 ，`a` 重新开始执行
+ 也可以设置一个超时参数，这样目标线程在超时后还没结束的话，`join()` 方法总能返回
+ 对 `join()` 的调用可以被打断
    + 在调用线程上调用 `interrupt()` 方法，需要 `try...catch...`
+ 示例代码 [Joining](../Example_Code/concurrency/Joining.java)

```java
// 示例代码
import static net.mindview.util.Print.*;
class Sleeper extends Thread {
    private int duration;
    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        start();
    }
    public void run() {
        try {
            sleep(duration);
        } catch(InterruptedException e) {
            print(getName() + " was interrupted. " +
                  "isInterrupted(): " + isInterrupted());
            return;
        }
        print(getName() + " has awakened");
    }
}

class Joiner extends Thread {
    private Sleeper sleeper;
    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }
    public void run() {
        try {
            sleeper.join();
        } catch(InterruptedException e) {
            print("Interrupted");
        }
        print(getName() + " join completed");
    }
}

public class Joining {
    public static void main(String[] args) {
        Sleeper
            sleepy = new Sleeper("Sleepy", 1500),
        grumpy = new Sleeper("Grumpy", 1500);
        Joiner
            dopey = new Joiner("Dopey", sleepy),
        doc = new Joiner("Doc", grumpy);
        grumpy.interrupt();
    }
}
/* Output:
Grumpy was interrupted. isInterrupted(): false
Doc join completed
Sleepy has awakened
Dopey join completed
*/
```



### 21.2.12 创建有响应的用户界面

+ 示例代码 [ResponsiveUI](../Example_Code/concurrency/ResponsiveUI.java)
    + 通过 `setDaemon(true)`



### 21.2.13 线程组

+ 线程组持有一个线程集合
    + **不成功的尝试**
+ 具体内容可以查看 `Thinking in Java(2nd)`



### 21.2.14 捕获异常

+ 一旦异常逃逸出 `run()` 方法，则会直接被丢到控制台
    + 线程组可以捕获异常
    + `Java SE5` 可以通过 `Executor` 来解决
+ [TestExceptionInThread](TestExceptionInThread.java)
    + **为什么？**

```java
import java.util.concurrent.*;
public class TestExceptionInThread {
    public static void main(String...args) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    throw new RuntimeException();
                }
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
/* Output
*/
```

+ [TestExceptionInThread2](TestExceptionInThread2.java)

```java
import java.util.concurrent.*;
public class TestExceptionInThread2 {
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            exec.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("ERROR");
            }});
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            exec.shutdown();
        }
    }
}

/* Output
Exception in thread "pool-1-thread-1" java.lang.RuntimeException: ERROR
    at TestExceptionInThread2$1.run(TestExceptionInThread2.java:15)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
    at java.lang.Thread.run(Thread.java:748)
*/
```

+ `Java SE5` 添加的新接口：`Thread.UncaughtExceptionHandler`
    + 允许为每一个 `Thread` 对象附着一个异常处理器
+ `Thread.UncaughtExceptionHandler.uncaughtException()`
    + 上述方法会在线程因为因未被捕获的异常而临近死亡的时候调用
+ 示例代码 [CaptureUncaughtException](../Example_Code/concurrency/CaptureUncaughtException.java)

```java
// 示例代码
class ExceptionThread2 implements Runnable {
    public void run() {
        Thread t = Thread.currentThread();
        System.out.println("run() by " + t);
        System.out.println(
            "eh = " + t.getUncaughtExceptionHandler());
        throw new RuntimeException();
    }
}

class MyUncaughtExceptionHandler implements
    Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("caught " + e);
    }
}

class HandlerThreadFactory implements ThreadFactory {
    public Thread newThread(Runnable r) {
        System.out.println(this + " creating new Thread");
        Thread t = new Thread(r);
        System.out.println("created " + t);
        t.setUncaughtExceptionHandler(
            new MyUncaughtExceptionHandler());
        System.out.println(
            "eh = " + t.getUncaughtExceptionHandler());
        return t;
    }
}

public class CaptureUncaughtException {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool(
            new HandlerThreadFactory());
        exec.execute(new ExceptionThread2());
        // 这里应该加上
        exec.shutdown();
    }
}
/* Output: (90% match)
HandlerThreadFactory@de6ced creating new Thread
created Thread[Thread-0,5,main]
eh = MyUncaughtExceptionHandler@1fb8ee3
run() by Thread[Thread-0,5,main]
eh = MyUncaughtExceptionHandler@1fb8ee3
caught java.lang.RuntimeException
*/
```

+ 更方便的方式
    + 设置默认异常处理器
    + 局限性在于不能为每个不一样设置特殊的异常处理器
    + 使用于异常都一致的情况
+ 示例代码 [SettingDefaultHandler](../Example_Code/concurrency/SettingDefaultHandler.java)

```java
import java.util.concurrent.*;
public class SettingDefaultHandler {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(
            new MyUncaughtExceptionHandler());
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new ExceptionThread());
        exec.shutdown();
    }
}
/* Output:
caught java.lang.RuntimeException
*///:~
```

+ 这个处理器只有在不存在线程专有的未捕获异常处理器的情况下在会被调用
    + 首先检查线程专有版本
    + 在检查线程组
    + 最后选择调用 `defaultUncaughtExceptionHandler`
+ [TestDefaultUncaughtExceptionHandler](TestDefaultUncaughtExceptionHandler.java)

```java
import java.util.concurrent.*;
class TDUEH1 implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException("RTException from TDUEH1");
    }
}

class TDUEH2 implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException("RTException from TDUEH2");
    }
}

class MyUncaughtExceptionHandler1 implements
    Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("default(Handler1) : " + e);
    }
}

class MyUncaughtExceptionHandler2 implements
    Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("Handler2 : " + e);
    }
}

public class TestDefaultUncaughtExceptionHandler {
    public static void main(String...args) {
        // set deault
        Thread.setDefaultUncaughtExceptionHandler(
            new MyUncaughtExceptionHandler1());
        // 有专有的 Exception Handler
        Thread t1 = new Thread(new TDUEH1());
        t1.setUncaughtExceptionHandler(
            new MyUncaughtExceptionHandler2());
        t1.start();
        // 没有专有的 Exception Handler
        new Thread(new TDUEH2()).start();
    }
}
/* Output
Handler2 : java.lang.RuntimeException: RTException from TDUEH1
default(Handler1) : java.lang.RuntimeException: RTException from TDUEH2
*/
```

