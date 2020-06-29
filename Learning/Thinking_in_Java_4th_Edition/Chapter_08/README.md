# Chapter_08 多态

> [总目录](../README.md)

---

[TOC]

---

+ 多态 : 动态绑定、后期绑定、运行时绑定



## 8.1 再论向上转型

### 8.1.1 忘记参数类型

+ 多态
+ 代码结构简单，更容易维护、开发

+ 字段是 **编译时绑定**，方法是 **运行时绑定**
+ [TestBind.java](TestBind.java)

```java
class A{
    char a = 'A';
    void func(){
        System.out.println("A");
    }
}

class B extends A{
    char a = 'B';

    @Override
    void func(){
        System.out.println("B");
    }
}

class TestBind{
    public static void main(String...args){
        func(new A());
        System.out.println("-----");
        func(new B());
        System.out.println("-----");
        A a = new B(); // 动态绑定
        a.func();
    }

    public static void func(A a){
        System.out.println(a.a);
        a.func();
    }
}
```

```output
A
A
-----
A
B
-----
B
```



## 8.2 转机

### 8.2.1 方法调用绑定

+ `final` 方法不是动态绑定，是前期绑定



### 8.2.2 产生正确的行为

### 8.2.3 可扩展性

### 8.2.4 缺陷 : “覆盖” 父类 private 方法

+ 父类的 `private` 方法对子类不可见
+ 同名函数不是覆盖，因此也不存在多态

### 8.2.5 缺陷 : 域与静态方法

+ 编译时绑定
+  静态方法 **不是** 多态



## 8.3 构造器和多态

+ 构造器实际上是 `static` 方法，隐式声明

### 8.3.1 构造器的调用顺序

+ 调用顺序
    + 基类构造器（递归）
    + 按照声明顺序调用成员对象的初始化方法
    + 导出类的构造器主体
+ [TestSeq.java](TestSeq.java)

```java
import static java.lang.System.out;

class Warrior{
    Warrior(){
        out.println("Warrior!");
    }
}

class Knight extends Warrior{
    Knight(){
        out.println("Knight!");
    }
}

class PegKnight extends Knight{
    PegKnight(){
        out.println("PegKnight!");
    }
}

class Dancer{
    Dancer(){
        out.println("Dancer!");
    }
}

class Troop{
    public Dancer dance1 = new Dancer();
    public PegKnight pegKnight1 = new PegKnight();
    Troop(){
        out.println("Troop!");
    }
}

public class TestSeq{
    public static void main(String...args){
        new Troop();
    }
}
```

```tput
Dancer!
Warrior!
Knight!
PegKnight!
Troop!
```



### 8.3.2 继承与清理

+ 如果实在需要，可以手动实现一个函数用于处理一些事后事务
+ 调用顺序应该要求与 **析构函数要求的一致**
+ 复杂情况，多处引用
    + 使用引用计数



### 8.3.3 构造器内部的多态方法行为

+ 初始化实际过程
    + **在任何事物发生之前，将分配给对象的存储空间初始化为二进制的 `0`**
    + 基类构造器（递归）
    + 按照声明顺序调用成员对象的初始化方法
    + 导出类的构造器主体
+ [TestConstructorSeq.java](TestConstructorSeq.java)

```java
import static java.lang.System.out;

class Base{
    Base(){
        sayHi();
    }

    void sayHi(){
        out.println("Base:Hi");
    }
}

class Derived extends Base{
    int kind = 1;
    Derived(){}
    void sayHi(){
        out.println("Derived:Hi, my kind is " + kind);
    }
}

public class TestConstructorSeq{
    public static void main(String...args){
        new Derived();
    }
}
```

```output
Derived:Hi, my kind is 0
```

+ `C++` 行为
    + 不多态
    + [TestConstructorSeq.cpp](TestConstructorSeq.cpp)



## 8.4 协变返回类型

+ 允许覆盖方法返回父类返回类型的导出类型
+ [TestOverrideReturnType.java](TestOverrideReturnType.java)

```java
/**
 * @author banbao
 */

import static java.lang.System.out;

class Base1{}
class Derived1 extends Base1{}

class Base2{
    public Base1 func(){
        out.println("Base2");
        return new Base1();
    }
}
class Derived2 extends Base1{
    // Derived1 extends Base1
    public Derived1 func(){
        out.println("Derived2");
        return new Derived1();
    }
}

public class TestOverrideReturnType{
    public static void main(String...args){
        new Derived2().func();
    }
}
```

```output
Derived2
```



## 8.5 用继承进行设计

+ 用继承表达性行为间的差异，用字段表达状态上的变化
+ `FE` 中的转职设计
+ [Evolution.java](Evolution.java)

```java
import static java.lang.System.out;

class Warrior{
    public void attack(){
        out.println("Attacking prototype!");
    }
}

class FalcoKnight extends Warrior{
    @Override
    public void attack(){
        out.println("FalcoKnight attacking!");
    }
}

class PegKnight extends Warrior{
    @Override
    public void attack(){
        out.println("PegKnight attacking!");
    }
}

class Fighter{
    private Warrior warrior = new PegKnight();
    public void evolution(){
        this.warrior = new FalcoKnight();
    }
    public void attack(){
        this.warrior.attack();
    }
}

public class Evolution{
    public static void main(String...args){
        Fighter fighter = new Fighter();
        fighter.attack();
        fighter.evolution();
        fighter.attack();
    }
}
```

```output
PegKnight attacking!
FalcoKnight attacking!
```



### 8.5.1 纯继承与扩展

+ 纯继承 `is-a`
    + 只有在基类中已经建立的方法才可以在导出类中被覆盖
        + 导出类中的所有方法都在基类中存在方法
    + 一种 **纯替代**
        + 导出类可以完全代替基类
        + 通过多态实现
+ 扩展 `is-like-a`
    + 有额外的方法



### 8.5.2 向下转型和运行时类型识别

+ 若基类不存在导出类的方法，在运行时会抛出异常 `ClassCastException`

```java
class A{
    void func(){}
}

class B extends A{
    void func(){}
    void f(){};
}

class Test{
    public static void main(String...args){
        ((B)(new A())).f();
        // Exception in thread "main" java.lang.ClassCastException:
        // A cannot be cast to B
    }
}
```



