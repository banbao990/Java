# Chapter_10 内部类

> [总目录](../README.md)

---

[TOC]

---

+ 将一个类的定义放在另一个类的内部，这就是 **内部类**
    + 将一些逻辑相关的类组织在一起
    + 控制类的可视性（访问权限）
+ 内部类可以了解外围类，并与之通信



## 10.1 创建内部类

```java
public class WyvernKnight{
    class Pierce{
        public final boolean negDefence = true;
        Pierce(){}
        public void attack(){
            System.out.println("Neglect enemy'denfence:true");
        }
    }

    public void attack(){
        new Pierce().attack(); // USE
    }

    public Pierce pierce(){
        return new Pierce();
    }

    public static void main(String...arg){
        new WyvernKnight().attack();
        WyvernKnight t = new WyvernKnight();
        WyvernKnight.Pierce p = t.pierce(); // outer.inner
        p.attack();
    }
}
```

```output
Neglect enemy'denfence:true
Neglect enemy'denfence:true
```



## 10.2 链接到外部类

+ `c++` **的嵌套类** : 单纯的名字隐藏机制，与外围对象没有联系，也没有隐含的访问权
+ **迭代器设计模式**
    + 这里内部类访问了外围类
+ [Sequence.java](Sequence.java)

```java
// 迭代器设计模式
// 样例代码

// Selector 接口
interface Selector{
    boolean end();
    Object current();
    void next();
}

// 试图使用迭代器 Selector
public class Sequence{
    private Object[] items;
    private int next = 0;
    public Sequence(int size){
        items = new Object[size];
    }

    // 添加元素
    public void add(Object x){
        if (next < items.length){
            items[next++] = x;
        }
    }

    // 内部类
    private class SequenceSelector implements Selector{
        private int i = 0;
        // 实现接口中的 3 个函数
        public boolean end(){
            return i == items.length;
        }
        public Object current(){
            return items[i];
        }
        public void next(){
            if(i < items.length){
                ++i;
            }
        }
    }
    // 获取迭代器
    public Selector selector(){
        return new SequenceSelector();
    }
    // main
    public static void main(String...args){
        Sequence sequence = new Sequence(5);
        // 添加元素
        for(int i = 0;i < 5; ++i){
            sequence.add(i);
        }
        // 获取迭代器
        Selector selector = sequence.selector();
        // 使用迭代器
        while(!selector.end()){
            System.out.println(selector.current());
            selector.next();
        }
    }
}
```

```output
0
1
2
3
4
```

+ 编译器会在内部类对象生成的时候，为其生成一个指向外围对象的引用



## 10.3 使用 .this 和 .new

+ 使用 `Outer.this.field` 在编译期就被知晓并且检查，因此没有运行时开销
+ 通过 `.new` 来创建某个内部类对象
    + **注意必须使用外围类的对象进行对内部类对象的创建**
    + 原因是内部类对象会被自动来年街道船箭塔的外部类对象上
    + 如果是 **静态内部类**(**嵌套类**) 则不需要
+ [TestDotNewDotThis.java](TestDotNewDotThis.java)

```java
class Outer{
    final public int value = 10;
    public class Inner{
        final public int value = 100;
        public void print(){
            System.out.println(this.value);             // 100
            System.out.println(Outer.this.value);       // 10
        }
    }

    // 静态内部类(嵌套类)
    public static class StaticInner{
        private int value = 11;
        public void print(){
            System.out.println(value);
        }
    }

    public void print(){
        new Inner().print();
    }
}

public class TestDotNewDotThis{
    public static void main(String...args){
        // .this
        new Outer().print();
        System.out.println("-----");
        // .new
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner(); // private Inner 则无法使用
        inner.print();
        System.out.println("-----");
        // Outer.Inner inner2 = new Outer.Inner();
        // 错误: 需要包含Outer.Inner的封闭实例
        // static
        System.out.println("-----");
        Outer.StaticInner staticInner = new Outer.StaticInner();
        staticInner.print();
    }
}
```

```output
100
10
-----
100
10
-----
-----
11
```



## 10.4 内部类与向上转型

+ 若内部类为 `private` 则向上转型成基类之后无法访问该类的方法及字段
    + 也无法向下转型为内部类
+ 在设计中使用 `private` 内部类可以对客户端程序员实现全隐藏
+ [TestPrivateInnerClass.java](TestPrivateInnerClass.java)

```java
interface Base{
    void func();
}

class Outer{
    private class Inner1 implements Base{
        public void func(){
            System.out.println("Inner1:func");
        }
    }

    public class Inner2 implements Base{
        public void func(){
            System.out.println("Inner2:func");
        }
    }
}

public class TestPrivateInnerClass{
    public static void print(){
        System.out.println("-----");
    }

    public static void main(String...args){
        Outer outer = new Outer();

        // Outer.Inner1 inner1 = outer.new Inner1();
        // 错误: Outer.Inner1 在 Outer 中是 private 访问控制

        Outer.Inner2 inner2 = outer.new Inner2();

        // Base inner3 = outer.new Inner1();
        // 错误: Outer.Inner1 在 Outer 中是 private 访问控制

        Base inner4 = outer.new Inner2();
        print();
        // inner1.func();
        print();
        inner2.func();
        print();
        // inner3.func();
        print();
        inner4.func();
        print();
    }
}
```

```output
-----
-----
Inner2:func
-----
-----
Inner2:func
-----
```



## 10.5 在方法和作用域内的内部类

+ 在方法作用域内的类 : **局部内部类**
+ 在方法外不能访问这个类，但是可以通过 **转型** 传递
+ [TestInnerClassInMethod.java](TestInnerClassInMethod.java)
    + 编译生成文件
        + `Base.class`
        + **`Outer$1Inner.class`**
        + `Outer.class`
        + `TestInnerClassInMethod.class`

```java
interface Base{
    void func();
}

class Outer{
    public Base func(){
        // 不允许增加访问权限限定词( public,private 都不行)
        class Inner implements Base{
            public void func(){
                System.out.println("Inner1:func");
            }
        }
        return new Inner();
    }

    public void test(){
        // new Inner().func();
        // 错误: 找不到符号
    }
}

public class TestInnerClassInMethod{
    public static void print(){
        System.out.println("-----");
    }

    public static void main(String...args){
        Base b = new Outer().func();
        b.func();
    }
}

// output
// Inner1:func
```

+ 在任意作用域中嵌入内部类
    + 注意只能在该作用域中使用内部类

```java
interface Base{
    void func();
}

class Outer{
    public Base func(){
        Base b;
        {
            // 不允许增加访问权限限定词( public,private 都不行)
            class Inner implements Base{
                public void func(){
                    System.out.println("Inner1:func");
                }
            }
            b = new Inner();
        }
        // b = new Inner(); // 错误: 找不到符号
        return b;
    }
}

// output
// Inner1:func
```



## 10.6 匿名内部类

+ [TestAnonymousClass.java](TestAnonymousClass.java)

```java
// 匿名类

interface Base{
    void func();
}

class Outer{
    public Base func(){
        // 匿名类
        // 自动创建一个继承自Base(实现Base接口)的类
        // 并且向上转型至Base
        return new Base(){
            public void func(){
                System.out.println("Inner1:func");
            }
        };
    }
}

public class TestAnonymousClass{
    public static void main(String...args){
        Base b = new Outer().func();
        b.func();
    }
}

// output:
// Inner1:func
```

+ 构造函数参数的传递
    + **从内部类引用的本地变量必须是最终变量或实际上的最终变量**
        + 指在内部类中引用外围类变量
        + **声明为 `final`** 或者 **不被修改**
+ [TestAnonymousClassConstructor.java](TestAnonymousClassConstructor.java)

```java
// 匿名类

abstract class Base{
    // 无参构造函数
    Base(){
        System.out.println("No args!");
    }
    // 含参构造函数
    Base(int x){
        System.out.println("One arg!");
    }
    // 测试函数
    void func(){
        System.out.println("Blank!");
    }
}

class Outer{
    public Base func(){
        // 匿名类
        // 无参构造函数
        return new Base(){
            @Override
            public void func(){
                System.out.println("Func, no args!");
            }
        };
    }

    public Base func(int x){
        // 匿名类
        // 含参构造函数
        return new Base(x){
            public void func(){
                // x = x + 1;
                // 错误: 从内部类引用的本地变量必须是最终变量或实际上的最终变量
                System.out.println("Func, one arg!");
            }
        };
    }
}

public class TestAnonymousClassConstructor{
    public static void main(String...args){
        Base b = new Outer().func();
        b.func();
        b = new Outer().func(10);
        b.func();
    }
}
```

```output
No args!
Func, no args!
One arg!
Func, one arg!
```



### 10.6.1 再访工厂方法

+ 将方法工厂作为匿名类，同时声明为 `static` ，更加合乎实际
+ 可以作为一种新的 **设计模式**

```java
class Implementation1 implements Service {
    // 可以声明为 private, 因为只有在下面这个 static 方法中用到
    private Implementation1() {}
    public void method1() {System.out.println("Implementation1 method1");}
    public void method2() {System.out.println("Implementation1 method2");}
    // 通常只需要有一个工厂方法,因此声明为 static 更好
    public static SeviceFactory factory =
        new ServiceFactory(){
            public Service getService(){
                return new Implements1();
            }
        };
}
```



## 10.7 嵌套类

+ 将内部类声明为 `static`
+ 切断了内部类对于外围类的了解
+ **嵌套类**
    + 要创建嵌套类的对象，不需要其外围类的对象
    + 不能从嵌套类的对象中访问非静态类的外围对象
+ 普通的内部类不能 **声明**  `static` 方法、字段和嵌套类，但是嵌套类中可以包含这些
    + 允许内部类的基类包含 `static` 字段
    + 个人觉得是没有意义
        + 如果是用于基类，可以直接在基类中声明
        + 如果是想用于该内部类，可以在外围类中声明

```java
class Outer{
    public class Inner1 {

        // 错误: 内部类 Outer.Inner1 中的静态声明非法
        // public static int h1 = 0;

        public static final int h2 = 0;

        // 错误: 内部类Outer.Inner1中的静态声明非法
        // 只能使用常量声明
        // public static final int h3 = new Integer(10);

        public static final int h4 = h2;
    }

    public class Inner2 extends StaticBase{}
}

class StaticBase{
    public static int x = 0;
}

public class Test{
    public static void main(String...args){
        System.out.println(Outer.Inner2.x);
    }
}
```

```output
0
```



### 10.7.1 接口内部的类

+ 接口中的类是自动 `public`、`static` 的
+ 可以在接口的内部类中实现外围接口
+ 建议在每一个类中都声明一个 `main` 方法，用于测试该类
    + 存在的问题
        + 在编译生成的代码中会一直携带着这一部分测试代码
    + 替代方案
        + 可以使用嵌套类，然后在最终发布的代码中简单的删除对应嵌套类

```java
public class Work{
    public void func(){
        System.out.println("test func()");
    }
    public static class Tester{
        public static void main(String...args){
            Work work = new Work();
            work.func();
        }
    }
}
```

```powershell
# windows 下的编译与运行命令
# 编译 Work 类
javac Work.java
# 运行测试函数
java Work$Tester
```

```output
test func()
```



### 10.7.2 从多层嵌套的内部类中访问外部类的成员

+ 直接访问即可
+ `private` 也可以访问
+ [ManyNestedClass.java](ManyNestedClass.java)

```java
class Print{
    // package access
    static void p(Object s){
        System.out.println(s);
    }
}

public class ManyNestedClass{
    private class O1{
        // 同名字段
        private int o = 1;
        // 同名方法
        private void f(){ Print.p("O1.f"); }
        private void f1(){ Print.p("f1"); }
        class O2{
            // 同名字段
            int o = 2;
            // 同名方法
            void f(){ Print.p("O2.f"); }
            void f2(){ Print.p("f2"); }
            public class O3{
                // 同名字段
                public int o = 3;
                // 同名方法
                void f(){ Print.p("O3.f"); }
                public void f3(){
                    // 访问外部字段(从外到里)
                    Print.p("-----");
                    Print.p(O1.this.o);
                    Print.p(O2.this.o);
                    Print.p(this.o);
                    Print.p("-----");
                    // 访问外部方法
                    f1();
                    f2();
                    Print.p("-----");
                    // 同名方法(从外到里)
                    O1.this.f();
                    O2.this.f();
                    this.f();
                }
            }
        }
    }
    public static void main(String...args){
        ManyNestedClass t0 = new ManyNestedClass();
        ManyNestedClass.O1 t1 = t0.new O1();
        ManyNestedClass.O1.O2 t2 = t1.new O2();
        ManyNestedClass.O1.O2.O3 t3 = t2.new O3();
        t3.f3();
    }
}
```



## 10.8 为什么需要内部类

+ 每一个内部类都能独立的继承自一个（接口的）实现，因此无论外围类是够已经集成可某个（接口的）实现，对于内部类没有影响
+ 可以有效实现 **多重继承**
    + 接口的话单一类、内部类都可以
    + 抽象类、具体类的话必须通过内部类实现
+ **单一类** vs **内部类**
    + 都可以实现同样的功能，具体应用有所取舍

```java
interface A{}
interface B{}

class X implements A,B{}

class Y implements A{
    B makeB(){
        return new B(){};
    }
}
```

+ 迭代器的重新实现
    + 内部类的灵活性
    + 如果两个接口具有同名函数，要想是想接口原有功能，则需要使用内部类
+ [Sequence2.java](Sequence2.java)

```java
// ReverseSelector 接口
interface ReverseSelector{
    boolean begin();
    Object current();
    void next();
}

public class Sequence2{
    ...
    // 获取反向迭代器
    public ReverseSelector reverseSelector(){
        // 匿名类
        return new ReverseSelector(){
            private int i = items.length - 1;
            public boolean begin(){
                return i == -1;
            }
            public Object current(){
                return items[i];
            }
            // 和 Selector 接口具备同名函数,但是功能不同
            public void next(){
                if(i > -1){
                    --i;
                }
            }
        };
    }
    ...
}
```



### 10.8.1 闭包与回调

+ 闭包（**closure**）是一个可调用的对象，它记录了一些信息，这些信息来自于创建它的作用域
+ 内部类是面向对象的闭包，其记录了外围类的信息
+ 使用内部类实现回调（**callback**）
    + 避免使用指针
        + 通过访问权限的控制更加安全
    + 如果实现具有同名函数的两个接口



### 10.8.2 内部类与控制框架

+ **控制框架**（**control framework**）
+ **事件驱动系统**
    + **GUI**
        + `Java Swing`
+ 以下是一个例子

#### （1）接口描述要控制的时间，基于时间

+ [Event.java](Event.java)

```java
// 示例代码
// 事件接口
public abstract class Event{
    private long eventTime;
    protected final lng delayTime;
    public Event(long delayTime){
        this.delayTime = delayTime;
        start();
    }
    // 和构造器独立,可以在时间运行之后重新启动计时器
    // 重复一个同样的事件
    public void start(){
        eventTime = System.nanoTime() + delayTime;
    }
    // 何时可以开始运行该事件( action 方法)
    public boolean ready(){
        return System.nanoTime() >= eventTime;
    }
    // 用于继承实现的 action 方法
    public abstract void action();
}
```

#### （2）控制框架

+ [Controller.java](Controller.java)

```java
// 示例代码
// 控制框架

import java.util.*;

public class Controller{
    // 保存事件的列表
    private List<Event> eventList = new ArrayList<Event>();
    public void addEvent(Event e){
        this.eventList.add(e);
    }
    // 阻塞,检查已经准备好的事件,逐个执行
    public void run(){
        while(this.eventList.size() > 0){
            // 进行一个拷贝, 为了防止在迭代的时候由于 remove 导致的问题
            for(Event e : new ArrayList<Event>(eventList)){
                if(e.ready()){
                    System.out.println(e);
                    e.action();
                    eventList.remove(e);
                }
            }
        }
    }
}
```

#### （3）一个例子，温室控制

+ [Controller.java](Controller.java)
+ [Event.java](Event.java)
+ [GreenhouseController.java](GreenhouseController.java)
+ [GreenhouseControls.java](GreenhouseControls.java)



## 10.9 内部类的继承

+ 需要将对外围类的引用进行初始化
+ 导出类不再拥有对于基类外围类的引用

 ```java
// 示例代码
// 继承自内部类

class WithInner{
    int testOuter = 11;
    class Inner{
        int testInner = 22;
    }
}

public class InheritInner extends WithInner.Inner{
    // InheritInner(){}
    // 错误: 需要包含WithInner.Inner的封闭实例
    InheritInner(WithInner wi){
        wi.super(); // 需要这么使用才能够实现继承内部类
    }
    public static void main(String...args){
        WithInner wi = new WithInner();
        InheritInner ii = new InheritInner(wi);
        // System.out.println(ii.testOuter);
        // 错误: 找不到符号
        // System.out.println(((WithInner.Inner)ii).testOuter);
        // 错误: 找不到符号
        System.out.println(ii.testInner);
    }
}
 ```



## 10.10 内部类可以被覆盖吗

+ 外围类 `A` ，内部类 `B` ，定义一个新类 `C` 继承自外围类 `A`，在 `C` 重新定义 `B`
+ 不能覆盖

```java
// 子类的内部类和父类的内部类位于不同的命名空间
// 父类的内部类不能被覆盖

class Outer{
    // 用于测试的内部类
    private Inner inner;
    Outer(){}
    public Outer(int nouse){
        inner = new Inner(0);
    }

    class Inner{
        Inner(){}
        Inner(int nouse){
            System.out.println("Outer.Inner");
        }
        public void test(){
            System.out.println("Outer.Inner:test");
        }
    }

    // 测试多态
    public void test(){
        a();
    }
    // 多态
    public void a(){
        System.out.println("Outer:a");
    }
}

public class Outer2 extends Outer{
    public Outer2(){}
    public Outer2(int nouse){
        super(nouse);
    }
    class Inner{
        public Inner(){}
        public Inner(int nouse){
            System.out.println("Outer2.Inner");
        }
        public void test(){
            System.out.println("Outer2.Inner:test");
        }
    }

    // 覆盖
    @Override
    public void a(){
        System.out.println("Outer2:a");
    }
    public static void main(String...args){
        Outer2 o2 = new Outer2();
        // 多态测试
        System.out.println("-----");
        o2.test();
        // 不能覆盖内部类测试
        System.out.println("-----");
        new Outer2(0);
        // 拥有独立命名空间测试
        System.out.println("-----");
        Outer o1 = new Outer();
        Outer.Inner i1 = o1.new Inner();
        Outer2.Inner i2 = o2.new Inner();
        i1.test();
        i2.test();
    }
}
```

```output
-----
Outer2:a
-----
Outer.Inner
-----
Outer.Inner:test
Outer2.Inner:test
```



## 10.11 局部内部类

+ 局部内部类不能有访问权限控制符，因为其不是外围类的一部分
+ 局部内部类可以访问当前代码块内的常量，可以访问该外围类的所有成员
+ 和匿名类一致
+ 什么时候使用局部内部类而不是匿名类
    + 不止需要一个对象
    + 需要重载或重新声明一个构造器



## 10.12 内部类标识符

+ `$`

