# Chapter_14 类型信息

> [总目录](../README.md)

---

[TOC]

---



+ 运行时类型信息使得你可以在程序运行时发现和使用类型信息
    + 传统的 `RTTI` ：认为编译时已经知道了所有的类型信息
    + **反射** 机制：允许在运行时发现和使用类的信息



## 14.1 为什么需要 RTTI

## 14.2 Class 对象

+ **类加载器子系统**
    + 生成 `.class` 文件
    + 可以包括一条类加载器链，但是只有一个 **原生类加载器**（ `JVM` 实现的一部分）
        + 原生加载器加载的是所谓的 **可信类**，包括 `Java API` 类（一般本地加载）
+ 所有的类都是在对其第一次使用时，动态加载到 `JVM` 中的
    + 当程序创建第一个对类的静态成员的引用时，就会加载这个类
    + 类的构造器也是类的静态方法
        + `new` 操作符创建类的新对象也会被当作对类的静态成员的引用
+ 如上，`Java` 程序在其开始运行时并非完全加载，各个部分 **必需** 时才加载
    + `C++` 静态加载语言
+ 类加载器首先检查这个类的的 `Class` 对象是否已经加载
    + 若尚未加载则默认的类加载器就会根据类名查找 `.class` 文件
    + 加载时会接受验证（安全）

```JAVA
 Class.forName("Test"); // 返回类 Class, 若其没加载需要会加载它
```

+ 具体查看 `docs`
+ [TestClass](TestClass.java)
+ 使用 `public T newInstance()` 方法时需要有默认构造函数



### 14.2.1 类字面常量

+ 更高效的生成对 `Class` 对象的引用
    + 编译时检查（不需要 `try...catch...`），更简单、**安全**
    + 不需要调用函数 `forName()` 更高效

```java
Class cl = null;
try {
    cl = Class.forName("java.util.ArrayList"); // forName()
} catch (Exception e) {
    print(e);
    System.exit(1);
}
cl = ArrayList.class; // 类字面常量
```

+ 对于基本数据类型，返回包装类

```java
// 等价
void.class
Void.TYPE
```

+ 在使用 `Class.class` 的时候，不会触发初始化
+ 在使用 `Class.forName()` 的时候，会触发初始化

```java
class Init1 {
    static {
        System.out.println("Init1:static!");
    }
}
class Init2 {
    static {
        System.out.println("Init2:static!");
    }
}

public class TestClass2 {
    public static void main(String...args) throws Exception {
        // Init1.class
        Class init1 = Init1.class;
        System.out.println("After Init1.class");
        new Init1();
        System.out.println("new Init1()");

        System.out.println("~~~~~~~");

        // Class.forName("Init2")
        Class init2 = Class.forName("Init2");
        System.out.println("Class.forName(\"Init2\")");
        new Init2();
        System.out.println("new Init2()");
    }
}
```

```output
After Init1.class
Init1:static!
new Init1()
~~~~~~~
Init2:static!
Class.forName("Init2")
new Init2()
```

+ 关于 `final/static` 和初始化
+ [TestStaticInitialization](TestStaticInitialization.java)
+ 为了使用类而做的准备工作分为 `3` 步
    + **加载**
        + 由类加载器执行，该步骤将查找字节码，并从这些字节码中创建一个 `Class` 对象
    + **链接**
        + 验证字节码，为静态域分配存储空间（**如果必需的话**，解析这个类创建的对其他类的引用）
    + **初始化**
        + 如果该类具有父类，则对父类进行初始化，执行静态初始化器和静态初始化代码
+ 初始化被延迟到了对 **静态方法（包括构造器）** 或者 **非常数静态域** 的首次引用执行



### 14.2.2 泛化的 Class 引用

+ 增加编译器的检查
+ [TestGenericClass](TestGenericClass.java)

```java
public class TestGenericClass {
    public static void main(String...args) {
        Class cl1 = int.class;
        cl1 = double.class; // OK

        Class<Integer> cl2 = int.class;
        // cl2 = double.class; // ERROR

        // Class<Number> cl3 = int.class; // ERROR
        // Integer 是 Number 的子类
        // 但是 Class<Integer> 不是 Class<Number>的子类

        Class<?> cl3 = int.class; // 通配符(和 Class 等价, 但是更优, 对维护而言)
        cl3 = double.class;

        Class<? extends Number> cl4 = int.class; // 限定范围(对比cl3)
        cl4 = double.class;
    }
}
```

+ 将泛型语法使用于 `Class` 对象时，`newInstance()` 返回的是确切的类型
+ [TestGenericClassNewInstance](TestGenericClassNewInstance.java)

```java
class Base {}
class Derived extends Base {}

public class TestGenericClassNewInstance {
    public static void main(String...args) throws Exception {
        // 泛型
        Class<Derived> cl1 = Derived.class;
        Derived instance1 = cl1.newInstance();

        // 不使用泛型
        Class cl2 = Derived.class;
        // Derived instance2 = cl2.newInstance();
        // 错误: 不兼容的类型: Object无法转换为Derived

        // 模糊的类型,而不是具体的
        Class<? super Derived> cl3 = cl1.getSuperclass();
        // Class<Base> cl4 = cl1.getSuperclass();
        /* 错误: 不兼容的类型: Class<CAP#1>无法转换为Class<Base>
         * Class<Base> cl4 = cl1.getSuperclass();
         * 其中, CAP#1是新类型变量:
         * CAP#1从? super Derived的捕获扩展Object 超 Derived
         */
    }
}
```



### 14.2.3 新的转型语法

+ `cast()`
+ [TestCast](TestCast.java)

```java
class Base {}
class Derived extends Base {}
public class TestCast {
    public static void main(String...args) throws Exception {
        // traditional cast 传统方法
        Base b1 = new Derived();
        Derived d1 = (Derived) b1;
        // new cast
        Class<Derived> cd1 = Derived.class;
        Derived d2 = cd1.cast(b1);
    }
}
```



### 14.3 类型转换前先做检查

+ 传统的类型转换若失败，则会抛出异常 `ClassCastException`
+ 可以通过 `Class` 对象进行转换
+ `instanceof`
    + 返回 `boolean`
+ [TestInstanceof](TestInstanceof.java)

```java
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

class Base {}
class A extends Base {}
class B extends Base {}
class C extends Base {}
class D extends Base {}

public class TestInstanceof {
    public static void main(String...args) throws Exception {
        Random rd = new Random(47);
        // map
        Map<Integer, Class<? extends Base>> map = new HashMap<>();
        map.put(0, A.class);
        map.put(1, B.class);
        map.put(2, C.class);
        map.put(3, D.class);

        // count
        Map<String, Integer> count = new HashMap<>();

        // instanceof
        System.out.println("instanceof");
        for(int i = 0;i < 10;++i) {
            int which = rd.nextInt(4);
            Base b = map.get(which).newInstance();
            System.out.format(i + ":(" + which + ")");
            if(b instanceof A) { System.out.println("A"); }
            if(b instanceof B) { System.out.println("B"); }
            if(b instanceof C) { System.out.println("C"); }
            if(b instanceof D) { System.out.println("D"); }
        }

        // Class.isInstance(Object)
        rd.setSeed(47);
        List<Class<? extends Base>> list = new ArrayList<>();
        Collections.addAll(list, A.class, B.class, C.class, D.class);
        System.out.println("Class.isInstance(Object)");
        for(int i = 0;i < 10;++i) {
            int which = rd.nextInt(4);
            Base b = map.get(which).newInstance();
            System.out.format(i + ":(" + which + ")");
            for(Class<? extends Base> tClass : list) {
                if(tClass.isInstance(b)) {
                    System.out.println(tClass.getName());
                    break;
                }
            }
        }
    }
}
```

```output
instanceof
0:(2)C
1:(1)B
2:(2)C
3:(0)A
4:(0)A
5:(2)C
6:(0)A
7:(1)B
8:(2)C
9:(2)C
Class.isInstance(Object)
0:(2)C
1:(1)B
2:(2)C
3:(0)A
4:(0)A
5:(2)C
6:(0)A
7:(1)B
8:(2)C
9:(2)C
```



+ `newInstance()` 可能会抛出两种异常
    + `IllegalAccessException`：默认构造器是 `private` 的
    + `InstantiationException`：没有默认构造器
+ [TestNewInstanceEx](TestNewInstanceEx.java)

```java
class PriClass {
    // 私有构造器
    private PriClass(){}
}

class NoDefaultClass {
    // 没有默认构造器
    NoDefaultClass(int noUse) {}
}

public class TestNewInstanceEx {
    public static void main(String...args) {
        func(PriClass.class);
        func(NoDefaultClass.class);
    }

    public static <T> void func(Class<T> tClass) {
        try{
            tClass.newInstance();
        } catch (InstantiationException e) {
            System.err.println("InstantiationException");
        } catch (IllegalAccessException e) {
            System.err.println("IllegalAccessException");
        } finally {
            System.err.println(tClass.getName() + "\n~~~~~~");
        }
    }
}
```

```output
IllegalAccessException
PriClass
~~~~~~
InstantiationException
NoDefaultClass
~~~~~~
```



#### 14.3.1 使用类字面常量

+ `Class.forName(String)`
+ `ClassName.class`（类字面常量）



#### 14.3.2 动态的instanceof

+ 使用 `Class.isInstance(Object)`



### 14.3.3 递归计数

+ 使用 `Class.getSupperclass()` 获取继承链



## 14.4 注册工厂

+ 方便增加新的派生类



## 14.5 instanceof 与 Class 的等价性

+ `instanceof` 和 `isInstance()` **一致**
    + `A instanceof ClassName = true` ：`A` 是 类`ClassName ` 、类 `ClassName ` 派生类的一个实例
+ 直接比较 `Class` 对象：`equals()` 和 `==` **一致**
    + `A.getClass() == ClassName.class = true`：`A` 恰好是类 `ClassName ` 的一个实例
+ [FamilyVsExactType](FamilyVsExactType.java)

```java
class Base {}
class Derived extends Base {}

public class FamilyVsExactType {
    static void print(String args) {
        System.out.println(args);
    }

    static void test(Object x) {
        print("Testing x of type " + x.getClass());
        print("x instanceof Base " + (x instanceof Base));
        print("x instanceof Derived "+ (x instanceof Derived));
        print("Base.isInstance(x) "+ Base.class.isInstance(x));
        print("Derived.isInstance(x) " +
            Derived.class.isInstance(x));
        print("x.getClass() == Base.class " +
            (x.getClass() == Base.class));
        print("x.getClass() == Derived.class " +
            (x.getClass() == Derived.class));
        print("x.getClass().equals(Base.class)) "+
            (x.getClass().equals(Base.class)));
        print("x.getClass().equals(Derived.class)) " +
            (x.getClass().equals(Derived.class)));
    }

    public static void main(String[] args) {
        test(new Base());
        test(new Derived());
    }

}
```

```output
Testing x of type class Base
x instanceof Base true
x instanceof Derived false
Base.isInstance(x) true
Derived.isInstance(x) false
x.getClass() == Base.class true
x.getClass() == Derived.class false
x.getClass().equals(Base.class)) true
x.getClass().equals(Derived.class)) false

Testing x of type class Derived
x instanceof Base true
x instanceof Derived true
Base.isInstance(x) true
Derived.isInstance(x) true
x.getClass() == Base.class false
x.getClass() == Derived.class true
x.getClass().equals(Base.class)) false
x.getClass().equals(Derived.class)) true

```



## 14.6 反射：运行时的类信息

+ 在编译时，编译器必须执法所有要通过 `RTTI` 来处理的类
+ 不满足条件的例子
    + 从磁盘中/网络中获取了一段字节，被告知这一段字节代表一个类
    + 但是这个类在编译器为你的程序生成代码很久之后才出现
    + 如何使用这个类？
+ `Class` 类和 `java.lang.reflect` 类库对 **反射** 的概念进行了支持
+ 反射 `vs` `RTTI`
    + `RTTI`：在编译时就能够获取到 `.class` 文件
    + 反射：在编译时无法获取 `.class` 文件
+ 在观察 `JDK` 文档时，我们只能看到被这个类定义或者覆盖的方法（看不到继承的方法）
+ 反射机制对于这个进行了支持
+ [ShowMethods.java](ShowMethods.java)



## 14.7 动态代理

+ **代理** 是基本的设计模式之一，用于隐藏一些功能，只提供部分功能
    + 封装、修改
    + 方便的实现添加、修改
        + 例如度量原有方法的调用开销（但是不希望将其加入应用中）
+ [TestProxy.java](TestProxy.java)

```java
interface Connectable {
    public void connect();
    public void close();
}

class RealObject implements Connectable {
    public void connect() {
        System.out.println("RealObject connect...");
    }
    public void close() {
        System.out.println("RealObject close...");
    }
}

class SimpleProxy implements Connectable {
    private Connectable ro;
    public SimpleProxy(Connectable ro) {
        this.ro = ro;
    }
    public void connect() {
        System.out.println("SimpleProxy");
        ro.connect();
    }
    public void close() {
        System.out.println("SimpleProxy");
        ro.close();
    }
}

public class TestProxy {
    public static void run(Connectable cd) {
        cd.connect();
        cd.close();
    }

    public static void main(String...args) {
        run(new SimpleProxy(new RealObject()));
        run(new RealObject());
    }
}
```

```output
SimpleProxy
RealObject connect...
SimpleProxy
RealObject close...
RealObject connect...
RealObject close...
```

+ **动态代理**
    + 动态的创建代理并启用动态地处理对所代理方法的调用
    + 在动态代理上所做的所有调用都会被重定向到单一的 **调用处理器** 上
    + 它的工作是揭示调用的类型并确定相应的对策
+ [DynamicProxy](TestDynamicProxy.java)

```java
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

interface Connectable {
    public void connect();
    public void close();
}

class RealObject implements Connectable {
    public void connect() {
        System.out.println("RealObject connect...");
    }
    public void close() {
        System.out.println("RealObject close...");
    }
}

class DynamicProxy implements InvocationHandler {
    private Connectable ro;
    public DynamicProxy(Connectable ro) {
        this.ro = ro;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
        System.out.println("**** proxy: " + proxy.getClass() +
            ", method: " + method + ", args: " + args);
        if(args != null) {
            for(Object arg : args) {
                System.out.println("  " + arg);
            }
        }
        return method.invoke(ro, args);
    }
}

public class TestDynamicProxy {
    public static void run(Connectable cd) {
        cd.connect();
        cd.close();
    }

    public static void main(String...args) {
        RealObject ro = new RealObject();
        run(ro);
        // proxy
        Connectable proxy = (Connectable) Proxy.newProxyInstance(
            Connectable.class.getClassLoader(),
            new Class[]{ Connectable.class }, // 实现的接口列表(不是类、抽象类)
            new DynamicProxy(ro)
        );
        run(proxy);
    }
}
```

```output
RealObject connect...
RealObject close...
**** proxy: class $Proxy0, method: public abstract void Connectable.connect(), args: null
RealObject connect...
**** proxy: class $Proxy0, method: public abstract void Connectable.close(), args: null
RealObject close...
```

+ 可以通过对于 `invoke` 函数中的参数的检查，从而实现对某些特定代理方法的特殊处理
    + 例如上述例子中的 `Object proxy, Method method, Object[] args`



## 14.8 空对象

+ 使用内置的 `null` 表示缺少对象时，在每次使用时都需要进行检查（麻烦、枯燥）
+ 引入 **空对象** 的想法很实用，可以假设所有的对象都不会为 `null`
+ 最简单的方法，创建一个标记接口
    + 可以使用 `instanceof` 来检测

```java
public interface Null {}
```

+ 一般空对象都是单例，可以设计为 `final`，将其构造函数设为 `private`
    + 这样子可以方便的比较是否为 `Null` 对象
    + 另一方面可以使用 `equals/==` 来比较
+ 如此设计在某些地方仍然需要像检测 `null` 一样检测 `Null` ，但是在某些地方也可以获得方便



### 14.8.1 模拟对象和桩



## 14.9 接口和类型信息

+ `interface` 的关键作用就是将隔离构件，降低耦合性
+ 但是客户端可能会有一些奇怪的举动
+ 如下述例子，`a` 被当作 `B` 类来实现的，失去了 `interface` 的意义
    + 在 `a` 中调用了不存在在 `interface` `A` 中的方法

```java
// 示例代码
interface A{
    public void f();
}
class B implements A {
    public void f(){}
    public void g(){}
}
public class Test {
    public static void main(String...args) {
        A a = new B();
        a.f();
        // a.g(); // CE
        if(a instanceof B) {
            B b = (B)a;
            b.g();
        }
    }
}
```

+ 将如上 `B` 类声明为包访问权限，这样子就能够避免在包外将 `a` 转型为 `B`
    + 但是通过反射机制，仍然可以访问到低权限的方法
+ [TestPri.java](TestPri.java)

```java
import java.lang.reflect.Method;
class A {
    private void f() {
        System.out.println("private:A.f()");
    }
}

public class TestPri extends A {
    public static void main(String...args) {
        A a = new A();
        // a.f(); // 错误: f() 在 A 中是 private 访问控制
        try {
            Method m = a.getClass().getDeclaredMethod("f");
            m.setAccessible(true);
            m.invoke(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```output
private:A.f()
```

+ 真是可怕
+ 配合 `javap -c -private A.class` 可以调用所有方法

```powershell
javap -private A.class
```

```output
Compiled from "TestPri.java"
class A {
  A();
  private void f();
}
```

```powershell
javap A.class
```

```output
Compiled from "TestPri.java"
class A {
  A();
}
```

+ 对于 **私有内部类**、**匿名类**、**域**，反射都可以调用非正常访问权限的方法
    + `final` 域是安全的，接受修改，但实际上没有修改（正常不能修改，`CE`）
+ [TestPri.java](TestPri.java)