# Chapter_15 泛型(part1)

> [总目录](../README.md)

---

[TOC]

---

+ [part2](./README-part2.md)
+ 常见的泛化形式
    + 多态
    + 传递接口作为参数



## 15.1 与 C++ 的比较



## 15.2 简单泛型

+ 泛型出现的原因之一：**容器类**
+ 持有对象
    + 持有一个对象
        + 特定类型
        + 任意类型（`Object`）
        + 泛型（不指定为具体类型）
+ [Holder.java](Holder.java)

```java
public class Holder<T> {
    private T object;
    public void set(T object) { this.object = object; }
    public T get() { return this.object; }
    public static void main(String...args) {
        // String
        Holder<String> holder1 = new Holder<>();
        holder1.set("test string");
        String strGet = holder1.get();
        System.out.println(strGet);
        // Integer
        Holder<Integer> holder2 = new Holder<>();
        holder2.set(10086); // auto box
        int intGet = holder2.get();
        System.out.println(intGet);
    }
}
```

```output
test string
10086
```

+ 泛型和多态不冲突，声明具体类型之后，只能放入该类型（可以是子类）



### 15.2.1 一个元组类库

+ 一次调用返回多个对象
+ **元组/数据传送对象/信使**
    + 将一组对象打包存储在一个对象里
    + 内含的对象类型可以不一致
    + 允许读取其中的对象，但是不允许添加元素
    + 隐含的保存了顺序
+ [TwoTuple](TwoTuple.java)

```java
public class TwoTuple<T1, T2> {...}
```

+ 若不希望元组中的元素被修改，可以设置为 `final`
+ 若允许被修改，则可以设为 `private` 以保安全
+ 简单的使用继承机制实现更长的元组

```java
public class ThreeTuple<T1, T2, T3> extends TwoTuple<T1, T2> {...}
```



### 15.2.2 一个堆栈类

+ 通过链表实现 `stack`
+ [MyLinkedStack](MyLinkedStack.java)
+ 注意内部类是可以访问外围类的类型信息的



### 15.2.3 RandomList

+ 内部存储一个 `ArrayList<T>`



## 15.3 泛型接口

+ **生成器（generator）**
    + **工厂方法设计模式** 的应用

```java
public interface Generator<T> {
    T next();
}
```

+ 实现这个接口

```java
public class CoffeeGenerator implements Generator<Coffee> {
    @Override
    public Coffee next(){...}
}
```

+ **适配器** 的方法实现 `Iterable` 接口，可以用于迭代
    + 继承
    + 重写这个类



## 15.4 泛型方法

+ 所在类可以是泛型类也可以不是泛型类
+ 将泛型参数列表置于 **返回值之前**

```java
public <T> void func(T x) {
    ...
}
```

+ 使用泛型类时，必须在创建对象的时候指定类型参数的值
+ 使用泛型方法的时候不需要，编译器会进行 **类型参数推断（type argument inference）**
    + 传入基本类型时，会进行自动打包



### 15.4.1 杠杆利用类型参数推断

```java
HashMap<String, String> map = new HashMap<String,String>();
HashMap<String, String> map = new HashMap<>(); // Java 8 OK
```

+ `Java8` 之前类型自动推断只能用于 **赋值语句**

```java
// 示例代码
import typeinfo.pets.*;
import java.util.*;
import net.mindview.util.*;

public class LimitsOfInference {
    static void f(Map<Person, List<? extends Pet>> petPeople) {}
    public static void main(String[] args) {
        f(New.map()); // Java 8 OK
        f(New.<Person, List<? extends Pet>> map()); // 显式声明(Java8 没必要了)
    }
}
```



#### 15.4.1.1 显式的类型说明

+ 需要在后面加 `.<>` 来声明
+ 现在已经没有必要了



### 15.4.2 可变参数与泛型方法

+ [VarArgs](VarArgs.java)

```java
/**
 * @author banbao
 */

import java.util.List;
import java.util.ArrayList;

public class VarArgs {
    public static <T> List makeList(T...args) {
        List<T> list = new ArrayList<>();
        for(T t : args) {
            list.add(t);
        }
        return list;
    }
    public static void main(String...args) {
        List list = VarArgs.makeList(
            "test string for the class VarArgs.".split(" ")
        );
        System.out.println(list.size() + ":" + list);
    }
}
/* Compile(javac VarArgs.java)
注: VarArgs.java使用了未经检查或不安全的操作。
注: 有关详细信息, 请使用 -Xlint:unchecked 重新编译。
*/

/* Compile(>javac -Xlint:unchecked VarArgs.java)
VarArgs.java:8: 警告: [unchecked] 参数化 vararg 类型T的堆可能已受污染
    public static <T> List makeList(T...args) {
                                        ^
  其中, T是类型变量:
    T扩展已在方法 <T>makeList(T...)中声明的Object
1 个警告
*/
```

```output
6:[test, string, for, the, class, VarArgs.]
```



### 15.4.3 用于 Generator 的泛型方法

+ 往一个 `Collection` 里面填充元素

```java
public static <T> Collection<T>
    fill(Collection<T> coll, Generator<T> gen, int n) {
    for(int i = 0; i < n; i++)
        coll.add(gen.next());
    return coll;
}
```



### 15.4.4 一个通用的 Gennerator

+ 传入 `Class` 参数，通过 `newInstance()` 来创建对象
    + 要求有默认构造函数
    + 要求类 `public`
+ **通用** 通过泛型实现
+ 本质都是为了简化代码，但是 `java8` 之后意义不大了



### 15.4.5 简化元组的使用

+ 本质都是为了简化代码，但是 `java8` 之后意义不大了



### 15.4.6 一个 Set 实用工具

+ 完成数学中关于集合的操作

```java
public static <T> Set<T> union(Set<T> a, Set<T> b) {
    Set<T> result = new HashSet<T>(a);
    result.addAll(b);
    return result;
}
```

+ 一个作用就是展示 **派生类和基类方法的不同**



## 15.5 匿名内部类

+ [OceanNet](OceanNet.java)

```java
class SmallFish extends Fish {
    ...
    public static Generator<SmallFish> generator =
        // 这里必须要声明<SmallFish>,否则报错
        new Generator<SmallFish>() {
            public SmallFish next() {
                return new SmallFish();
            }
        };
}
```



## 15.6 构建复杂模型

```java
ArrayList<TwoTuple<String, Integer>> list =
    new ArrayList<>();
```

```java
class Book {}
class Shelf extends ArrayList<Book> {}
class Aisle extends ArrayList<Shelf> {}
// Aisle 等价于 ArrayList<ArrayList<Book>>
// 但更容易看懂,更简单,可以赋予更强大的功能
```



## 15.7 擦除的神秘之处

```java
ArrayList list = ArrayList.class.newInstance(); // OK
ArrayList<Interger> list2 = ArrayList<Integer>.class.newInstance(); // CE
```

+ [ErasedTypeEquivalence](ErasedTypeEquivalence.java)

```java
import java.util.ArrayList;
public class ErasedTypeEquivalence {
    public static void main(String...args) {
        Class c1 = new ArrayList<Integer>().getClass();
        Class c2 = new ArrayList<String>().getClass();
        System.out.println(c1 == c2);
    }
}
```

```output
true
```

+ [LostInformation](LostInformation.java)
+ 通过 `getTypeParameters()` 获取泛型类的参数，**只能获取到占位符**

```java
System.out.println(
    Arrays.toString(
        new ArrayList<Frob>()
        .getClass()
        .getTypeParameters()
    )
);
```

+ **在泛型代码内部，无法获得任何有关泛型参数类型的信息**
+ `Java` 泛型是通过 **擦除** 来实现的，这意味着在使用泛型时，任何具体的类型信息都已经被擦除了
    + 因此 `ArrayList<String>`  和 `ArrayList<Integer>` 在运行时是相同的类型
    + 都是原生的 `ArrayList`



### 15.7.1 C++ 的方式

+ `C++` 在实例化模板类的时候会检查函数
    + 模板代码知道其模板参数的类型（否则无法检查）
    + 下述例子中检查 `obj` 是否具有函数 `f()`
+ [TestTemplates](TestTemplates.cpp)

```c++
# include <iostream>
using namespace std;

template<class T> class TestTemplates{
    T obj;
public:
    TestTemplates(T _obj) : obj(_obj){}
    void test() { obj.f(); }
};

class HasF {
public:
    void f() { cout << "HasF:f()" << endl;}
};

class NotHasF {};

int main() {
    // HasF
    HasF hf;
    TestTemplates<HasF> hasF(hf);
    hasF.test();
    // NotHasF
    // error C2039: “f”: 不是“NotHasF”的成员
    // NotHasF nhf;
    // TestTemplates<NotHasF> notHasF(nhf);
    // notHasF.test();
    return 0;
}
```

```output
HasF:f()
```

+ `Java` 由于擦除相同的代码会报错
+ [TestTemplates](TestTemplates.java)

```java
class Templates<T> {
    private T obj;
    public Templates(T obj) { this.obj = obj; }
    public void test() {
        // obj.f();
        // 错误: 找不到符号
    }
}
```

+ 需要用 `extends` 声明范围

```java
class Templates2<T extends HasF> {
    private T obj;
    public Templates2(T obj) { this.obj = obj; }
    public void test() {
        obj.f();
    }
}
```

+ 注意声明了边界参数也只是占位符

```java
System.out.println(
    Arrays.toString(
        new Templates2<HasF>(new HasF())
        .getClass()
        .getTypeParameters()
    )
);
```

+ 对于 `Template2` 可以用 `Templates3` 实现同样功能，但是若存在一个返回 `T` 类型的方法则不一样
+ 泛型会返回具体类型 `T`，但是 `Templates3` 只能返回基类

```java
class Templates3 {
    private HasF obj;
    public Templates3(HasF obj) { this.obj = obj; }
    public void test() {
        obj.f();
    }
}
```



### 15.7.2 迁移兼容性

+ 泛型没有使用 **实例化** 而是 **擦除** 实现是因为：泛型是后来出现的，在 `Java1.0` 中还没有
+ 泛型类型只有在静态类型检查时期被检查，之后程序中的泛型类型都将被擦除，替换为非泛型上界
+ 擦除的核心动机：使得泛化的客户端可以用非泛化的类库实现
+ 一种对于向后兼容的折中实现



### 15.7.3 擦除的问题

+ `T` 被擦除为 `Object`
+ `set()` 会报警告
    + `@SuppressWarnings("unchecked")` 取消警告，可以放置在方法之上

```java
// 示例代码
class GenericBase<T> {
    private T element;
    public void set(T arg) { element = arg; }
    public T get() { return element; }
}

class Derived1<T> extends GenericBase<T> {}

class Derived2 extends GenericBase {} // No warning

// class Derived3 extends GenericBase<?> {}
// Strange error:
//   unexpected type found : ?
//   required: class or interface without bounds

public class ErasureAndInheritance {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Derived2 d2 = new Derived2();
        Object obj = d2.get();
        d2.set(obj); // Warning here!
  }
}
```



### 15.7.4 边界处的动作

+ **创建数组**
+ 疑惑：可以表示没有意义的事物
    + 在调用 `create()` 的时候，并不知道 `kind` 的具体类型
    + 创建泛型数组的建议方式 `(T[])Array.newInstance(kind, size)`

```java
// 示例代码
import java.lang.reflect.Array;
import java.util.Arrays;
public class ArrayMaker<T> {
    private Class<T> kind;
    public ArrayMaker(Class<T> kind) { this.kind = kind; }
    @SuppressWarnings("unchecked")
    T[] create(int size) {
        return (T[])Array.newInstance(kind, size);
        // return Array.newInstance(kind, size);
        // 错误: 不兼容的类型: Object无法转换为T[]
    }
    public static void main(String[] args) {
        ArrayMaker<String> stringMaker =
            new ArrayMaker<String>(String.class);
        String[] stringArray = stringMaker.create(9);
        System.out.println(Arrays.toString(stringArray));
    }
}
```

+ **创建容器**

```java
// 示例代码
import java.util.List;
import java.util.ArrayList;
public class ListMaker<T> {
    List<T> create() {
        return new ArrayList<T>();
        // return new ArrayList();
        // 警告: [unchecked] 未经检查的转换
    }
    public static void main(String[] args) {
        ListMaker<String> stringMaker = new ListMaker<String>();
        List<String> stringList = stringMaker.create();
    }
}
```

+ 这里 `create()` 方法中的 `ArrayList<T>`  并非没有意义
    + 即使编译器无法知道有关 `create()` 方法中 `T` 的任何信息
    + 但是仍然可以在编译器确保放置到 `result` 中的对象具有 `T` 类型，使其适合 `ArrayList<T>`
    + 因此即使擦除在方法或者类内部移除了有关实际类型的信息
    + 编译器依然能够确保在方法或类中使用类型的 **内部一致性**

```java
// 示例代码
import java.util.List;
import java.util.ArrayList;
public class FilledListMaker<T> {
    List<T> create(T t, int n) {
        List<T> result = new ArrayList<T>();
        for(int i = 0; i < n; i++)
            result.add(t);
        return result;
    }
    public static void main(String[] args) {
        FilledListMaker<String> stringMaker =
            new FilledListMaker<String>();
        List<String> list = stringMaker.create("Hello", 4);
        System.out.println(list);
    }
}
```

```output
[Hello, Hello, Hello, Hello]
```

+ 对示例代码进行反编译
    + [SimpleHolder.java](SimpleHolder.java)
    + [GenericHolder.java](GenericHolder.java)

```java
// 示例代码
public class GenericHolder<T> {
    private T obj;
    public void set(T obj) { this.obj = obj; }
    public T get() { return obj; }
    public static void main(String[] args) {
        GenericHolder<String> holder = new GenericHolder<String>();
        holder.set("Item");
        String s = holder.get();
    }
}
```



+ 生成的字节码是相同的
    + 对进入 `set()` 类型进行检查是不需要的（由编译器完成）
    + 对 `get()` 返回的值进行转型是需要的（这由编译器自动插入代码）

```assembly
Compiled from "SimpleHolder.java"
public class SimpleHolder {
  public SimpleHolder();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public void set(java.lang.Object);
    Code:
       0: aload_0
       1: aload_1
       2: putfield      #2                  // Field obj:Ljava/lang/Object;
       5: return

  public java.lang.Object get();
    Code:
       0: aload_0
       1: getfield      #2                  // Field obj:Ljava/lang/Object;
       4: areturn

  public static void main(java.lang.String[]);
    Code:
       0: new           #3                  // class SimpleHolder
       3: dup
       4: invokespecial #4                  // Method "<init>":()V
       7: astore_1
       8: aload_1
       9: ldc           #5                  // String Item
      11: invokevirtual #6                  // Method set:(Ljava/lang/Object;)V
      14: aload_1
      15: invokevirtual #7                  // Method get:()Ljava/lang/Object;
      18: checkcast     #8                  // class java/lang/String
      21: astore_2
      22: return
}
```

+ **边界**
    + 对象进入和离开方法的地点
    + 运行时的问题就是在边界
+ 泛型中所有的动作都发生在边界处
    + 对传递 **进来** 的值进行额外的 **编译器检查**
    + 并插入对传递 **出去** 的值的 **转型**
+ **边界就是发生动作的地方**



## 15.8 擦除的补偿

+ 擦除丢失了泛型代码中某些操作的能力，任何在运行时需要知道确切类型的操作都无法工作
+ [Erased](Erased.java)

```java
/**
 * @author banbao
 * @comment 修改自示例代码
 */

public class Erased<T> {
    public static void main(String...args) {
        private final int SIZE = 100
        // if(arg instanceof T) {}
        /*
         * 错误: instanceof 的泛型类型不合法
         *     if(arg instanceof T) {}
         *                       ^
        */

        // T var = new T();
        /*
         * 错误: 意外的类型
         * T var = new T();
         *             ^
        */

        // T[] array1 = new T[SIZE];
        /*
         * 错误: 创建泛型数组
         * T[] array1 = new T[SIZE];
         *              ^
        */

        T[] array2 = (T[])new Object[SIZE];
        //  警告: [unchecked] 未经检查的转换
    }
}
```

+ 但是可以通过传入 `Class` 对象使用 `isInstanceof()` 动态判断
+ [ClassTypeCapture](ClassTypeCapture.java)

```java
public class ClassTypeCapture<T> {
    private Class<T> kind;
    public ClassTypeCapture(Class<T> kind) {
        this.kind = kind;
    }
    public boolean f(Object arg) {
        return kind.isInstance(arg);
    }
}
```



### 15.8.1 创建类型实例

+ `C++` 可以直接 `new T()`
    + [InstantiateGenericType.cpp](InstantiateGenericType.cpp)
+ `Java` 不行
    + 擦除
    + `C++` 在编译器检查是否具有默认构造函数， `Java` 没有
    + 工厂方法（`newInstance()`）
    + [InstantiateGenericType.java](InstantiateGenericType.java)
```java
class ClassAsFactory<T> {
    T x;
    public ClassAsFactory(Class<T> kind) {
        try {
            x = kind.newInstance();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Employee {}

public class InstantiateGenericType {
    public static void main(String[] args) {
        ClassAsFactory<Employee> fe =
            new ClassAsFactory<Employee>(Employee.class);
        System.out.println("ClassAsFactory<Employee> succeeded");
        try {
            // 失败,因为 Integer 没有默认构造函数
            // 编译通过,因为这个不在编译期检查
            ClassAsFactory<Integer> fi =
                new ClassAsFactory<Integer>(Integer.class);
        } catch(Exception e) {
            System.out.println("ClassAsFactory<Integer> failed");
        }
    }
}
/* Output:
ClassAsFactory<Employee> succeeded
ClassAsFactory<Integer> failed
*/
```

+ 想办法在编译器就接受检查，**显式工厂设计方法**
    + 要求实现了指定工厂接口的类才能创建对象
    + 获得了编译器检查
+ [FactoryConstraint](FactoryConstraint)

```java
// 示例代码
interface FactoryI<T> {
    T create();
}

class Foo2<T> {
    private T x;
    // 限制 F 为实现了 FactoryI 接口的类
    public <F extends FactoryI<T>> Foo2(F factory) {
        x = factory.create();
    }
}
```

+ **模板方法设计模式**
+ [CreatorGeneric.java](CreatorGeneric.java)

```java
abstract class GenericWithCreate<T> {
    final T element;
    GenericWithCreate() { element = create(); }
    abstract T create();
}

class Creator extends GenericWithCreate<X> {
    X create() { return new X(); }
    void f() {
        System.out.println(element.getClass().getSimpleName());
    }
}
```

+ 调用含有参数的构造器
+ [TestConstructor.java](TestConstructor.java)
    + 通过反射可以调用 `private` 构造函数

```java
/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.lang.reflect.Constructor;
class ZeroOrOne {
    // One
    public ZeroOrOne(int One) {
        System.out.println("one arg!");
    }
    // Zero
    public ZeroOrOne() {
        System.out.println("zero args!");
    }
}

public class TestConstructor {
    public static void main(String[] args)
        throws Exception {
        Class cl = ZeroOrOne.class;
        // Class.newInstance()
        ZeroOrOne zoo = (ZeroOrOne)cl.newInstance();

        // Constructor.newInstance()
        // zero args
        zoo = (ZeroOrOne)cl
                .getDeclaredConstructor()
                .newInstance();
        // one arg
        zoo = (ZeroOrOne)cl
                .getDeclaredConstructor(
                    new Class[]{int.class}
                ).newInstance(
                    new Object[]{5}
                );
    }
}
/* Output:
zero args!
zero args!
one arg!
*/
```



### 15.8.2 泛型数组

+ 使用 `ArrayList`

```java
// 示例代码
import java.util.ArrayList;
import java.util.List;

public class ListOfGenerics<T> {
    private List<T> array = new ArrayList<T>();
    public void add(T item) { array.add(item); }
    public T get(int index) { return array.get(index); }
}
```

+ 创建泛型数组的方法
    + 牢记：**泛型的具体类型只存在于编译期**
    + 不能通过 `Object[]` 转型
    + 只能通过创建被擦除类型的数组在转型
+ [ArrayOfGeneric](ArrayOfGeneric.java)

```java
// 示例代码
class Generic<T> {}
public class ArrayOfGeneric {
    static final int SIZE = 100;
    static Generic<Integer>[] gia;
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // Compiles; produces ClassCastException:
        //! gia = (Generic<Integer>[])new Object[SIZE];
        // Runtime type is the raw (erased) type:
        gia = (Generic<Integer>[])new Generic[SIZE];
        //! gia = new Generic<Integer>[SIZE];
        // 错误: 创建泛型数组
        System.out.println(gia.getClass().getSimpleName());
        gia[0] = new Generic<Integer>();
        //! gia[1] = new Object(); // Compile-time error
        // Discovers type mismatch at compile time:
        //! gia[2] = new Generic<Double>();
    }
}
/* Output:
Generic[]
*/
```

+ 理解擦除
    + `Integer[] ia = gai.rep();` 会报错的原因在于，运行期间，`array` 被擦除为 `Object[]`

```java
// 示例代码
public class GenericArray<T> {
    private T[] array;
    @SuppressWarnings("unchecked")
    public GenericArray(int sz) {
        array = (T[])new Object[sz];
    }
    public void put(int index, T item) {
        array[index] = item;
    }
    public T get(int index) { return array[index]; }
    // Method that exposes the underlying representation:
    public T[] rep() { return array; }
    public static void main(String[] args) {
        GenericArray<Integer> gai =
            new GenericArray<Integer>(10);
        // This causes a ClassCastException:
        //! Integer[] ia = gai.rep();
        // This is OK:
        Object[] oa = gai.rep();
    }
}
```

+ 在集合内部使用 `Object[]` ，需要使用到具体类型的时候再将其进行转型
    + 如果内部存为 `T[]`
        + 在编译期结束后数组的实际类型也会丢失
        + 这样可能在编译期错过一些错误检查
    + 若存为 `Object[]`
        + 我们则不大容易忘记数组的运行时类型为 `Object[]`

```java
// 示例代码
public class GenericArray2<T> {
    private Object[] array;
    public GenericArray2(int sz) {
        array = new Object[sz];
    }
    public void put(int index, T item) {
        array[index] = item;
    }
    @SuppressWarnings("unchecked")
    public T get(int index) { return (T)array[index]; }
    @SuppressWarnings("unchecked")
    public T[] rep() {
        return (T[])array; // Warning: unchecked cast
    }
    public static void main(String[] args) {
        GenericArray2<Integer> gai =
            new GenericArray2<Integer>(10);
        for(int i = 0; i < 10; i ++)
            gai.put(i, i);
        for(int i = 0; i < 10; i ++)
            System.out.print(gai.get(i) + " ");
        System.out.println();
        try {
            Integer[] ia = gai.rep();
        } catch(Exception e) { System.out.println(e); }
    }
}
/* Output: (Sample)
0 1 2 3 4 5 6 7 8 9
java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Ljava.lang.Integer;
*/
```

+ 如何使得运行时类型为确切的 `T[]`
    + 在构造函数中传入具体类型 `Class<T>`，使其在擦除中恢复（**创建时就是具体的类型**）
    + `public` 的话就可以在外面直接 `gai.array =  new Integer[10];`

```java
//: generics/GenericArrayWithTypeToken.java
import java.lang.reflect.*;

public class GenericArrayWithTypeToken<T> {
    private T[] array;
    @SuppressWarnings("unchecked")
    public GenericArrayWithTypeToken(Class<T> type, int sz) {
        array = (T[])Array.newInstance(type, sz);
    }
    public void put(int index, T item) {
        array[index] = item;
    }
    public T get(int index) { return array[index]; }
    // Expose the underlying representation:
    public T[] rep() { return array; }
    public static void main(String[] args) {
        GenericArrayWithTypeToken<Integer> gai =
            new GenericArrayWithTypeToken<Integer>(
                Integer.class, 10);
        // This now works:
        Integer[] ia = gai.rep();
    }
}
```



## 15.9 边界

+ `Java` 重用了 `extends` 关键字
    + `<T extends ClassA>`
    + 多边界
        +  `<T extends ClassA & InterfaceB>`
        +  `<T extends ClassA & InterfaceB & interfaceC>`
        + **要求类在接口的前面**
+ 继承要求子类的边界在父类之中

```java
interface HasColor { java.awt.Color getColor(); }
class HoldItem<T> {}
class Dimension { public int x, y, z; }
class Colored2<T extends HasColor> extends HoldItem<T> {}
class ColoredDimension2<T extends Dimension & HasColor>
    extends Colored2<T> {}
// class ColoredDimension3<T extends Dimension> extends Colored2<T> {}
/*
错误: 类型参数T#1不在类型变量T#2的范围内
extends Colored2<T> {
                 ^
  其中, T#1,T#2是类型变量:
    T#1扩展已在类 ColoredDimension3中声明的Dimension
    T#2扩展已在类 Colored3中声明的HasColor
1 个错误
*/
```

+ 可以是子类

```java
interface HasColor { java.awt.Color getColor(); }
class HoldItem<T> {}
class Dimension { public int x, y, z; }
class FourDimension extends Dimension { public int l;}
class Colored2<T extends HasColor> extends HoldItem<T> {}
class ColoredDimension2<T extends Dimension & HasColor>
    extends Colored2<T> {}

// 注意 ColoredDimension4
class ColoredDimension4<T extends FourDimension & HasColor>
    extends ColoredDimension2<T> {}
```



## 15.10 通配符

+ 对于数组
    + 注意这里并不是向上转型
        + `Friut[] <- Apple[]` 而不是 `Fruit <- Apple`
+ 但是对于泛型容器而言就是不正确的

```java
class Fruit {}
class Apple extends Fruit {}
public class Test {
    public static void main(String...args) {
        Fruit[] f = new Apple[10]; // OK
        // 运行时刻类型就是具体的 Apple
        // List<Fruit> list = new ArrayList<Apple>();
        // 错误: 不兼容的类型: ArrayList<Apple>无法转换为List<Fruit>
    }
}
```

+ 和数组不同，泛型没有内建的协变类型
    + 这是因为数组在语言中是完全定义的，因此可以内建编译时和运行时的检查
    + 但是在使用泛型时，编译器和运行时系统都不知道你想用类型做什么，以及采用什么样的规则
+ **通配符** 允许了在两个类型之间建立某种类型的向上转型关系
+ 不过似乎有点极端，连声明的类型都不能加入了

```java
List<? extends Fruit> list = new ArrayList<Apple>();
// ??? CE
// list.add(new Apple());
// list.add(new Fruit());
// list.add(new Object());
list.add(null); // 没用

List<? extends Fruit> flist =
    Arrays.asList(new Apple());
Apple a = (Apple)flist.get(0);  // No warning
flist.contains(new Apple());    // Argument is 'Object'
flist.indexOf(new Apple());     // Argument is 'Object'
```



### 15.10.1 编译器有多聪明

+ 查看源码发现，`ArrayList` 中允许调用的方法，**参数中都不含有泛型占位符**
+ 再例如不能被调用的方法，参数中都含有泛型占位符
    + 例如 `add(E e)` ，此时 `E = <? extends Fruit>`
    + 编译器不能了解是具体的哪一种 `Fruit`，因此拒绝所有的 `Fruit` 参数
    + 转型为 `Fruit` 也不行，这被编译器直接拒绝
+ 以下示例代码便是一个说明

```java
public class Holder<T> {
    private T value;
    public Holder() {}
    public Holder(T val) { value = val; }
    public void set(T val) { value = val; }
    public T get() { return value; }
    public boolean equals(Object obj) {
        return value.equals(obj);
    }
    public static void main(String[] args) {
        Holder<Apple> Apple = new Holder<Apple>(new Apple());
        Apple d = Apple.get();
        Apple.set(d);
        // Holder<Fruit> Fruit = Apple; // Cannot upcast
        Holder<? extends Fruit> fruit = Apple; // OK
        // Holder<Apple> fruit2 = fruit; // 不能转换
        Fruit p = fruit.get();
        d = (Apple)fruit.get(); // Returns 'Object'
        try {
            Orange c = (Orange)fruit.get(); // No warning(返回Fruit)
        } catch(Exception e) { System.out.println(e); }
        // fruit.set(new Apple()); // Cannot call set()
        // fruit.set(new Fruit()); // Cannot call set()
        System.out.println(fruit.equals(d)); // OK
    }
}
/* Output: (Sample)
java.lang.ClassCastException: Apple cannot be cast to Orange
true
*/
```

+ `Holder<Apple>` 不能向上转型为 `Holder<Fruit>`
+ `Holder<Apple>` 可以向上转型为 `Holder<? extends Fruit>`



### 15.10.2 逆变

+ **超类型通配符**
    + `List<? super MyClass>`
        + 可以往 `List`  添加 `MyClass` 或者 `MyClass` 的子类
    + `<? super T>`
    + `ERROR`：`<T super MyClass>`
+ 可以往其中添加子类

```java
import java.util.List;
import java.util.ArrayList;

class Fruit {}
class Apple extends Fruit {}
class RedApple extends Apple {}

public class Fruits2 {
    public static void main(String...args) {
        test(new ArrayList<Apple>());
    }

    public static void test(List<? super Apple> list) {
        list.add(new RedApple());
        list.add(new Apple());
        // list.add(new Fruit());
        return;
    }
}
```

+ 对比一下泛型和超类型参数的区别

```java
// 示例代码
import java.util.*;

public class GenericWriting {
    static List<Apple> apples = new ArrayList<Apple>();
    static List<Fruit> fruit = new ArrayList<Fruit>();

    // 泛型
    static <T> void writeExact(List<T> list, T item) {
        list.add(item);
    }
    static void f1() {
        writeExact(apples, new Apple());
        // writeExact(apples, new Fruit()); // ERROR
        /*
         * 需要: List<T>,T
         * 找到: List<Apple>,Fruit
         * 原因: 推论变量T具有不兼容的限制范围
         *   等式约束条件: Apple
         *   下限: Fruit
         */
        writeExact(fruit, new Apple()); // Java8 OK
        writeExact(fruit, new Fruit());
    }

    // 超类型通配符
    static <T> void writeWithWildcard(
        List<? super T> list, T item) {
        list.add(item);
    }
    static void f2() {
        writeWithWildcard(apples, new Apple());
        // writeWithWildcard(apples, new Fruit()); // ERROR
        /*
         * 需要: List<? super T>,T
         * 找到: List<Apple>,Fruit
         * 原因: 推断类型不符合上限
         *   推断: Fruit
         *   上限: Apple,Object
         */
        writeWithWildcard(fruit, new Apple());
        writeWithWildcard(fruit, new Fruit());
    }
    public static void main(String[] args) { f1(); f2(); }
}
```



### 15.10.3 无界通配符

+ `<?>`：好像等价于使用原生类型
    + `List<?>  ->  List`

```java
// 示例代码
import java.util.*;
public class UnboundedWildcards1 {
    static List list1;
    static List<?> list2;
    static List<? extends Object> list3;
    static void assign1(List list) {
        list1 = list;
        list2 = list;
        // list3 = list; // Warning: unchecked conversion
        // Found: List, Required: List<? extends Object>
    }
    static void assign2(List<?> list) {
        list1 = list;
        list2 = list;
        list3 = list;
    }
    static void assign3(List<? extends Object> list) {
        list1 = list;
        list2 = list;
        list3 = list;
    }
    public static void main(String[] args) {
        assign1(new ArrayList());
        assign2(new ArrayList());
        // assign3(new ArrayList()); // Warning:
        // Unchecked conversion. Found: ArrayList
        // Required: List<? extends Object>
        assign1(new ArrayList<String>());
        assign2(new ArrayList<String>());
        assign3(new ArrayList<String>());
        // Both forms are acceptable as List<?>:
        List<?> wildList = new ArrayList();
        assign1(wildList);
        assign2(wildList);
        assign3(wildList);
        wildList = new ArrayList<String>();
        assign1(wildList);
        assign2(wildList);
        assign3(wildList);
    }
}
```

+ 无界通配符的一种重要应用
    + 处理多个泛型参数的时候，有时允许其中一个参数可以是任何类型，
    + 同时为其他参数确定某种特定类型的这种能力显得尤为重要
+ 但是如果全都是无界通配符时，编译器不好将其与原生类型区别开
    + `Map<?,?>  -  Map`

```java
// 示例代码
import java.util.*;
public class UnboundedWildcards2 {
    static Map map1;
    static Map<?,?> map2;
    static Map<String,?> map3;
    static void assign1(Map map) { map1 = map; }
    static void assign2(Map<?,?> map) { map2 = map; }
    static void assign3(Map<String,?> map) { map3 = map; }
    public static void main(String[] args) {
        assign1(new HashMap());
        assign2(new HashMap());
        // assign3(new HashMap()); // Warning:
        // Unchecked conversion. Found: HashMap
        // Required: Map<String,?>
        assign1(new HashMap<String,Integer>());
        assign2(new HashMap<String,Integer>());
        assign3(new HashMap<String,Integer>());
    }
}
```

+ 什么时候编译器会将 `List<?>` 和 `List` 区别开
+ [Wildcards](Wildcards.java)
+ 原生 `Holder` 持有任意类型的 `Object`，`Holder<?>` 持有某种具体类型的同构集合

```java
// Raw argument:
static void rawArgs(Holder holder, Object arg) {
    // holder.set(arg); // Warning:
    //     Unchecked call to set(T) as a
    //     member of the raw type Holder
    // holder.set(new Wildcards()); // Same warning

    // Can't do this; don't have any 'T':
    // T t = holder.get();

    // OK, but type information has been lost:
    Object obj = holder.get();
}
// Similar to rawArgs(), but errors instead of warnings:
static void unboundedArg(Holder<?> holder, Object arg) {
    // holder.set(arg); // Error:
    //     set(capture of ?) in Holder<capture of ?>
    //     cannot be applied to (Object)
    // holder.set(new Wildcards()); // Same error

    // Can't do this; don't have any 'T':
    // T t = holder.get();

    // OK, but type information has been lost:
    Object obj = holder.get();
}
```



### 15.10.4 捕获转换

+ 如果向一个使用 `<?>` 的方法传递原生类型，
+ 那么对编译器来说，可能会推断出实际的类型参数，
+ 使得这个方法可以回转并调用另一个使用这个确切类型的方法
+ [CaptureConversion](CaptureConversion.java)
    + `line 13,14`

```java
// 示例代码
public class CaptureConversion {
    static <T> void f1(Holder<T> holder) {
        T t = holder.get();
        System.out.println(t.getClass().getSimpleName());
    }
    static void f2(Holder<?> holder) {
        f1(holder); // Call with captured type
    }
    // @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Holder raw = new Holder<Integer>(1);
        f1(raw); // Produces warnings
        f2(raw); // No warnings
        Holder rawBasic = new Holder();
        rawBasic.set(new Object()); // Warning
        f2(rawBasic); // No warnings
        // Upcast to Holder<?>, still figures it out:
        Holder<?> wildcarded = new Holder<Double>(1.0);
        f2(wildcarded);
    }
}
/* Output:
Integer
Integer
Object
Double
*/
```



## [part2](./README-part2.md)