# Chapter_12 通过异常处理错误

> [总目录](../README.md)

---

[TOC]

---

+ `Java` 的基本理念 ：**结构不佳的代码不能运行**



## 12.1 概念

+ `C` 风格错误处理模式
    + 返回某个 **特殊值**，设置某个标志（**errno**）
    + 然后在某些地方检查 **返回值** 或者 **标志**
+ `Java` 处理异常的方式继承自 `C++`



## 12.2 基本异常

+ **普通问题** vs **异常情形**（`exceptional condition`）
    + **普通问题**
        + 在当前环境下能够的到足够的信息，总能够处理这个错误
    + **异常情形**
        + **在当前环境下** 无法获得必要的信息来解决问题
        + 需要从当前环境跳出，把问题提交给上一级环境
            + **抛出异常**
+ 抛出异常之后发生的事
    + 在堆上使用 `new` 创建异常对象
    + 当前执行路径被终止，并从当前环境中弹出对异常对象的引用
    + 异常处理机制接管程序，并寻找一个恰当的地方继续执行程序
        + 恰当的地方：**异常处理程序**
        + 将程序从错误状态中恢复，要么换一种方式运行，要么继续运行下去
+ 异常使得我们将每件事都当作 **事务** 来考虑，异常则可以视作时看护这些事物的底线
    + `Jim Gray`：图灵奖得主，事务处理中的杰出贡献
        + 著作：**【事务处理 概念与技术】**
+ 异常最重要的方面之一
    + 如果发生问题，异常将不允许程序沿着其正常的路径继续走下去



### 12.2.1 异常参数

+ 标准异常类
    + 默认构造器（**无参**）
    + 接受 **字符串** 为参数
+ [TestStandardException](TestStandardException.java)

```java
/**
 * @author banbao
 */

public class TestStandardException{
    public static void main(String...args){
        try{
            test1();
        } catch (Exception e){
            System.out.println(e);
        }
        ////////////////////
        try{
            test2();
        } catch (Exception e){
            System.out.println(e);
        }
        /////////////
        test3();
    }

    static void test1() throws Exception{
        throw new Exception();
    }

    static void test2() throws Exception{
        throw new Exception("test2");
    }

    static void test3(){
        try{
            throw new Exception();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
```

```output
java.lang.Exception
    at TestStandardException.test3(TestStandardException.java:32)
    at TestStandardException.main(TestStandardException.java:19)
```

+ 看起来似乎是种返回效果
    + 能够使用抛出异常的方式从当前的作用域中退出
        + 首先返回一个异常对象，然后退出 **方法**/**作用域**
    + 但是异常返回的地点和普通方法返回的地点完全不同
        + 在一个恰当的异常处理程序中处理
        + 可能跨越方法调用栈的许多层次
+ 异常的根类 `Throwable`
+ 通常情况下，异常对象仅有的信息就是异常类型，除此之外无有意义内容



## 12.3 捕获异常

+ 监控区域（`guarded region`）
    + 一段可能产生异常的代码，并且后面跟着处理这些异常的代码



### 12.3.1 try 块

+ 捕获异常

```java
try{
    // 可能产生异常的代码
}
```

+ 异常处理程序
    + `Type1` 要求是 `Type2` 的子类，否则永远不会被调用，**会报错**
    + 异常总是顺序匹配 **第一次** 能够匹配上的异常处理程序

```java
try{
    // 可能产生异常的代码
} catch(Type1 id1){
    // 异常处理程序 1
} catch(Type2 id2){

}
```

+ [TestMultiException](TestMultiException.java)

```java
import java.io.*;
public class TestMultiException{
    public static void main(String...args){
        try{
            test1();
        } catch (IOException e){
            System.out.println("IO:" + e);
        } catch (Exception e){
            System.out.println("E:" + e);
        }
    }

    static void test1() throws IOException{
        throw new IOException();
    }
}
```

```output
IO:java.io.IOException
```



#### 12.3.1.1 终止与恢复

+ **终止模型**
    + 假设错误非常关键，无法返回到异常发生的地方继续运行
+ **恢复模型**
    + 希望处理完异常后，继续执行原来的代码
    + `while` 循环



## 12.4 创建自定义异常

```java
// 一般这样就够了，因为异常关键的还是名字
class MyException extends Exception{}

// 增加字符串的构造函数
class MyException2 extends Exception{
    MyException2(){}
    MyException2(String msg){
        super(msg);
    }
}
```

+ `System.err` 将错误信息发送给 **标准错误流**
    + 不会随着 `System.out` 一起被重定向
+ `e.printStackTrace()`
    + 默认输出到标准错误流
    + 输出到标准输出
        + `e.prinStackTrace(System.out)`



### 12.4.1 异常与记录日志

+ `java.util.logging.*`
+ [LoggingExceptions](LoggingExceptions.java)

```java
// 示例代码

import java.util.logging.*;
import java.io.*;

class LoggingException extends Exception {
    // 创建一个 String 参数相关联的 logger
    private static Logger logger =
        Logger.getLogger("LoggingException");
    // 构造函数
    public LoggingException() {
        // 为了将栈轨迹输出到字符串中
        StringWriter trace = new StringWriter();
        printStackTrace(new PrintWriter(trace));
        // severe 严重级别的异常
        logger.severe(trace.toString());
    }
}

public class LoggingExceptions {
    public static void main(String[] args) {
        try {
            throw new LoggingException();
        } catch(LoggingException e) {
            System.err.println("Caught " + e);
        }
        try {
            throw new LoggingException();
        } catch(LoggingException e) {
            System.err.println("Caught " + e);
        }
    }
}
```

```output
七月 13, 2020 11:22:36 上午 LoggingException <init>
严重: LoggingException
    at LoggingExceptions.main(LoggingExceptions.java:19)

Caught LoggingException
七月 13, 2020 11:22:36 上午 LoggingException <init>
严重: LoggingException
    at LoggingExceptions.main(LoggingExceptions.java:24)

Caught LoggingException
```



## 12.5 异常说明

+ `throws` 关键字
    + 表示该方法可能抛出异常

```java
class Test{
    public void func() throws IOException(){}
}
```

+ `Java` 要求如果一个方法中含有异常，要么处理异常，要么抛出异常
+ **可以声明抛出异常，但是实际上不抛出**
    + **留后路**
+ **被检查的异常**：在编译时被强制检查的异常



## 12.6 捕获所有异常

```java
catch(Exception e){
    // 异常处理程序
    e.printStackTrace();
}
```

+ 放在异常处理程序的末尾，**最后一个捕获**
+ `Exception` 是与编程相关的所有异常类的基类
+ [TestExceptionFunc.java](TestExceptionFunc.java)
    + `getMessage() < getLocalizedMessage() < toString() < printStackTrace()`

```java
import java.io.*;
public class TestExceptionFunc{
    public static void main(String...args){
        try{
            test1();
        } catch (Exception e){
            System.out.println("    e.printStackTrace():");
            e.printStackTrace();
            System.out.println("    e.getMessage():\n"
                + e.getMessage());
            System.out.println("    e.getLocalizedMessage():\n"
                + e.getLocalizedMessage());
            System.out.println("    e.toString():\n" + e);
        }
    }

    static void test1() throws IOException{
        throw new IOException("IOException from test1().");
    }
}
```

```output
    e.printStackTrace():
java.io.IOException: IOException from test1().
    at TestExceptionFunc.test1(TestExceptionFunc.java:21)
    at TestExceptionFunc.main(TestExceptionFunc.java:8)
    e.getMessage():
IOException from test1().
    e.getLocalizedMessage():
IOException from test1().
    e.toString():
java.io.IOException: IOException from test1().
```



### 12.6.1 栈轨迹

+ [TestStackTrace.java](TestStackTrace.java)

```java
import java.io.*;
public class TestStackTrace{
    public static void main(String...args){
        System.out.println("--------");
        a0();
        System.out.println("--------");
        a1();
        System.out.println("--------");
        a2();
        System.out.println("--------");
        a3();
        System.out.println("--------");
    }

    static void a0() { a1(); }
    static void a1() { a2(); }
    static void a2() { a3(); }
    static void a3() {
        try{
            throw new Exception("Exception from a3().");
        }catch(Exception e){
            for(StackTraceElement ele : e.getStackTrace())
                System.out.println(ele);
        }
    }
}
```

```output
--------
TestStackTrace.a3(TestStackTrace.java:23)
TestStackTrace.a2(TestStackTrace.java:20)
TestStackTrace.a1(TestStackTrace.java:19)
TestStackTrace.a0(TestStackTrace.java:18)
TestStackTrace.main(TestStackTrace.java:8)
--------
TestStackTrace.a3(TestStackTrace.java:23)
TestStackTrace.a2(TestStackTrace.java:20)
TestStackTrace.a1(TestStackTrace.java:19)
TestStackTrace.main(TestStackTrace.java:10)
--------
TestStackTrace.a3(TestStackTrace.java:23)
TestStackTrace.a2(TestStackTrace.java:20)
TestStackTrace.main(TestStackTrace.java:12)
--------
TestStackTrace.a3(TestStackTrace.java:23)
TestStackTrace.main(TestStackTrace.java:14)
--------
```



### 12.6.2 重新抛出异常

+ `fillInStackTrace()` **更新栈轨迹**
    + 清空原来的栈轨迹
+ [ReThrowing](ReThrowing.java)

```java
import java.io.*;

class E1 extends Exception {}
class E2 extends Exception {}

public class ReThrowing{
    public static void main(String...args){
        try{
            a0(1);
        } catch(Exception e){
            for(StackTraceElement ele : e.getStackTrace())
                System.out.println(ele.getMethodName());
        }
        ////////////////////////////////////////////////
        System.out.println("---------");
        try{
            a0(2);
        } catch(Exception e){
            for(StackTraceElement ele : e.getStackTrace())
                System.out.println(ele.getMethodName());
        }
    }

    static void a0(int kind) throws Exception {
        try{
            a1(kind);
        } catch(E1 e){
            System.out.println("E1 is caught. ReThrowing!");
            throw e;
        } catch(E2 e){
            System.out.println("E1 is caught. FillInStackTrace!");
            e.fillInStackTrace();
            throw e;
        }
    }
    static void a1(int kind) throws Exception { a2(kind); }
    static void a2(int kind) throws Exception { a3(kind); }
    static void a3(int kind) throws Exception {
        if(kind == 1){
            throw new E1();
        } else if(kind == 2){
            throw new E2();
        }
    }
}
```

```output
E1 is caught. ReThrowing!
a3
a2
a1
a0
main
---------
E1 is caught. FillInStackTrace!
a0
main
```



### 12.6.3 异常链

+ 在捕获一个异常之后重新抛出一个异常，但是希望保留原来异常的信息
+ `Throwable` 的子类中只有 `Error,Exception,RuntimeException` 中有以异常为参数的构造器
    + 如此可以直接在构造器中传入原始异常作为参数
    + 其余需要通过调用 `initCause()` 来实现
+ [TestCause.java](TestCause.java)

```java
import java.io.*;

class E1 extends Exception{}
class E2 extends Exception{
    E2(){}
    E2(Exception e){
        super(e);
    }
}

public class TestCause{
    public static void main(String...args){
        Exception[] es = new Exception[3];
        es[0] = new Exception();
        es[1] = new E1();
        es[2] = new E2();
        for(Exception e : es){
            try{
                test(e);
            }catch(Exception ec){
                ec.printStackTrace();
            }
            System.out.println("-------");
        }
    }
    static void test(Exception e) throws Exception{
        try {
            a1(e);
        }catch(Exception ec){
            System.out.println("~~~~~origin start~~~~~");
            ec.printStackTrace();
            System.out.println("~~~~~origin end~~~~~");
            if(ec instanceof E1){
                E1 en = new E1();
                en.initCause(ec);
                throw en;
            } else if(ec instanceof E2){
                throw new E2(ec);
            } else{
                throw new Exception(ec);
            }
        }
    }
    static void a1(Exception e) throws Exception { a2(e); }
    static void a2(Exception e) throws Exception { a3(e); }
    static void a3(Exception e) throws Exception {
        if(e instanceof E1){
            throw new E1();
        } else if(e instanceof E2){
            throw new E2();
        } else{
            throw new Exception();
        }
    }
}
```

```output
~~~~~origin start~~~~~
java.lang.Exception
    at TestCause.a3(TestCause.java:55)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    at TestCause.main(TestCause.java:22)
~~~~~origin end~~~~~
java.lang.Exception: java.lang.Exception
    at TestCause.test(TestCause.java:43)
    at TestCause.main(TestCause.java:22)
Caused by: java.lang.Exception
    at TestCause.a3(TestCause.java:55)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    ... 1 more
-------
~~~~~origin start~~~~~
E1
    at TestCause.a3(TestCause.java:51)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    at TestCause.main(TestCause.java:22)
~~~~~origin end~~~~~
E1
    at TestCause.test(TestCause.java:37)
    at TestCause.main(TestCause.java:22)
Caused by: E1
    at TestCause.a3(TestCause.java:51)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    ... 1 more
-------
~~~~~origin start~~~~~
E2
    at TestCause.a3(TestCause.java:53)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    at TestCause.main(TestCause.java:22)
~~~~~origin end~~~~~
E2: E2
    at TestCause.test(TestCause.java:41)
    at TestCause.main(TestCause.java:22)
Caused by: E2
    at TestCause.a3(TestCause.java:53)
    at TestCause.a2(TestCause.java:48)
    at TestCause.a1(TestCause.java:47)
    at TestCause.test(TestCause.java:31)
    ... 1 more
-------
```



## 12.7 Java标准异常

+ `Throwable` 的子类有两种
    + `Error` 编译时和系统错误（一般情况不需要考虑）
    + `Exception` 可以被抛出的基本类型

### 12.7.1 特例 RuntimeException

+ 运行时会自动检测是否为 `null`
+ 以下代码没有意义

```java
if(t == null){
    throw new NullPointerException();
}
```

+ 运行时异常都是继承自 `RuntimeException`
    + 不需要程序员手动声明
    + 也不需要在方法中声明 `throws RuntimeException`
    + 在程序退出之前会调用其 `printStackTrace()` 方法
+ [TestRuntimeException](TestRuntimeException.java)

```java
/**
 * @author banbao
 */
import java.io.*;
public class TestRuntimeException{
    public static void main(String...args){
        a1();
    }

    static void a1() { a2(); }
    static void a2() { a3(); }
    static void a3() {
        throw new RuntimeException();
    }
}
```

```output
Exception in thread "main" java.lang.RuntimeException
    at TestRuntimeException.a3(TestRuntimeException.java:13)
    at TestRuntimeException.a2(TestRuntimeException.java:11)
    at TestRuntimeException.a1(TestRuntimeException.java:10)
    at TestRuntimeException.main(TestRuntimeException.java:7)
```



## 12.8 使用 finally 进行清理

+ `C++` 中没有，可以以来析构函数实现
+ `Java` 中的 `finally` 子句无论是否抛出异常都会执行

```java
try {
    // 可能产生异常的代码
} catch(Type1 id1){
    // 异常处理程序 1
} finally{
    // finally 子句
}
```

+ []

```java
/**
 * @author banbao
 */
import java.io.*;
public class TestFinally{
    public static void main(String...args){
        boolean[] test = {true, false};
        /////////// Exception //////////
        for(boolean t : test){
            System.out.println("-------");
            try {
                a1(t);
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                System.out.println("Reach \"finally\"!");
            }
        }
    }

    static void a1(boolean t) throws Exception { a2(t); }
    static void a2(boolean t) throws Exception { a3(t); }
    static void a3(boolean t) throws Exception {
        System.out.println("Exception:" + t);
        if(t){
            throw new Exception();
        }
    }
}
```

+ [TestFinally](TestFinally.java)

```output
-------
Exception:true
java.lang.Exception
    at TestFinally.a3(TestFinally.java:26)
    at TestFinally.a2(TestFinally.java:22)
    at TestFinally.a1(TestFinally.java:21)
    at TestFinally.main(TestFinally.java:12)
Reach "finally"!
-------
Exception:false
Reach "finally"!
```

+ 将 `finally` 子句和带标签的 `break,continue` 语句配合可以实现 `goto` 的功能

### 12.8.1 finally 用来做什么

+ 释放内存（`Java` 不需要，有垃圾自动回收机制）
+ 清理资源：**已经打开的文件**、**网络连接**

### 12.8.1 在 return 中使用 finally

+ [TestFinally2.java](TestFinally2.java)

```java
/**
 * @author banbao
 */
import java.io.*;
public class TestFinally2{
    public static void main(String...args){
        boolean[] test = {true, false};
        /////////// return //////////
        System.out.println("~~~~~test return~~~~~");
        test(11);
        /////////// break ///////////
        System.out.println("~~~~~test break~~~~~");
        test(5);
    }

    static void test(int t) {
        try{
            if(t >= 10) return;
            for(int i = 0; i < 10; ++i){
                System.out.println(i);
                if(i == t) break;
            }
        } finally{
            System.out.println("Clean up!");
        }
    }
}
```

```output
~~~~~test return~~~~~
Clean up!
~~~~~test break~~~~~
0
1
2
3
4
5
Clean up!
```



### 12.8.3 缺憾：异常缺失

+ 在 `finally`  中返回
+ [LostException](LostException.java)

```java
import java.io.*;
public class LostException{
    public static void main(String...args){
        try {
            test();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    static void test() {
        try {
            throw new Exception();
        } finally{
            return;
        }
    }
}
```

```java
// 没有输出
```

+ 在 `finally` 子句中的异常会覆盖在 `try` 子句中的一场
+ `C++` 认为前一个异常还未处理就抛出下一个异常是一个 **糟糕的编程错误**
+ [LostException2](LostException2.java)

```java
import java.io.*;

class E1 extends Exception{
    @Override
    public String toString(){ return "Important!"; }
}

class E2 extends Exception{
    @Override
    public String toString(){ return "Negligible!"; }
}

public class LostException2{
    public static void main(String...args){
        try {
            test();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    static void test() throws Exception {
        try {
            throw new E1();
        } finally{
            throw new E2();
        }
    }
}
```

```output
Negligible!
```



## 12.9 异常的限制

+ 当覆盖基类方法的时候，值能跑出基类方法的异常说明里的异常
    + 即要求基类使用的代码能够正确应用到派生类对象中
        + 基类不抛出异常，派生类也不能抛出异常
        + 基类抛出异常，派生类可以不抛出异常
    + **构造器不受这个规则的限制，可以抛出基类构造器没有的异常**
        + 当然，派生类调用对应基类构造器时，必须声明对应构造器抛出的异常
+ [TestOverride.java](TestOverride.java)

```java
import java.io.*;

class E1 extends Exception {}
class E2 extends E1 {}
class Base {
    Base() throws E1 {}
    public void func() throws E1 {}
}
class Derived1 extends Base {
    // 构造器可以抛出基类构造器没有的异常
    Derived1() throws E1, Exception {}
    @Override
    public void func() throws E2 {}
}

class Derived2 extends Base {
    // 默认构造器中未报告的异常错误E1
    // Derived2() {}
    Derived2() throws E1 {}
    // @Override
    // 错误: Derived2中的func()无法覆盖Base中的func()
    // 被覆盖的方法未抛出Exception
    // public void func() throws Exception {}
}
public class TestOverride {
    public static void main(String...args){}
}
```

```java
// 没有输出
```



## 12.10 构造器

+ 注意一些细节
    + 例如构造器中需要打开文件
        + 若构造器抛出异常时文件已经打开，则需要在关闭文件，**否则则不能关闭**
        + 不能简单地用一个 `finally` 子句解决
    + 可以对异常分类（多个 `catch` 语句）
    + 可以拆开语句，每一句处理某类异常
    + 可以使用多层嵌套的 `try...catch...`
        + 基本规则：**在创建需要清理的对象之后，立即进入一个 `try-finally` 语句块**
        + [CleanupIdiom](CleanupIdiom.java)

```java
// 示例代码

class NeedsCleanup { // Construction can't fail
    private static long counter = 1;
    private final long id = counter++;
    public void dispose() {
        System.out.println("NeedsCleanup " + id + " disposed");
    }
}

class ConstructionException extends Exception {}

class NeedsCleanup2 extends NeedsCleanup {
    // Construction can fail:
    public NeedsCleanup2() throws ConstructionException {}
}

public class CleanupIdiom {
    public static void main(String[] args) {
        // Section 1:
        NeedsCleanup nc1 = new NeedsCleanup();
        try {
            // ...
        } finally {
            nc1.dispose();
        }

        // Section 2:
        // 如果构造函数不会抛出异常，顺序列出即可
        NeedsCleanup nc2 = new NeedsCleanup();
        NeedsCleanup nc3 = new NeedsCleanup();
        try {
            // ...
        } finally {
            nc3.dispose(); // 逆序回收
            nc2.dispose();
        }

        // Section 3:
        // 如果构造函数会抛出异常，需要将每一个都嵌套上 "try-catch"
        try {
            NeedsCleanup2 nc4 = new NeedsCleanup2();
            try {
                NeedsCleanup2 nc5 = new NeedsCleanup2();
                try {
                    // ...
                } finally {
                    nc5.dispose();
                }
            } catch(ConstructionException e) { // nc5 constructor
                System.out.println(e);
            } finally {
                nc4.dispose();
            }
        } catch(ConstructionException e) { // nc4 constructor
            System.out.println(e);
        }
    }
}
```

```output
NeedsCleanup 1 disposed
NeedsCleanup 3 disposed
NeedsCleanup 2 disposed
NeedsCleanup 5 disposed
NeedsCleanup 4 disposed
```

+ 一些设计问题
    + 需要将所有的异常都直接抛出，还是先处理一部分
    + 就像编译器设计类似，什么时候处理



## 12.11 异常匹配

+ 异常只会被第一个匹配上的 `catch` 语句处理
    + 派生类的异常也会被基类捕获
+ 若派生类异常处理程序在基类之后，编译器回报错



## 12.12 其他可选方式

+ 开发异常处理的初衷是为了方便程序员处理错误
+ 重要原则：**知道怎么处理异常的时候才捕获**
+ 被检查的异常可能有害
    + 可能编译器要求你进行捕获，但是你不知道怎么处理
    + `harmful if swallowed`
+ 简单处理方式，将异常传递给控制台

```java
public class Test{
    public static void main(String...arg) throws Exception {
        // code
    }
}
```

#### 12.12.1 将“被检查的异常”转化为“不检查的异常”

+ 技巧，通过 `RuntimeException` 向上传递
+ 可以通过 `getCause()` 方法获取原始异常

```java
public class Test{
    public static void main(String...arg) {
        // code
    }

    public void func(){
        try{
            // code
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
```



## 12.13 异常使用指南

+ 应该在下列情况使用异常
    + 在恰当的级别处理问题。(在知道该如何处理的情况下才捕获异常。)
    + 解决问题并且重新调用产生异常的方法。
    + 进行少许修补，然后绕过异常发生的地方继续执行。
    + 用别的数据进行计算，以代替方法预计会返回的值。
    + 把当前运行环境下能做的事情尽量做完，然后把**相同的**异常**重抛**到更高层。
    + 把当前运行环境下能做的事情尽量做完，然后把**不同的**异常抛到更高层。
    + 终止程序。
    + 进行简化。(如果你的异常模式使问题变得太复杂，那用起来会非常痛苦也很烦人。)
    + 让类库和程序更安全。‘这既是在为调试做短期投资，也是在为程序的健壮性做长期投资。)