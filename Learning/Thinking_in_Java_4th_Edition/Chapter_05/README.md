# Chapter_05 初始化与清理

> [总目录](../README.md)

---

[TOC]

---



## 5.1 用构造器确保初始化

+ `constructor`
+ **默认构造器** / **无参构造器**
    + 仅在没有构造函数的时候有



## 5.2 方法重载

### 5.2.1 区分重载方法

+ 唯一的参数类型列表
    + 仅顺序不同也行
        + 不过这样不好，代码难以维护



### 5.2.2 涉及基本类型的重载

+ 若有该类型，则直接调用
+ 否则直接提升至最接近的
    + `char` 直接提升至 `int`，若无则再向上提升
    + 没有考虑 `short` 

```java
public static void main(String[] args){
    char a = 1;
    f(a);
}

public static void f(byte a){System.out.println("byte");}
public static void f(char a){System.out.println("char");}
public static void f(short a){System.out.println("short");}
public static void f(int a){System.out.println("int");}
public static void f(long a){System.out.println("long");}
public static void f(float a){System.out.println("float");}
public static void f(double a){System.out.println("double");}
```



### 5.2.3 以返回值区分重载方法

+ **不行**



## 5.3 默认构造器

+ 若 **没有** 构造器，编译器自动生成一个 **默认的无参构造器**
+ 若有构造器，则不会有默认构造器



## 5.4 this 关键字

### 5.4.1 构造器调用其他构造器

+ `this` 语句
    + 对 `this` 的调用必须是构造器中的第一个语句
    + 只能调用一个

+ 可以判别递归调用

```java
class Ha{
    Ha(){this(1);}
    Ha(int i){this();System.out.println(i);}   // 错误: 递归构造器调用
    public void ha(){System.out.println("ha");}
}
```

+ 判别参数和成员字段

```java
class Ha{
    public int i = 10;
    Ha(){}
    public void func(int i){
        System.out.println(this.i); // 10
    }
}


public class Test{
    public static void main(String[] args){
        new Ha().func(10086); // 10
    }
}
```

### 5.4.2 static

+ 静态方法内部不能调用非静态方法
    + 反之 OK



## 5.5 清理 : 终结处理和垃圾回收

+ `finalize`

+ 原理

    + 一旦垃圾回收器准备好释放对象所占用的存储空间，首先调用 `finalize` 方法，

        并且在下一次垃圾回收动作发生时，才会真正回收对象占用的内存

+ 存在意义

    + `native` 方法调用了 `C` 风格的函数 `malloc`

+ 一种用法

    + 基类中重载 `finalize` 函数，做一些终结条件检查
    + 调用 `System.gc()` 强制回收垃圾
    + [Test01.java](Test01.java)

    ```java
    class Tank{
    
        // 是否弹药充足
        private boolean full;
    
        // 构造函数
        Tank(boolean full){
            this.full = full;
        }
    
        // 进攻
        public boolean attack(){
            if(!this.full){
                System.out.println("ERROR:The tank is lack of ammunition!");
                return false;
            }
            this.full = false;
            return true;
        }
    
        // finalize
        @Override
        protected void finalize(){
            if(this.full)
                System.out.println("ERROR:The tank has enough ammunition!");
            // super.finalize(); // Object
            System.out.println("GC");
        }
    }
    
    
    public class Test01{
        public static void main(String[] args){
            // test01();
            test02();
        }
    
        public static void test01(){
            System.out.println("---test01---");
            Tank tank1 = new Tank(true);
            Tank tank2 = new Tank(false);
            Tank tank3 = new Tank(true);
            Tank tank4 = new Tank(false);
            tank1.attack();
            tank2.attack();
            System.gc();
        }
    
        public static void test02(){
            System.out.println("---test02---");
            new Tank(true).attack();
            new Tank(false).attack();
            new Tank(true);
            new Tank(false);
            System.gc();
        }
    
    }
    ```

    ```output
    分别注释 test01,test02
    
    case1 : 注释 test02, 只有 test01(当前无法确认tank1,2,3,4是否为垃圾)
    ---test01---
    ERROR:The tank is lack of ammunition!
    
    case2 : 注释 test01, 只有 test02
    ---test02---
    ERROR:The tank is lack of ammunition!
    GC
    ERROR:The tank has enough ammunition!
    GC
    GC
    GC
    ```

+ 垃圾回收方法
    + `stop-and-copy` 停止复制法
    + `mark-and-sweep` 标记清除法
    + 分代
    + 自适应技术
+ `JIT` : `Just-In-Time`
    + 将程序全部或者部分翻译成本地机器码
    + 惰性评估 `lazy evaluation`
        + 在必要的时候才使用 `JIT`   
+ `Java HotSpot`
    + 代码每执行一次就会有一些优化



## 5.6 成员初始化

+ 可以直接在声明的时候进行赋初值
    + `C++` 不支持

```java
class TEST{
    int a = f(); // OK
    int b = 2;   // OK
    int f(){return 1;}
}
```

+ 构造器初始化

### 5.7.1 初始化顺序

+ 变量定义
    + 变量定义的先后顺序决定了初始化的顺序
    + 在任何方法（包括构造器）调用之前先进行初始化
    + 包括 `{}` 语句中的初始化
+ 构造器



## 5.7.2 静态数据的初始化

+ 静态对象在第一次被访问的时候创建
+ 只在该类对象第一次加载的时候初始化一次
+ 静态对象初始化早于非静态对象

```java
class AA{
    int b = 1;
    static int a = 2;
    static {
        System.out.println("static");
    }
    AA(int b){
        System.out.println(b);
    }
}

public class Test{
    public static void main(String[] args){
        AA aa = new AA();
    }
}
```

```output
static
1
```



## 5.8 数组初始化

```java
int[] a; // 更直观(一般用这个)
int a[]; // 符合 C++/C 编程习惯
```

```java
int[] a = {1, 2, 3 , 4, 5}; // 等价与调用 new, 再进行初始化
```

+ `length` 数组的长度
    + 只读，不可修改
+ 越界抛出异常
+ 数组中的元素会被赋初值
    + 各种 `0`

```java
// 都 OK
int[] a = {1, 2,};
int[] b = {1, 2};
// 还能这样
int[] c = new int[]{
    1,2,3,
};
```



### 5.8.1 可变参数列表

+ `Object` 数组

```java
public class Test{
    public static void main(String[] args){
        printArrayOld(new Object[]{new Integer(1), new Float(1.0F)});
        // printArrayOld(new Integer(1), new Float(1.0F)); // ERROR
        /* 需要: Object[]
         * 找到: Integer,Float
         * 原因: 实际参数列表和形式参数列表长度不同
         */
        
        printArrayNew(new Object[]{new Integer(1), new Float(1.0F)});
        printArrayNew(new Integer(1), new Float(1.0F)); 
        printArrayNew(); // 没有参数也 OK, 这个很牛逼
    }
    
    // 可变参数
    public static void printArrayNew(Object...args){
        for(Object o : args){
            System.out.println(o);
            // System.out.println(o.toString());
        }
    }
    
    public static void printArrayOld(Object[] args){
        for(Object o : args){
            System.out.println(o);
        }
    }
}
```

```java
public class Test{
    public static void main(String[] args){
        printArrayNew(1, 2); 
        // printArrayNew();  ERROR
        /*
         * Test 中的方法 printArrayNew(Integer...) 
         * 和 Test 中的方法 printArrayNew(Long...) 都匹配
         */
    }
    public static void printArrayNew(Integer...args){}
    public static void printArrayNew(Long...args){}
}
```



## 5.9 枚举类型

+ `enum`
+ 可以当作一个特殊的类使用
+ 自动添加  `toString()` 方法
+ 自动添加 `ordinal()` 方法，按照常量添加顺序进行从 `0` 开始的赋值

```java
enum Cost{
    ZERO,ONE,TWO
}

public class Test{
    public static void main(String[] args){
        for(Cost cost : Cost.values())
            System.out.println(cost + ":" + cost.ordinal());
    }    
}
```

```output
ZERO:0
ONE:1
TWO:2
```

+ 可以在 `switch` 中使用



