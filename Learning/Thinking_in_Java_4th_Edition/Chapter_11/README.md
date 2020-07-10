# Chapter_11 持有对象

> [总目录](../README.md)

---

[TOC]

---

+ **程序可能需要在任意时刻创建任意数量的对象**
+ 数组
+ **集合类** / **容器**
    + `List`、`Set`、`Map`、`Queue`



## 11.1 泛型和类型安全的容器

+ 不使用泛型，默认 `Object`
+ 以下代码能够通过编译，但是运行时会抛出异常

```java
class Man{
    void func(){}
}

class Woman{}

public class Test{
    @SuppressWarnings("unchecked")
    // 不加注解
    // 注: Test.java使用了未经检查或不安全的操作
    // 注: 有关详细信息, 请使用 -Xlint:unchecked 重新编译
    public static void main(String...args){
        java.util.ArrayList a = new java.util.ArrayList();
        a.add(new Man());
        a.add(new Woman());
        for(Object man : a){
            // Exception in thread "main" java.lang.ClassCastException:
            // Woman cannot be cast to Man
            ((Man)man).func();
        }
    }
}
```

+ 使用泛型
    + 只能有一个参数
        + 如果需求上需要多个参数的话，他们应该具备同类特征
        + 可以让他们继承相同的基类 / 实现相同的接口
        + 使用基类或者接口作为泛型参数

```java
class Man{}
class Woman{}

public class Test{
    public static void main(String...args){
        test1();
        test2();
    }

    public static void test1(){
        java.util.ArrayList<Man> a = new java.util.ArrayList<>();
        a.add(new Man());
        // a.add(new Woman());
        // 方法 Collection.add(Man)不适用
        //   (参数不匹配; Woman无法转换为Man)
        // 方法 List.add(Man)不适用
        //   (参数不匹配; Woman无法转换为Man)
        // 方法 List.add(int,Man)不适用
        //   (实际参数列表和形式参数列表长度不同)
        // 方法 AbstractCollection.add(Man)不适用
        //   (参数不匹配; Woman无法转换为Man)
        // 方法 AbstractList.add(Man)不适用
        //   (参数不匹配; Woman无法转换为Man)
        // 方法 AbstractList.add(int,Man)不适用
        //   (实际参数列表和形式参数列表长度不同)
        // 方法 ArrayList.add(Man)不适用
        //   (参数不匹配; Woman无法转换为Man)
        // 方法 ArrayList.add(int,Man)不适用
        //   (实际参数列表和形式参数列表长度不同)
    }

    public static void test2(){
        // java.util.ArrayList<Man,Woman> a = new java.util.ArrayList<>();
        // 错误: 类型变量数目错误; 需要1
    }
}
```

+ 继承自相同的基类
    + 通过编译

```java
class Human{}
class Man extends Human{}
class Woman extends Human{}
public class Test{
    public static void main(String...args){
        java.util.ArrayList<Human> a = new java.util.ArrayList<>();
        a.add(new Man());
        a.add(new Woman());
        a.add(new Human());
    }
}
```



## 11.2 基本概念

+ `Collection`
    + `List`：按照插入顺序保存元素
    + `Set`：不能有重复元素
        + 不会被重复加入
    + `Queue`：队列
+ `Map`：键值对



## 11.3 添加一组元素

+ [TestCollectionAddition.java](TestCollectionAddition.java)

```java
import java.util.*;
public class TestCollectionAddition{
    public static void print(Collection<String> c){
        for(String s : c)
            System.out.println(s);
    }

    public static void main(String...args){
       Collection<String> collection = new ArrayList<>();
        // 添加方式 1
        collection.add("a");
        // 添加方式 2
        Collections.addAll(collection, "b", "c");
        // 添加方式 3
        String[] strings = {"d", "e"};
        collection.addAll(Arrays.asList(strings));
        // 添加方式 4
        collection.addAll(collection);
        // 打印
        print(collection);
    }
}
```

```output
a
b
c
d
e
a
b
c
d
e
```

+ `Collection`  的构造器可以接受另一个 `Collection` 作为参数
    + `Arrays.asList()`
    + 可以直接使用 `Arrays.asList()` ，但是这里底层时数组，不能修改数组大小
+ 但是 `addAll()` 方法运行更快
+ [TestCollection.java](TestCollection.java)

```java
import java.util.*;
public class TestCollection{
    public static void print(Collection<String> c){
        for(String s : c)
            System.out.println(c + ":" + s);
    }

    public static void main(String...args){
        String[] strings = {"d", "e"};
        // 添加方式 1
        Collection<String> collection1 =
            new ArrayList<>(Arrays.asList(strings));
        // 添加方式 2
        Collection<String> collection2 = new ArrayList<>();
        collection2.addAll(Arrays.asList(strings));
        // 构造方式 3
        Collection<String> collection3 = Arrays.asList(strings);
        collection1.add("f");
        collection2.add("f");
        // collection3.add("f");
        // Exception in thread "main" java.lang.UnsupportedOperationException
        // 方式 3 的底层是数组,不允许修改大小
        print(collection1);
        print(collection2);
        print(collection3);
    }
}
```

+ 一些类型转化的问题 `Arrays.asList()`
+ [TestAsList.java](TestAsList.java)

```java
class Warrior {}
class Knight extends Warrior{}
class Swordsman extends Warrior{}
class Swordmaster extends Swordsman{}
class Myrmidon extends Swordsman{}

public class TestAsList{
    public static void main(String...args){
        String[] strings = {"d", "e"};
        // 添加方式 1
        Collection<String> collection1 =
            new ArrayList<>(Arrays.asList(strings));
        // 添加方式 2
        Collection<String> collection2 = new ArrayList<>();
        collection2.addAll(Arrays.asList(strings));
        // 构造方式 3
        Collection<String> collection3 = Arrays.asList(strings);
        collection1.add("f");
        collection2.add("f");
        // collection3.add("f");
        // Exception in thread "main" java.lang.UnsupportedOperationException
        // 方式 3 的底层是数组,不允许修改大小
        print(collection1);
        print(collection2);
        print(collection3);
    }
}
```

+ 关于 `Array.asList()`
    + `1.8` 改进了
    + `java.util.Arrays$ArrayList`
+ [AsListInference.java](AsListInference.java)

```java
// 示例代码
import java.util.*;

class Snow {}
class Powder extends Snow {}
class Light extends Powder {}
class Heavy extends Powder {}
class Crusty extends Snow {}
class Slush extends Snow {}

public class AsListInference {
    public static void main(String[] args) {
        // case 1: OK
        List<Snow> snow1 = Arrays.asList(
            new Crusty(), new Slush(), new Powder());

        // case 2: OK
        // 编译器能够识别出来(1.8)
        // 书上 1.6 好像不太行(没有测试)
        List<Snow> snow2 = Arrays.asList(
            new Light(), new Heavy());

        // case 3: OK
        // Collections 都 OK
        List<Snow> snow3 = new ArrayList<Snow>();
        Collections.addAll(snow3, new Light(), new Heavy());

        // case 4: OK
        // 加上提示信息
        List<Snow> snow4 = Arrays.<Snow>asList(
             new Light(), new Heavy());
    }
}
```



## 11.4 容器的打印

+ [TestPrintMap.java](TestPrintMap.java)

```java
import java.util.*;
public class TestPrintMap{
    public static void main(String...args){
        HashMap<String, String> map = new HashMap<>();
        map.put("FE6", "The Binding Blade");
        map.put("FE7", "The Blazing Sword");
        map.put("FE8", "The Sacred Stone");
        System.out.println(map);
        HashSet<String> set = new HashSet<>();
        set.add("The Binding Blade");
        set.add("The Blazing Sword");
        set.add("The Sacred Stone");
        System.out.println(set);
    }
}
```

```output
{FE6=The Binding Blade, FE8=The Sacred Stone, FE7=The Blazing Sword}
[The Binding Blade, The Sacred Stone, The Blazing Sword]
```

+ `Set`
    + `HashSet`：快
    + `TreeSet`：有序
    + `LinkedHashSet`：按照插入顺序排列



## 11.5 List

+ `List` 接口
    + `ArrayList`
        + 随机访问快，中间插删慢
    + `LinkedList`
        + 优化的顺序访问，随机访问慢，插删快
+ `ArrayList`
    + `remove()`
        + 使用 `equals()` 判断
        + 只移除第一个匹配上的
    + `removeAll()`
        + `removeAll()` 参数为 `Collection`，移除所有匹配上的元素（相同的元素全被移除）
    + `retainALL()`
        + 求交集（**in-place**）
    + `add()`、`addAll()`
    + `get()`
    + `indexOf()`
    + `contains()`、`containsAll()`
    + `set()`
        + 替换某个位置的元素
    + `subList()`
    + `isEmpty()`
    + `clear()`
    + `toArray()`



## 11.6 迭代器

+ `Iterator`
    + `iterator()` 、`hasNext()`、`next()`、`remove()`
+ [TestIterator.java](TestIterator.java)

```java
import java.util.*;

class FE{
    public String name;
    public FE(String name){ this.name = name; }
    @Override
    public String toString(){
        return this.name;
    }
    public boolean equals(String otherName){
        return this.name.equals(otherName);
    }
}

public class TestIterator{
    public static void display(Iterator<FE> iterator){
        while(iterator.hasNext()){ // hasNext
            System.out.println(iterator.next()); // next
        }
    }

    public static void main(String...args){
        ArrayList<FE> set = new ArrayList<>();
        set.add(new FE("FE1"));
        set.add(new FE("FE2"));
        set.add(new FE("FE3"));
        set.add(new FE("FE4"));
        set.add(new FE("FE5"));
        set.add(new FE("FE6"));
        System.out.println(set);
        // 1
        Iterator<FE> iterator = set.iterator(); // iterator
        display(iterator);
        // 2
        iterator = set.iterator();
        // iterator.remove();
        // java.lang.IllegalStateException
        while(iterator.hasNext()){
            FE fe = iterator.next();
            if(fe.equals("FE3")){
                // remove 的元素是上一个 next 返回的元素
                iterator.remove(); // remove
            }
        }
        System.out.println(set);
    }
}
```

```output
[FE1, FE2, FE3, FE4, FE5, FE6]
FE1
FE2
FE3
FE4
FE5
FE6
[FE1, FE2, FE4, FE5, FE6]
```

+ **迭代器统一了对容器的访问模式**
    + 上述 `display` 函数中对于任意容器都行得通



### 11.6.1 ListIterator

+ `ListIterator extends Iterator`
    + `iterator()` 、`hasNext()`、`next()`、`remove()`、`hasPrevious()`、`previous()`、`set()`
    + 应用于 `List` 的迭代
    + 可以双向访问
    + 可以设置起始位置
    + `set()` 可以替代最后访问过的元素
+ [TestListIterator.java](TestListIterator.java)

```java
import java.util.*;

class FE{
    public String name;
    public FE(String name){ this.name = name; }
    @Override
    public String toString(){
        return this.name;
    }
    public boolean equals(String otherName){
        return this.name.equals(otherName);
    }
}

public class TestListIterator{

    public static void main(String...args){
        ArrayList<FE> set = new ArrayList<>();
        set.add(new FE("FE1"));
        set.add(new FE("FE2"));
        set.add(new FE("FE3"));
        set.add(new FE("FE4"));
        set.add(new FE("FE5"));
        set.add(new FE("FE6"));
        System.out.println(set);
        // 1
        System.out.println("Iterator!");
        ListIterator<FE> iterator = set.listIterator(); // iterator
        // ListIterator<FE> iterator = set.listIterator(0); // 等价
        while(iterator.hasNext()){ // hasNext
            System.out.print(iterator.next() + " "); // next
        }
        System.out.println();
        // 2
        iterator = set.listIterator();
        while(iterator.hasNext()){
            FE fe = iterator.next();
            if(fe.equals("FE3")){
                // 移除最后一个访问的元素
                iterator.remove(); // remove
            }
        }
        System.out.println(set);
        System.out.println("ListIterator!");
        // 3
        while(iterator.hasPrevious()){ // hasPrevious
            System.out.print(iterator.previous() + " "); // previous
        }
        System.out.println();
        // 4
        iterator = set.listIterator(2); // 设置起始位置为 2
        int count = 0; // 证明设置为 2 生效
        while(iterator.hasNext()){
            FE fe = iterator.next();
            if(fe.equals(set.get(2).name)){
                count += 100;
            }
            ++ count;
            if(fe.equals("FE6")){
                // 设置最后一个访问的元素
                System.out.println("count:" + count);
                iterator.set(new FE("FE66")); // set
                break;
            }
        }
        System.out.println(set);
        // 5
        while(iterator.hasPrevious()){
            FE fe = iterator.previous();
            if(fe.equals("FE2")){
                // 移除最后一个访问的元素
                iterator.remove(); // remove
            }
            if(fe.equals("FE1")){
                // 设置最后一个访问的元素
                iterator.set(new FE("FE11")); // set
                break;
            }
        }
        System.out.println(set);
    }
}
```

```output
[FE1, FE2, FE3, FE4, FE5, FE6]
Iterator!
FE1 FE2 FE3 FE4 FE5 FE6
[FE1, FE2, FE4, FE5, FE6]
ListIterator!
FE6 FE5 FE4 FE2 FE1
count:103
[FE1, FE2, FE4, FE5, FE66]
[FE11, FE4, FE5, FE66]
```



## 11.7 LinkedList

+ 增删高效，随机访问性能较差
+ 除了基础的 `List` 接口
+ 增加了可以用作**栈**、**队列**、**双端队列**的方法
+ `getFirst()`、`element()`
    + 完全一样
        + 都是返回列表头（第一个元素）
        + 不移除头元素
        + 若列表为空，抛出异常 `NoSuchElementException`
    + `peek()` 的区别是若列表为空，返回 `null`
+ `removeFirst()`、`remove()`
    + 完全一致
        + 都是移除并返回列表头

        + 若列表为空，抛出异常 `NoSuchElementException`
    + `poll()` 的区别是若列表为空，返回 `null`
+ `addFirst()`
    + 将某个元素插入列表的头部
+ `add()`、`addLast()`
    + 完全一致，都是将某个元素插入列表的尾部
+ `removeLast()` 移除并返回列表的的最后一个元素



## 11.8 Stack

+ **栈** `LIFO`
+ `java.util.Stack` 是继承自 `Vector`
+ `LinkedList` 可以当作栈使用

```java
// 代理模式,只能使用以下方法
// 不使用继承,继承则汇集成所有 LinkedList 的方法

import java.util.LinkedList;
class Stack<T> {
    private LinkedList<T> stack = new LinkedList<T>();
    public void push(T v){ stack.addFirst(v); }
    public T peek(){ return stack.peek(); }
    public T pop(){ return stack.poll(); }
    public boolean empty(){ return stack.size() == 0; }
    public String toString(){ return stack.toString(); }
}
```



## 11.9 Set

+ 和 `Collection` 完全一致的接口
+ 不允许元素重复
+ 具体实现 `HashSet`、`TreeSet`



## 11.10 Map

+ `put()`、`get()`
+ `containsKey()`、`containsValue()`
+ `keySet()`



## 11.11 Queue

+ **队列** `FIFO`
+ `LinkedList` 实现了 `Queue` 接口

```java
Queue<String> queue = new LinkedList<>();
```

+ `add()`、`offer()`、`remove()`、`poll()`、`element`、`peek()`
+ `offer()`
    + 在允许的情况下将一个元素插入到队尾
    + `LinkedList`：`add()`



### 11.11.1 PriorityQueue

+ **优先队列**
+ `java.util.PriorityQueue`



## 11.12 Collection 和 Iterator

+ 容器的共性
    + 通过 `Iterator` 表现
        + 实现一个不是 `Collection` 的外部类时，比较方便
    + 都实现了 `Collection` 接口
        + 可以使用 `foreach` 结构
    + 二者糅合，实现了 `Collection` 则必须提供 `iterator()` 方法



## 11.13 Foreach 与 迭代器

+ `foreach` 语法能够用于所有的 `Collection` 对象
    + 只要实现了 `Iterable` 接口，便可以使用 `foreach` 语法
+ [TestIterable.java](TestIterable.java)

```java
import java.util.*;
public class TestIterable implements Iterable<Integer>{
    public int a, b, c;
    public TestIterable(int a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public Iterator<Integer> iterator(){
        return new Iterator<Integer>(){
            private int index = 0;
            @Override
            public boolean hasNext(){
                if(index == 3){
                    return false;
                }
                return true;
            }
            @Override
            public Integer next(){
                int ret = -1;
                if(index == 0){
                    ret = a;
                } else if(index == 1){
                    ret = b;
                } else if(index == 2){
                    ret = c;
                }
                ++index;
                return ret;
            }
        };
    }

    public static void main(String...args){
        for(Integer n : new TestIterable(1, 2, 3)){
            System.out.println(n);
        }
    }
}
```

```output
1
2
3
```

+ `foreach` 可以用于数组，但是数组不是一个 `Iterable`
+ [TestArray.java](TestArray.java)

```java
import java.util.*;
public class TestArray{
    public static <T> void testIterable(Iterable<T> it){
        System.out.println("OK!");
    }

    public static void main(String...args){
        String[] test = "I am a boy!".split(" ");
        for(String n : test){
            System.out.println(n);
        }
        // testIterable(test);
        // 错误: 无法将类 Test中的方法 testIterable应用到给定类型
        testIterable(Arrays.asList(test));
    }
}
```

```output
I
am
a
boy!
OK!
```



### 11.13.1 适配器方法惯用法

+ 使得实现 `Iterable` 的类既能正向迭代，又能反向迭代

```java
/**
 * @author banbao
 */

import java.util.*;

public class TestMultiIterable implements Iterable<Integer>{
    public int a, b, c;
    public TestMultiIterable(int a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public Iterator<Integer> iterator(){
        return new Iterator<Integer>(){
            private int index = 0;
            @Override
            public boolean hasNext(){
                if(index == 3){
                    return false;
                }
                return true;
            }
            @Override
            public Integer next(){
                int ret = -1;
                if(index == 0){
                    ret = a;
                } else if(index == 1){
                    ret = b;
                } else if(index == 2){
                    ret = c;
                }
                ++index;
                return ret;
            }
        };
    }
    // 另外返回一个 Iterable 实现反向 foreach
    public Iterable<Integer> reverse(){
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator(){
                return new Iterator<Integer>(){
                    private int index = 2;
                    @Override
                    public boolean hasNext(){
                        if(index == -1){
                            return false;
                        }
                        return true;
                    }
                    @Override
                    public Integer next(){
                        int ret = -1;
                        if(index == 0){
                            ret = a;
                        } else if(index == 1){
                            ret = b;
                        } else if(index == 2){
                            ret = c;
                        }
                        --index;
                        return ret;
                    }
                };
            }
        };
    }

    public static void main(String...args){
        TestMultiIterable test = new TestMultiIterable(1, 2, 3);
        for(Integer n : test){
            System.out.println(n);
        }
        for(Integer n : test.reverse()){
            System.out.println(n);
        }
    }
}
```

```output
1
2
3
3
2
1
```



## 11.14 总结

+ 过时的 `Stack`、`Vector`、`HashTable`

