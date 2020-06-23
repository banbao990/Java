# Chapter_03 操作符

> [总目录](../README.md)

---

[TOC]

---

## 3.1 更简单的打印语句

+ `net.mindview.util.Print`



## 3.2 使用 Java 操作符

+ 基本上只支持基本类型

```java
+   -   *   /   %
```

+ `String` 类支持 `+/+=`

+ 适用于所有对象

```java
=   ==  !=
```



## 3.3 优先级

+ 括号真香



## 3.4 赋值

+ 基本数据类型，复制值
+ 非基本数据类型，复制引用
    + 别名
    + 参数传递



## 3.5 算术操作符

```java
+   -   *   /   %
+=  ...
++  --
```

+ 取模 `%` 向下取整
+ 随机数

```java
Random rand = new Random(47); // 47 : magic number
```



### 3.5.1 一元加、减操作符

```java
-   +
```

```java
x = a * -b; // 等价于 x = x * (-b)
```

+ `+` 将类型提升为 `int`

```java
byte a = 1;
byte b = +a; // 错误: 不兼容的类型: 从int转换到byte可能会有损失
```



## 3.6 自动递增和递减

```java
++  --
```

+ 注意 **前缀式** 和 **后缀式**

```java
int a = 0;
System.out.println(a++);     // 0
System.out.println(a);       // 1
System.out.println(++a);     // 2
System.out.println(a);       // 2
```

+ `Java = C++ --`



## 3.7 关系操作符

```java
<   >   <=  >=  ==  !=
```

+ 注意对象（非基本数据类型）的 `==` 比较比较的是 **引用**

+ `equals`
    + 注意最好重载一下这个函数



## 3.8 逻辑操作符

```java
&&  ||  !
```

+ 注意短路操作 [Test01.java](Test01.java)

```java
public class Test01{
    public static void main(String[] args){
        System.out.println("test1:&&");
        boolean t1 = (falseFunc() && trueFunc());
        System.out.println("test2:||");
        boolean t2 = (trueFunc() || falseFunc());
    }
    
    public static boolean falseFunc(){
        System.out.println("FALSE");
        return false;
    }
    
    public static boolean trueFunc(){
        System.out.println("TRUE");
        return true;
    }
}
```

```output
test1:&&
FALSE
test2:||
TRUE
```

+ 注意浮点数的比较 
    + `eps`



## 3.9 直接常量

```java
int a = 0xFF;   // 16 进制 : 255
int b = 077;    // 8 进制  : 63
float c = 1.0F;
double d = 1.0D;    // 小数默认 double
float e = 1.1e2;    // exp 指数记数法
```



## 3.10 按位操作符

```java
&   |   ^   ~
&=  |=  ^=
```

+ `boolean` 可以使用  `&,|,^`
    + 但是不会短路



## 3.11 移位操作符

```java
>>  <<  // 有符号
<<< >>> // 无符号
// 可以与 = 组合使用
```

+ 只能作用于`int`

+ 提升数据类型为 `int`

    + 先转换再移位

    ```java
    char a = 0xFF;
    System.out.println((int)a);     // 255
    System.out.println(a<<1);       // 510
    ```

+ 与 `=` 一起操作

    + 先转换为 `int` 再移位操作，最后进行截断

    ```java
    char a = 0xFF;
    System.out.println((int)a);     // 255
    a >>= 1;                        // 高位补了0,而不是1
    System.out.println((int)a);     // 127,而不是128
    ```

    + 注意以下的示例

    ```java
    byte a = 1;
    a <<= 1;
    // a = a<<1; ERROR
    // 错误: 不兼容的类型: 从int转换到byte可能会有损失
    ```

    

## 3.12 三元操作符

+ `if-else`

```java
a = exp1 ? exp2 : exp3;
/*
 * if(exp1) a = exp2;
 * else a = exp3;
 */
```

+ 一个测试 [Test02.java](Test02.java)

```java
public class Test02{
    public static void main(String[] args){
        boolean a = trueFunc() ? trueFunc() : falseFunc();
    }
    
    public static boolean falseFunc(){
        System.out.println("FALSE");
        return false;
    }
    
    public static boolean trueFunc(){
        System.out.println("TRUE");
        return true;
    }
}
```

```output
TRUE
TRUE
```



## 3.13 字符串操作符 + / +=

+ 一个表达式如果是 **字符串** 打头，那么就是一个字符串
    + 本质上搞清楚运算符的优先级就行

```java
System.out.println("" + 1 + 2); // 12
System.out.println(1 + 2);      // 3
```



## 3.14 使用操作符时的常见错误

```java
// java 不允许这种情况:ERROR
while(x = y){
    ...
}
```



## 3.15 类型转换操作符

+ `cast`
+ 窄化转换 `narrowing conversion`
    + 需要显示操作
+ 扩展转换 `widening conversion`
    + 编译器自动操作
+ 允许所有的基本操作类型互转
    + `boolean`  除外

+ **截尾和舍入**

    + 默认截尾 

    ```java
    System.out.println((int) 1.1D);     // 1
    System.out.println((int) 1.9D);     // 1
    System.out.println((int) -1.1D);    // -1
    System.out.println((int) -1.9D);    // -1
    ```

+ **提升**

    + `<<`
    + 注意对任意的 `byte,char,short` 的运算都会造成提升
    + 注意以下代码

    ```java
    byte a = 1;
    byte b = 1;
    a += b; // OK
    // a = a + b; ERROR
    // 错误: 不兼容的类型: 从int转换到byte可能会有损失
    ```

    



## 3.16 Java 没有 sizeof

+ 因为不存在 **移植** 问题



