# Chapter_15 泛型(part2)

> [总目录](../README.md)

---

[TOC]

---

+ [part1](./README.md)



## 15.11 问题



### 15.11.1 任何基本类型都不能作为类型参数

+ 使用自动包装机制 `List<Integer>`

+ 性能上可能会出现问题

```java
// 1
List<Integer> li = new ArrayList<Integer>();
for(int i = 0; i < 5; i++) { li.add(i); }
// 2
Byte[] possibles = { 1,2,3,4,5,6,7,8,9 };
Set<Byte> mySet =  new HashSet<Byte>(Arrays.asList(possibles));
```

+ 数组不支持转化

```java
// int[] a = new Integer[10];
// 错误: 不兼容的类型: Integer[]无法转换为int[]

int b = new Integer(10);

// Integer[] c = new int[10];
// 错误: 不兼容的类型: int[]无法转换为Integer[]

Integer d = 10;
```



### 15.11.2 实现参数化接口

```java
// CE
// 错误: 无法使用以下不同的参数继承Payable: <Hourly> 和 <Employee>
interface Payable<T> {}
class Employee implements Payable<Employee> {}
class Hourly extends Employee
  implements Payable<Hourly> {}

// OK
interface Payable<T> {}
class Employee implements Payable<Employee> {}
class Hourly extends Employee
  implements Payable<Employee> {}

// OK
interface Payable {}
class Employee implements Payable {}
class Hourly extends Employee
  implements Payable {}
```



### 15.11.3 转型和警告

+ 带有泛型类型参数的转型或者 `instanceOf` 不会有任何效果
    + 内部存储为 `Object` ，`get` 时在转型回 `T`
    + 由于擦除，这里其实只是将 `Object` 转型为 `Object`
        + 编译器会在使用时自动添加转型代码
            + `String s = strings.pop();`
            + ` checkcast  #10  // class java/lang/String`
    + 但是不能写作 `public Object pop() { return (Object)storage[--index]; }`

```java
// 示例代码
class FixedSizeStack<T> {
    private int index = 0;
    private Object[] storage;
    public FixedSizeStack(int size) {
        storage = new Object[size];
    }
    public void push(T item) { storage[index++] = item; }
    // @SuppressWarnings("unchecked")
    public T pop() { return (T)storage[--index]; }
}

public class GenericCast {
    public static final int SIZE = 10;
    public static void main(String[] args) {
        FixedSizeStack<String> strings =
            new FixedSizeStack<String>(SIZE);
        for(String s : "A B C D E F G H I J".split(" "))
            strings.push(s);
        for(int i = 0; i < SIZE; i++) {
            String s = strings.pop();
            System.out.print(s + " ");
        }
    }
}
/* Output:
J I H G F E D C B A
*/
```

+ 又是泛型没有消除对转型的需要，这就会由编译器产生警告，而这个警告是不恰当的

```java
import java.io.*;
import java.util.*;

class Apple{}
public class NeedCasting {
    // @SuppressWarnings("unchecked")
    public void f(String[] args) throws Exception {
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(args[0]));
        List<Apple> shapes = (List<Apple>)in.readObject();
        // readObject 不知道它读的是什么,因此必须转型
        // 但是转型了又会报警告
    }
}
```



### 15.11.4 重载

+ **擦除**

```java
// CE
// 错误: 名称冲突: f(List<W>)和f(List<T>)具有相同疑符
import java.util.*;
public class UseList<W,T> {
    void f(List<T> v) {}
    void f(List<W> v) {}
}
```

```java
// OK
import java.util.*;
public class UseList2<W,T> {
    void f1(List<T> v) {} // 方法名不同
    void f2(List<W> v) {}
}
```



### 15.11.5 基类劫持了接口

```java
//  错误: 无法使用以下不同的参数继承Comparable: <Cat> 和 <ComparablePet>
public class ComparablePet implements Comparable<ComparablePet>{
    @Override
    public int compareTo(ComparablePet cp) {
        // ...
        return 0;
    }
}

class Cat extends ComparablePet implements Comparable<Cat> {
    @Override
    public int compareTo(Cat cat) {
        // ...
        return 0;
    }
}
```

+ 以上确实是个令人头疼的问题
    + 因为这样的 **窄化** 是很有意义的（不同动物之间比较没有意义）

```java
class Cat extends ComparablePet {
    @Override
    public int compareTo(ComparablePet cp) {
        // if(cp instanceof Cat) {...}
        // ...
        return 0;
    }
}
```



## 15.12 自限定的类型

```java
class SelfBounded<T extends SelfBounded<T>> {
    // ...
}
```

### 15.12.1 古怪的循环泛型

+ 简单例子入手

```java
class GenericType<T> {}
public class CuriouslyRecurringGeneric
  extends GenericType<CuriouslyRecurringGeneric> {}
```

+ [CRGWithBasicHolder](CRGWithBasicHolder.java)
+ 注意 `Subtype` 接收的参数和返回值的类型都是 `Subtype` ，而不仅仅是基类 `BasicHolder`
+ `CRG` 的本质
    + **基类用导出类代替其参数**

```java
class BasicHolder<T> {
    T element;
    void set(T arg) { element = arg; }
    T get() { return element; }
    void f() {
        System.out.println(element.getClass().getSimpleName());
    }
}

class Subtype extends BasicHolder<Subtype> {}

public class CRGWithBasicHolder {
    public static void main(String[] args) {
        Subtype st1 = new Subtype(), st2 = new Subtype();
        st1.set(st2);
        Subtype st3 = st1.get();
        st1.f();
    }
}
/* Output
Subtype
*/
```



### 15.12.2 自限定

+ 当然 `BasicHolder` 可以采用其他参数

```java
class Other {}
class BasicOther extends BasicHolder<Other> {}
/* Output
Other
*/
```

+ 自限定将采用额外的步骤，强制泛型当作自己的边界参数来使用

```java
class A extends SelfBound<A> {}
```

+ 示例如下

```java
class SelfBounded<T extends SelfBounded<T>> {}

class A extends SelfBounded<A> {}
// OK

class B extends SelfBounded<A> {}
// OK(因为 A 满足 A extends SelfBouneded<A>)

class D {}
// class E extends SelfBounded<D> {}
// 错误: 类型参数D不在类型变量T的范围内

class F extends SelfBounded {}
// OK(这表示自限定还是并非强制执行)
```

+ 自限定用于泛型方法

```java
<T extends SelfBounded<T>> T  func(T arg) {
    // return ...;
}
```



### 15.12.3 参数协变

+ 自限定类型的价值在于能够产生协变类型（**方法参数类型会随着子类变化**）
+ 协变返回类型在 `Java5` 引入
    + 允许子类的重载函数返回比基类更具体的类型
+ 对于自限定类，只接受子类
    + 只接受 `T`，否则为重载
+ [TestBaseDerived](TestBaseDerived.java)

```java
/**
 * @author banbao
 * @comment 修改自示例代码
 */
class Base {
    public Base test(){ return new Base(); }
    public void test2(Base b){
        System.out.println("Base:test2:Base");
    }
}
class Derived extends Base {
    @Override
    public Derived test(){ return new Derived(); }
    // @Override
    // 和基类是两个方法,只是重载(Overload)
    public void test2(Derived d){
        System.out.println("Derived:test2:Derived");
    }
}

class Base2<T> {
    public void test2(T b){
        System.out.println("Base2:test2:T");
    }
}
class Derived2 extends Base2<Base> {
    @Override
    public void test2(Base b){
        System.out.println("Derived2:test2:Base");
    }
    // @Override
    // 重载
    public void test2(Derived b){
        System.out.println("Derived2:test2:Derived");
    }
}

class SelfBounded<T extends SelfBounded<T>> {
    public void test2(T b){
        System.out.println("SelfBounded:test2:T");
    }
}

class Derived3 extends SelfBounded<Derived3> {
    @Override
    public void test2(Derived3 b){
        System.out.println("Derived3:test2:Derived3");
    }
}

public class TestBaseDerived {
    public static void main(String...args) {
        // 1. 正常的
        new Derived().test2(new Base());
        new Derived().test2(new Derived());
        // 2. 泛型
        new Derived2().test2(new Base());
        new Derived2().test2(new Derived());
        // 3. 自限定
        new Derived3().test2(new Derived3());
    }
}
/* Output
Base:test2:Base
Derived:test2:Derived
Derived2:test2:Base
Derived2:test2:Derived
Derived3:test2:Derived3
*/
```



## 15.13 动态类型安全

+ 由于可以向 `Java5` 之前的代码传递泛型容器，所以旧式代码可能会破坏容器
+ `java.util.Collections` 中有一组工具，解决这种情况下的类型检查问题

```java
checkedCollection()
checkedList()
checkedMap()
checkedSet()
checkedSortedMap()
checkedSortedSet()
// 将希望动态检查的容器当作第一个参数
// 强制要求的类型当作第二个参数
// 当插入元素类型不正确时,抛出异常 ClassCastException
```

+ 一个例子
+ [CheckedList](CheckedList.java)

```java
/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Animal {}
class Cat extends Animal {}
class Dog extends Animal {
    public void bark() {
        System.out.println("Dog Bark!");
    }
}

public class CheckedList {
    @SuppressWarnings("unchecked")
    public static void oldStyleMethod(List dogs) {
        dogs.add(new Cat());
    }

    public static void main(String...args) {
        test1(); // 可以编译,但是运行抛出异常
        test2(); // 可以编译,但是运行抛出异常
    }

    public static void test2() {
        List<Dog> dogs = Collections.checkedList(
            new ArrayList<>(), Dog.class);
        oldStyleMethod(dogs);
        // 上一行抛出异常
        // ClassCastException: Attempt to insert class Cat
        //     element into collection with element type class Dog
        for(Dog d : dogs) {
            d.bark();
        }
    }

    public static void test1() {
        List<Dog> dogs = new ArrayList<>();
        // dogs.add(new Cat()); // 错误: 对于add(Cat), 找不到合适的方法
        oldStyleMethod(dogs);
        // ClassCastException: Cat cannot be cast to Dog
        // 下一行抛出异常
        for(Dog d : dogs) {
            d.bark();
        }
    }
}
```



## 15.14 异常

+ 泛型类不能直接或者间接继承于 `Throwable`
    + **擦除**，运行时不能知道确切类型

```java
class A{}
class MyA<T> extends A{}
// class MyException<T> extends Exception{}
// 错误: 泛型类不能扩展 java.lang.Throwable
```

+ 但是类型参数可以在一个方法的 `throws` 子句中用到
+ 这使得可以编写随检查型异常的类型而发生变化的泛型代码
+ [ThrowGenericException](ThrowGenericException.java)

```java
interface Processor<T,E extends Exception> {
    void process(List<T> toRun) throws E;
}
```



## 15.15 混型

### 15.15.1 C++中的混型

+ **多重继承的原因之一就是为了使用混型**
+ `TimeStamped<SerialNumbered<Basic> > mixin1, mixin2;`

```c++
//: generics/Mixins.cpp
#include <string>
#include <ctime>
#include <iostream>
using namespace std;

template<class T> class TimeStamped : public T {
    long timeStamp;
public:
    TimeStamped() { timeStamp = time(0); }
    long getStamp() { return timeStamp; }
};

template<class T> class SerialNumbered : public T {
    long serialNumber;
    static long counter;
public:
    SerialNumbered() { serialNumber = counter++; }
    long getSerialNumber() { return serialNumber; }
};

// Define and initialize the static storage:
template<class T> long SerialNumbered<T>::counter = 1;

class Basic {
    string value;
public:
    void set(string val) { value = val; }
    string get() { return value; }
};

int main() {
    TimeStamped<SerialNumbered<Basic> > mixin1, mixin2;
    mixin1.set("test string 1");
    mixin2.set("test string 2");
    cout << mixin1.get() << " " << mixin1.getStamp() <<
        " " << mixin1.getSerialNumber() << endl;
    cout << mixin2.get() << " " << mixin2.getStamp() <<
        " " << mixin2.getSerialNumber() << endl;
}
/* Output: (Sample)
test string 1 1129840250 1
test string 2 1129840250 2
*/
```

+ `Java` 不允许，因为擦除会忘记基类类型，因此泛型类不能直接继承自一个泛型参数

```java
interface A { int a(); }
interface B { int b(); }
public class Sample {
    public static void main(String...args) {
        A<B<Sample>> abs = new A<>();
        System.out.println(abs.a);
        // System.out.println(abs.b); // 错误: 找不到符号
    }
}

class A <T> { int a; }
class B <T> { int b; }
```



### 15.15.2 与接口混合

+ `Java` 的接口实现方法

```java
public class Sample implements A,B {
    int a;
    int b;
    @Override
    public int getA() { return a; };
    @Override
    public int getB() { return b; };
    public static void main(String...args) {
        Sample abs = new Sample();
        System.out.println(abs.a);
        System.out.println(abs.b);
    }
}

interface A { int getA(); }
interface B { int getB(); }

/* Output
0
0
*/
```



### 15.15.3 使用装饰器模式

+  当你观察混型的使用方式时，就会发现混型概念好像与**装饰器设计模式**关系很近
+ 装饰器经常用于满足各种可能的组合，而直接子类化会产生过多的类，因此是不实际的
+  装饰器模式使用分层对象来动态透明地向单个对象中添加责任。
+ 装饰器指定包装在最初的对象周围的所有对象都具有相同的基本接口。

```java
class Basic {
    private String value;
    public void set(String val) { value = val; }
    public String get() { return value; }
}

class Decorator extends Basic {
    protected Basic basic;
    public Decorator(Basic basic) { this.basic = basic; }
    public void set(String val) { basic.set(val); }
    public String get() { return basic.get(); }
}

class TimeStamped extends Decorator {
    private final long timeStamp;
    public TimeStamped(Basic basic) {
        super(basic);
        timeStamp = new Date().getTime();
    }
    public long getStamp() { return timeStamp; }
}
```

+  产生自泛型的类包含所有感兴趣的方法，但是由使用装饰器所产生的对象类型是最后被装饰的类型
+ 也就是说，尽管可以添加多个层，但是最后一层才是实际的类型，因此只有最后一层的方法是可视的，而混型的类型是所有被混合到一起的类型
+ 因此对于装饰器来说，其明显的缺陷是它只能有效地工作于装饰中的一层（最后一层），而混型方法显然会更自然一些。因此，装饰器只是对由混型提出的问题的一种局限的解决方案。



### 15.15.4 与动态代理混合

+ 由于动态代理的限制，每个被混入的类都必须是某个接口的实现



## 15.16 潜在类型

+ 代码 **泛化**
+ **潜在类型机制/结构化类型机制/鸭子类型机制**
    + 如果它走起来像鸭子，并且叫起来像鸭子，俺么你就可以将它作为鸭子对待
+ `C++/Python/Ruby/Smalltalk` 支持

```c++
#include <iostream>
using namespace std;

class Dog {
public:
    void speak() {
        cout << "Dog:speak()" << endl;
    }
    void sit() {}
};

class Robot{
public:
    void speak() {
        cout << "Robot:speak()" << endl;
    }
    void work() {}
};

template<class T> void perform(T anything) {
    anything.speak();
}

int main() {
    Dog d;
    Robot r;
    perform(d);
    perform(r);
    return 0;
}
/* Output
Dog:speak()
Robot:speak()
*/
```

+ 保证不发生错误
    + `Python` 在运行时保证，`C++` 在编译时保证
+ `Java` 不支持，可以使用 **接口** 实现
    + 因为泛型是后期加入的特性



## 15.17 对缺乏潜在类型的补偿

### 15.17.1 反射

```java
import java.lang.reflect.Method;
import java.lang.NoSuchMethodException;

class Dog {
    public void speak() {
        System.out.println("Dog:speak()");
    }
    public void sit() {
        System.out.println("Dog:sit()");
    }
}

class Robot {
    public void speak() {
        System.out.println("Robot:speak()");
    }
    public void work() {
        System.out.println("Robot:work()");
    }
}

class PseudoLatent {
    public static void perform(Object obj) {
        Class<?> cl = obj.getClass();
        // speak
        try {
            Method speakMethod = cl.getMethod("speak");
            speakMethod.invoke(obj);
        } catch(NoSuchMethodException e) {
            System.out.println("The class don't have the method speak()!");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        // work
        try {
            Method speakMethod = cl.getMethod("work");
            speakMethod.invoke(obj);
        } catch(NoSuchMethodException e) {
            System.out.println("The class don't have the method work()!");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}

public class LatentReflection {
    public static void main(String...args) {
        PseudoLatent.perform(new Dog());
        PseudoLatent.perform(new Robot());
    }
}
/* Output
Dog:speak()
The class don't have the method work()!
Robot:speak()
Robot:work()
*/
```



### 15.17.2 将一个方法用于序列

+ 反射将所有类型检查都推迟到了运行时

+ 怎么实现编译时检查呢？

    + 实现编译期类型检查和潜在类型机制

+ **可变参数** + **接口** + **反射** + **边界与通配符**

+ 示例

    + `apply`

    ```java
    public static <T, S extends Iterable<? extends T>>
        void apply(S seq, Method f, Object... args) {
        // ...
    }
    ```

    + 上述方法将方法 `f()` 序列 `seq` 中的所有对象，`args` 为参数

+ 这里一个方便的地方是 `Iterable` 是内建的接口，操作起来比较方便

+ `newInstance()`

    + 不能确定是否有默认构造函数，增加工厂方法（非原生）



### 15.17.3 当你并未碰巧拥有正确的接口时

+ 例如我们要实现一个 `fill` 方法，此时没有内建的 `Addable`
+ 于是我们实现为如下代码（限制在 `Collection`）

```java
public static <T> void fill(Collection<T> collection,
    Class<? extends T> classToken, int size) {
```

+ 但是如此泛化能力减弱，只能工作于 `Collecion`
    + 即使有一个类有 `add` 方法，也适用于 `fill`
+ 诶，此时，潜在类型有用了



### 15.17.4 用适配器仿真潜在类型机制

+ 潜在类型机制
    + **隐式接口**
+ 适配器
    + **包装**



## 15.18 将函数对象用作策略

+ **策略设计模式**
+ **函数对象**