# Chapter_09 接口

> [总目录](../README.md)

---

[TOC]

---

+ **接口** 和 **内部类** 为我们提供了一种将接口和实现分离的更加结构化的方法



## 9.1 抽象类和抽象方法

+ 在一些设计中，基类的方法是哑方法 `dummy`，只是为导出类提供一个 **通用接口**
    + **抽象基类** / **抽象类**
+ 抽象类
    + 只有声明没有方法体
    + 如果一个类存在抽象方法，则必须声明为抽象类 `abstract`
    + 无法为抽象类 `new`  对象
        + `错误: A是抽象的; 无法实例化`
+ 如果一个类是抽象类，那么道导出类要么覆盖所有的 `abstract` 方法，要么还是一个抽象类
+ 我们可以将一个没有抽象方法的类声明为抽象类，此时作用为无法为这个类生成实例



## 9.2 接口 interface

+ 所有的方法都是抽象方法，所有方法都是 `public` 的
+ 接口中可以有字段，但是这些字段都是隐式的 `static`，`final` 的
+ 可以实现 **一组** 接口 `implements`
    + 只能继承自 **一个基类**
+ 如果一个类 `implements` 于一个接口，那么这个类要么实现这个接口中的所有方法，要么声明为抽象类

```java
interface A{
    void func();
}

abstract class B implements A{}

class C implements A{
    void func(){}
}
```



## 9.3 完全解耦

+ 如果有两套类具备相同的方法，可以定义一个接口声明它们公共的方法，

    让他们都 `implements` 这个接口，让代码复用性更强

```java
interface Processor{
    ...
}

class A implements Processor{
    ...
}

class B implements Processor{
    ...
}

public class Test{
    public static void main(String[] args){

    }

    public static func(Processor p, Object s){
        ...
    }
}
```

+ 复用代码方式
    + 按照上述代码框架，`implements` 这个接口，并且按照其方法进行编写自己的类
    + 使用 **适配器模式**
        + 类似于 **代理** 的方法



## 9.4 Java 中的多重继承

+ 允许实现多个接口
+ 由于不存在接口的相关储存，一个方法即使在多个接口中出现，但是也只会有一种实现
    + 即使是基类中存在接口中的方法，也只会有一个实现
    + 注意访问权限的一致

```java
class Derived extends Base implements interfaceA, interfaceB{
    // stmt
}
```

+ 如果接口和基类中存在同名的字段，会报错

```java
interface A{
    int a = 10;
    void func();
}

class C{
    int a = 0;
    public void func(){}
}

class B extends C implements A{
    public void func(){
        // 错误: 对a的引用不明确
        // C 中的变量 a 和 A 中的变量 a 都匹配
        System.out.println(a);
    }
}

class Test{
    public static void main(String...args){
        new B().func();
    }
}
```



## 9.5 通过继承来扩展接口

+ 注意接口允许多重继承

```java
interface InterfaceA{}
interface InterfaceB{}
interface InterfaceC extends InterfaceA, InterfaceB{}
```

### 9.5.1 组合接口时的名字冲突

+ 名字相同，返回值不同会报错
+ 权限问题也会报错
    + 如果都是接口则不存在这个问题

```java
interface InterfaceA{
    int func();
}

interface InterfaceB{
    void func();
}

// 错误: 类型InterfaceB和InterfaceA不兼容;
// 两者都定义了func(), 但却带有不相关的返回类型
interface InterfaceC extends InterfaceA, InterfaceB{}
```



## 9.6 适配接口

+ 方法的参数是一个接口

```java
new Scanner(Readable); // 参数是一个 Readable 接口
// 只需实现 read() 方法
```



## 9.7 接口中的域

+ `static` ，`final`，`public`

+ `java SE5` 之前常用于创建枚举类型

```java
public interface Week{
    // 自动 public, static, final
    int
        Sunday = 0, Monday = 1, Tuesday = 2, Wednesday = 3,
        Thirsday = 4, Friday = 5, Saturday = 6;
}
```



### 9.7.1 初始化接口中的域

+ 不允许是 **空 final**，但是允许被非常量初始化
+ **注意这些域不是接口的一部分，它们的值被存储在该接口的静态存储区域内**



## 9.8 嵌套接口

+ 接口可以嵌套在类或其他接口中
    + 类似于内部类
+ 在类或者接口中的嵌套接口可以被实现为 `private`
    + 关于接口的访问控制权限
        + 接口中不加 `private` 则均为 `public`
        + 类中不加 `private` 表示为 `default`，可以实现为 `public`
    + 只能在类中声明接口为 `private` ，接口中不能
    + 声明为 `private` 的接口可以实现为 `public` 类
        + 这里的 `private` 表示这个嵌套类不能在包含这个嵌套类的类的外部用作他用
        + 这里的 `public` 表示这个嵌套类能在外面被包含这个嵌套类的类使用
        + 具体见例子

```java
class A{
    private interface AA{
        public void fun();
    }

    public class AAImp implements AA{
        public void fun(){}
    }

    public AA getAA(){
        return new AAImp();
    }

    public void testAA(AA test){}
}

interface B{
    // 错误: 非法的修饰符组合: public和private
    private interface BB{
        public void fun();
    }
}

public class Test{
    public static void main(String...arg){
        // 错误: AA 在 A 中是 private 访问控制
        A.AA a = new A().getAA();
        // 这是正确的
        new A().testAA(new A().getAA());
    }
}
```



## 9.9 接口与工厂

+ **工厂方法设计模式**
    + 生成遵循某个接口的对象的典型方式
+ [Factories.java](Factories.java)

```java
// 定义服务工厂接口
interface Service {
    void method1();
    void method2();
}

// 定义工厂接口
interface ServiceFactory {
    Service getService();
}

// 服务工厂接口实现 1
class Implementation1 implements Service {
    Implementation1() {} // 包访问权限
    public void method1() {System.out.println("Implementation1 method1");}
    public void method2() {System.out.println("Implementation1 method2");}
}

// 工厂接口实现 1
class Implementation1Factory implements ServiceFactory {
    public Service getService() {
        return new Implementation1();
    }
}

// 服务工厂接口实现 2
class Implementation2 implements Service {
    Implementation2() {} // 包访问权限
    public void method1() {System.out.println("Implementation2 method1");}
    public void method2() {System.out.println("Implementation2 method2");}
}

// 工厂接口实现 2
class Implementation2Factory implements ServiceFactory {
    public Service getService() {
        return new Implementation2();
    }
}

public class Factories {
    // 一个函数接受接口作为参数, 可以实现提供不同的服务
    // 如此便可以实现,不修改这个函数就能提供更多的服务功能
    //     1. 增加服务工厂和提供的服务(类)
    //     2. 而不修改这个函数
    public static void serviceConsumer(ServiceFactory fact) {
        Service s = fact.getService();
        s.method1();
        s.method2();
    }
    public static void main(String[] args) {
        serviceConsumer(new Implementation1Factory());
        // 新的服务
        serviceConsumer(new Implementation2Factory());
    }
}
```

```output
Implementation1 method1
Implementation1 method2
Implementation2 method1
Implementation2 method2
```

