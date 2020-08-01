# Chapter_16 数组

> [总目录](../README.md)

---

[TOC]

---

+ 数组
    + 尺寸不能改变
    + 可以使用整形索引值访问他们的元素



## 16.1 数组为什么特殊

+ 和其他容器不同的地方
    + **效率**、**类型**、**保存基本类型的能力**
+ 效率最高的存储和随机访问对象引用序列的方式
+ 编译期检查（数组可以指定类型）
+ 可以持有基本类型



## 16.2 数组是第一级对象

+ 唯一可以访问的字段或方法
    + `length`：**只读**，数组**能够容纳**元素的个数
+ 默认初始化：`0/null`



## 16.3 返回一个数组

+ `C/C++`：只能返回一个指针，难以控制数组的生命周期，容易造成内存泄漏

```java
public String[] returnArray() {
    String[] ret;
    // ...
    return ret;
}
```



## 16.4 多维数组

+ 方便的创建方式

```java
class SerialA {
    private static long serial = 0;
    public final long ID;
    public SerialA(){
        this.ID = ++serial;
    }
    @Override
    public String toString(){
        return "A_serialNumber_" + this.ID;
    }
}

```

```java
import java.util.Arrays;
public class MultiArray {
    public static void main(String...args) {
        // 方便的创建方式
        SerialA[][] a = {
            {new SerialA()},
            {new SerialA()},
        };
        System.out.println(Arrays.deepToString(a));
    }
}
```

+ **粗糙数组**
    + 多维数组中的每一维向量长度可以不相同

```java
SerialA[][] a = {
    {new SerialA()},
    {new SerialA(), new SerialA() },
    {new SerialA(), new SerialA(), new SerialA() },
};
```

```java
int[][] a = new int[10][10];
// int[][] b = new int[][10]; // ERROR
int[][] c = new int[10][];
int d[][] = new int[10][];
int[] e[] = new int[10][];

System.out.println(Arrays.deepToString(a));
// System.out.println(Arrays.deepToString(b));
System.out.println(Arrays.deepToString(c));
System.out.println(Arrays.deepToString(d));
System.out.println(Arrays.deepToString(e));
```

```output
[[0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]
[null, null, null, null, null, null, null, null, null, null]
[null, null, null, null, null, null, null, null, null, null]
[null, null, null, null, null, null, null, null, null, null]
```



## 16.5 数组与泛型

+ 通常数组于泛型不能很好的结合
+ 擦除会隐去参数类型信息，而数组要求知道所持有的确切类型，以强制保证类型安全

```java
ArrayList<Sample>[] a = new ArrayList<>()[10];
// 错误: 无法创建具有 '<>' 的数组
```

+ 但是可以参数化数组本身的类型
+ 注
    + 参数化方法比参数化类更好
        + 不需要为每个类型创建一个类
        + `static` 方法好用
    + [ParameterizedArrayType](ParameterizedArrayType.java)

```java
class ClassPara<T> {
    public T[] f(T[] arg) { return arg; }
}
class MethodPara {
    public static <T> T[] f(T[] arg) { return arg; }
}
public class ParameterizedArrayType {
    public static void main(String...args) {
        Integer[] int1 = {1,2,3};
        String[] str1 = {"a","b","c"};
        // class
        Integer[] int2 = new ClassPara<Integer>().f(int1);
        String[] str2 = new ClassPara<String>().f(str1);
        // method
        Integer[] int3 = MethodPara.f(int1);
        String[] str3 = MethodPara.f(str1);
    }
}
```

+ 可以定义泛型数组的引用
    + 不能 `new`
    + 但是可以通过转型实现

```java
ArrayList<String>[] ls;
ls = (ArrayList<String>[])new List[10]; // unchecked warning
ls[0] = new ArrayList<String>();

//! ls[0] = new ArrayList<Integer>();
// 错误: 不兼容的类型:
//       ArrayList<Integer>无法转换为ArrayList<String>

Object[] objs = (Object[])ls;
// 这个是OK的,注意之前提到的 ArrayList<String> 是 Object 的子类
// 但是 ArrayList<Sring> -> ArrayList<Object> 是不行的
```

+ [TestArrayAndGeneric](TestArrayAndGeneric.java)

```java
import java.util.Arrays;

class MakeArray<T> {
    private Object[] obj;
    public MakeArray(int size) {
        this.obj = new Object[size];
    }
    @SuppressWarnings("unchecked")
    public T[] getArray() {
        return (T[]) obj;
    }
    public void set(T o, int index)  {
        this.obj[index] = o;
    }
}

public class TestArrayAndGeneric {
    public static void main(String...args) {
        MakeArray<String> str = new MakeArray<String>(10);
        str.set("test", 0);
        // str.set(1, 0); // 错误: 不兼容的类型: int无法转换为String
        System.out.println(Arrays.deepToString(str.getArray()));
    }
}
/* Output
[null, null, null, null, null, null, null, null, null, null]
*/
```



## 16.6 创建测试数据

### 16.6.1 Arrays.fill()

```java
public static void fill(int[] a, int val) {
    for (int i = 0, len = a.length; i < len; i++)
        a[i] = val;
}

public static void fill(int[] a, int fromIndex, int toIndex, int val) {
    rangeCheck(a.length, fromIndex, toIndex);
    for (int i = fromIndex; i < toIndex; i++)
        a[i] = val;
}
```

```java
import java.util.Arrays;
public class TestArrays {
    public static void main(String...args) {
        int[] a = new int[5];
        System.out.println("~~~~~fill()~~~~~");
        Arrays.fill(a, 10086);
        System.out.println(Arrays.toString(a));
        Arrays.fill(a, 1, 2, 88);
        System.out.println(Arrays.toString(a));
    }
}
/* Output
[10086, 10086, 10086, 10086, 10086]
[10086, 88, 10086, 10086, 10086]
*/
```



### 16.6.2 数据生成器

+ 示例代码中的 `Generator`

```java
public interface Generator<T> { T next(); }
```



### 16.6.3 从 Generator 中创建数组

+ 示例代码
+ 注意泛型不能用于基本类型
+ 基本类型的实现需要通过其他方式

```java
public static boolean[] primitive(Boolean[] in) {
    boolean[] result = new boolean[in.length];
    for(int i = 0; i < in.length; i++)
        result[i] = in[i]; // Autounboxing
    return result;
}
```

## 16.7 Arrays 实用功能

+ [TestArrays](TestArrays.java)

### 16.7.1 复制数组

+ `System.arraycopy()`
+ **这个复制方法比 `for` 循环更快**
+ 有越界异常

```java
public static native void arraycopy(
    Object src,  int  srcPos, Object dest, int destPos, int length
);
```

+ 看看这个例子
+ 说明不是简单的复制，做了一些 `corner case` 的考虑

```java
int[] b = new int[10];
for(int i = 0;i < b.length; ++i) { b[i] = i + 1; }
System.out.println(Arrays.toString(b));
System.arraycopy(b, 2, b, 0, 4);
System.out.println(Arrays.toString(b));
for(int i = 0;i < b.length; ++i) { b[i] = i + 1; }
System.arraycopy(b, 0, b, 2, 4);
System.out.println(Arrays.toString(b));
```

```output
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
[3, 4, 5, 6, 5, 6, 7, 8, 9, 10]
[1, 2, 1, 2, 3, 4, 7, 8, 9, 10]
```



## 16.7.2 数组的比较

+ `equals(int[] a, int[] a2);`
+ 比较数组长度，每个元素使用 `equals`

```java
public static boolean equals(int[] a, int[] a2) {
    if (a==a2)
        return true;
    if (a==null || a2==null)
        return false;

    int length = a.length;
    if (a2.length != length)
        return false;

    for (int i=0; i<length; i++)
        if (a[i] != a2[i])
            return false;

    return true;
}
```

```java
public static boolean equals(Object[] a, Object[] a2) {
    if (a==a2)
        return true;
    if (a==null || a2==null)
        return false;

    int length = a.length;
    if (a2.length != length)
        return false;

    for (int i=0; i<length; i++) {
        Object o1 = a[i];
        Object o2 = a2[i];
        if (!(o1==null ? o2==null : o1.equals(o2)))
            return false;
    }

    return true;
}
```



### 16.7.3 数组元素的比较

+ 实现元素的比较
    + 实现 `java.lang.Comparable` 接口
    + 传入比较器 ` Comparator` 参数

```java
package java.lang;
import java.util.*;
public interface Comparable<T> {
    public int compareTo(T o);
}
```

```java
/* Arrays.sort() */

// 内置 sort
// 1. 基本类型
// 1.1 整个数组排序
public static void sort(double[] a) {
    DualPivotQuicksort.sort(a, 0, a.length - 1, null, 0, 0);
}

// 1.2 局部排序
public static void sort(double[] a, int fromIndex, int toIndex) {
    rangeCheck(a.length, fromIndex, toIndex);
    DualPivotQuicksort.sort(a, fromIndex, toIndex - 1, null, 0, 0);
}

// 2. 自定义类(泛型,比较器)
// 2.1 整个数组排序
public static <T> void sort(T[] a, Comparator<? super T> c) {
    if (c == null) {
        sort(a);
    } else {
        if (LegacyMergeSort.userRequested)
            legacyMergeSort(a, c);
        else
            TimSort.sort(a, 0, a.length, c, null, 0, 0);
    }
}
// 2.2 局部排序
public static <T> void sort(
    T[] a, int fromIndex, int toIndex,Comparator<? super T> c) {
    if (c == null) {
        sort(a, fromIndex, toIndex);
    } else {
        rangeCheck(a.length, fromIndex, toIndex);
        if (LegacyMergeSort.userRequested)
            legacyMergeSort(a, fromIndex, toIndex, c);
        else
            TimSort.sort(a, fromIndex, toIndex, c, null, 0, 0);
    }
}

// 3. Object(Comparable 接口)
// 3.1 局部排序
public static void sort(Object[] a, int fromIndex, int toIndex) {
    rangeCheck(a.length, fromIndex, toIndex);
    if (LegacyMergeSort.userRequested)
        legacyMergeSort(a, fromIndex, toIndex);
    else
        ComparableTimSort.sort(a, fromIndex, toIndex, null, 0, 0);
}
// 3.2 整体排序
public static void sort(Object[] a) {
    if (LegacyMergeSort.userRequested)
        legacyMergeSort(a);
    else
        ComparableTimSort.sort(a, 0, a.length, null, 0, 0);
}
```



### 16.7.4 数组排序

+ 上述原代码



### 16.7.5 在已排序的数组中查找

+ `Arrays.BinarySearch()`
    + 和 `sort()` 的方法类似
+ 整个数组，局部查询
+ 注意要已经排序
+ 找到则返回元素位置，否则返回一个负数
    + `-(插入点)-1`
+ 若有元素重复，不保证找到的是具体的哪一个



## 16.8 总结

+ [TestArrayStoreException](TestArrayStoreException.java)


```java
// 运行时异常
class Animal{}
class Dog extends Animal{}
class Cat extends Animal{}
public class TestArrayStoreException {
    public static void main(String...args) {
        Animal[] d = new Dog[10];
        // d[0] = new Animal(); // ArrayStoreException
        d[0] = new Dog();
        // d[0] = new Cat(); // ArrayStoreException
    }
}
```