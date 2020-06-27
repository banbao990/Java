# Chapter_06 访问权限控制

> [总目录](../README.md)

---

[TOC]

---

## 6.1 包 : 库单元

+ `ArrayList`
    + `java.util.ArrayList`

```java
public class Test{
    public static void main(String[] args){
        java.util.ArrayList<String> a = new java.util.ArrayList<>();
    }
}
```

```java
import java.util.ArrayList;

public class Test{
    public static void main(String[] args){
        ArrayList<String> a = new ArrayList<>();
    }
}
```

+ 一个 `java` **编译单元**
    + 一个 `.java` 文件
    + 只能有一个 `public` 类， 且要与文件名相同



### 6.1.1 代码组织

+ 若一个编译单元中含有多个类，则在编译时，会生成多个 `.class` 文件
+ `java` 的工作方式
    + `java` 可运行程序是一组可以打包并压缩成 `java` 文档文件 (`JAR`) 的 `.class` 文件
    + `java` 解释器负责这些文件的查找、装载、解释
+ `package` 语句
    + 必须是文件的第一个语句 
    + 一个 `package` 中只能有一个 `public` 类

```java
package test;
```



### 6.1.2 创建独一无二的包名

+ 使用自己独特 **域名** 反序

+ java` 解释器的运行过程

    + 找到环境变量 `CLASSPATH`
        + `CLASSPATH` 为找 `.class` 文件的根目录
    + 从根目录开始，解释器获取包的名称，并将其转换为路径
        + `foo.bar.baz -> foo\bar\baz`
            + 具体形式取决于操作系统
    + 在目录中查找 `.class` 文件
    + 注意必须是 `public` 才能被外部引用

    

### 6.1.3 定制工具库

+ 在 `CLASSPATH` 中加入 `.` ，这样可以直接访问同目录下的自定义包
+ 导入静态类

```java
import static ...;
```



### 6.1.4 用 import 改变行为

+ `C` 的条件编译
    + 可以实现不修改任何程序代码。就能够切换开关实现不同的行为
    + `java` 没有，因为没有跨平台的问题
+ 条件编译的其他用途
    + **调试**
        + 在开发中开启，发布时禁用



## 6.2 java 的访问权限修饰词



### 6.2.1 包访问权限 default

+ 同包下 `public`
+ 跨包 `private`



### 6.2.2 public 接口访问权限

+ 哪哪都可以访问
+ **默认包**
    + 如果没有声明所属包，则编译器认为属于 **默认包**



### 6.2.3 private 无法访问

+ 除了包含该成员的类之外，其他任何类都无法访问这个成员
+ 其他作用
+ 让一个函数只能在特定的地方被调用，避免误用

```java
// 只能在特定的地方(makeSundae)调用构造函数
class Sundae{
    private Sundae(){}
    public static makeSundae(){
        return new Sundae();
    }
}
```

+ 同样可以防止特定函数被误调用



### 6.2.4 protected 继承访问权限

+ 继承访问，只是不能访问 `private` 

```java
class a{
    private int a;
    public int b;
    int c;      //default
    protected int d;
}

class b extends a{
    public void func(){
        a = 1;  // ERROR, 继承不能访问 private 变量
        b = 1;
        c = 1;
        d = 1;
    }
}
```

+ `protected` 
    + 同包下都可以访问
    + 不同包的 **子类** 可以任意访问
    + 不同包的其他类不能访问



## 6.3 接口和实现

+ 方法声明和实现分离



## 6.4 类的访问权限

+ 只能是 `public`  与 `非public`

