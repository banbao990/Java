# Chapter_02 一切都是对象

> [总目录](../README.md)

---

[TOC]

---



## 2.1 用引用操纵对象

+ 只有 **引用**



## 2.2 必须由你创建所有对象

### 2.2.1 存储位置

+ 寄存器
    + `C,C++` 允许向编译器建议寄存器的分配方式
+ 堆栈 `stack`
+ 堆 `heap`
+ 常量存储
    + 字符串池

+ 非 `RAM` 存储
+ 数据完全存活在程序之外
    + 流对象，持久化对象



### 2.2.2 特例 : 基本类型

| 基本类型 | 大小(bit) | 包装器类型 |
| :------: | :-------: | :--------: |
| boolean  |     -     |  Boolean   |
|   char   |  **16**   | Character  |
|   byte   |     8     |    Byte    |
|  short   |    16     |   Short    |
|   int    |    32     |  Integer   |
|   long   |    64     |    Long    |
|  float   |    32     |   Float    |
|  double  |    64     |   Double   |
|   void   |     -     |    Void    |

+ 直接存储值在堆栈中
+ 注意 `boolean` 只能赋值为 `true,false`
+ `char` : **16 bit**， 支持 `unicode`
+ 高精度计算类
    + `BigInteger`，`BigDecimal`
    + 对应 `int`，`float`
    + 支持任意精度的整数、定点数



### 2.2.3 数组

+ 默认 **边界检查**、初始化为 `0`
+ 少量内存、时间开销



## 2.3 永远不需要销毁对象

### 2.3.1 作用域

+ 作用域 `scope`
+ 自由格式语言 `free-form` 
    + 空格、制表符、换行不会影响程序执行结果
+ 作用域不允许重叠

```java
int x = 0;
{
    int x = 0; // ERROR
}
```

+ 对比 `C++`
    + 隐藏大作用域的变量

```C++
int x = 0;
{
    int x = 1;
    printf("%d\n", x); // 1
}
printf("%d\n", x);     // 0
```

+ 如下 `java` 代码是可以通过编译的

```java
public class Test {
    
    static int x = 0;
    
    public static void main(String[] args) {
        test(1);
    }
    
    public static void test(int x){
        System.out.println(x); // print : 1
        return;
    }
}
```



### 2.4 创建新的数据类型 : 类

+ 类 `class`

### 2.4.1 字段和方法

+ 字段 : 数据成员

+ 方法 : 成员函数

+ 基本成员默认值 : **默认初始化**

    + 各种形式的 `0`

    + `char` : `null(\u0000)`

    + 如果不是字段，则没有初始化机制

        + 编译器会对没有初始化的变量的使用 **报错**

            ```java
            t.java:10: 错误: 可能尚未初始化变量x
            // fileName : line ERROR : msg
            ```



## 2.5 方法、参数和返回值

+ 方法签名 : 方法名 + 参数列表

    + 唯一确定一个方法

    + `java 8` 测试
    
        ```java
        // 只有返回值不同 : ERROR
        class Test{
            char x;
            public void func(int x){
                System.out.println(x);
            }
        
            // 错误: 已在类 test中定义了方法 func(int)
            public int func(int x){
                System.out.println(x);        
                return 0;
            }
        }
        ```
    
+ **发送消息给对象**

+ 参数列表



## 2.6 构建一个 java 程序

### 2.6.1 名字可见性

+ `C++` : 引入命名空间 `namespace` 来避免命名冲突 
+ `java` : `package`
    + ex : `net.mind.view.utility.foibles`



### 2.6.2 运用其他构件

+ `java` 支持使用后面定义的类
    + `C++` 不支持
    + 即 **向前引用** 问题
    
+ 导入某一个具体的类

    ```java
    // 例如导入 ArrayList
    import java.util.ArrayList;
    
    // 导入多个(ArrayList, HashMap)
    import java.util.*; // 通配符
    ```



### 2.6.3 static 关键字

+ 同一个类的所有对象共享一个存储空间

```java
public class Test{
    public static void main(String[] args){
        // new 对象引用(指向同一地址,可以通过两个对象修改来验证)
        System.out.println(new StaticTest().test);
        // 类名引用
        System.out.println(StaticTest.test);
    }
}

class StaticTest{
    static int test = 1;
}
```

+ 使用类名进行引用 **更好**
    + 有利于编译器的优化
    + 强调 `static` 结构，代码思路更清晰



## 2.7 你的第一个 Java 程序

+ `HelloWorld.java`

```java
// java.lang 类库被自动导入

public HelloWorld{
   public static void main(String[] args){
       System.out.println("Hello world!");
   }
}
```

+ 类名必须与文件名相同
    + 一个文件中只能有一个 `public` 类
        + 非 `public` 可以在其他文件中声明
        + 但是一般分开成多个文件会比较好（一个文件一个类）
            + 方便修改、增加功能、编译
+ `ShowProperties.java`

```java
public class ShowProperties{
    public static void main(String[] args){
        System.getProperties().list(System.out);
        System.out.println(System.getProperty("user.name"));
        System.out.println(System.getProperty("java.library.path"));
    }
}
```



## 2.8 注释与嵌入式文档

```java
// comment 1
/* comment 2 */
/** javadoc */
```

+ `javadoc`

    + 嵌入式 HTML

    + 文档标签

        ```java
        @author banbao
        ```

+ `javadoc` 只有 `public,protected`

    + 可以使用 `-private` 来实现涵盖 `private`

+ 一些示例

```java
// 引用其他类
@see
// 行内引用
@link
@docRoot
@inheritDoc
@version
@author
@since
@param
@return
@throws
@deprecated
```



## 2.9 代码风格

+ 驼峰风格
    + `CamelStyle`
+ 类名首字母大写
    + `ExampleClass`
+ 其他首字母小写
    + `exampleFiled`

