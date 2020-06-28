# Chapter_07 复用类

> [总目录](../README.md)

---

[TOC]

---

+ **组合** 与 **继承**



## 7.1 组合语法

+ 字段如果是一个对象的话，初始化为 `null`
    + 初始化
        + 定义处
        + 构造器中
        + **惰性初始化** : 在调用的时候初始化
        + 实例初始化



## 7.2 继承语法

+ 隐式继承自 `Object`
+ 一般的方法，为了继承
    + 把所有的字段指定为 `private`
    + 把所有的方法指定为 `public`
+ `super.func()`
    + 调用父类的同名方法
+ 任意一个类都可以有一个 `main` 函数
    + 即使是非 `public` 类，也可以调用
    + 便于调试

```java
class Test{
    public static void main(String[] args){
        // stmt;
    }
}
```

+ 一个 `trick`
    + 如果一个编译单元中没有 `public` 类，那么文件名可以随便起
    + 到那时难于管理



### 7.2.1 初始化基类

+ 先调用基类的构造器，再调用导出类的构造函数

```java
class Base{
    public Base(){
        System.out.println("Base constructor");
    }
    
    public Base(int a){
        System.out.println("Base constructor with an integer arg");    
    }
}

class Derived extends Base{
    public Derived(){
        super();
        System.out.println("Derived constructor");
    }
    
    public Derived(int a){
        super(a);
        System.out.println("Derived constructor with an integer arg");
    }
    
}

class Derived2 extends Base{
    
}

public class TestExtends{
    public static void main(String[] args){
        System.out.println("----------");
        new Derived();
        System.out.println("----------");
        new Derived(1);
        System.out.println("----------");
        new Derived2();// 都是调用默认构造函数
    }
}
```

```output
----------
Base constructor
Derived constructor
----------
Base constructor with an integer arg
Derived constructor with an integer arg
----------
Base constructor
```



## 7.3 代理

+ `Java` 没有直接提供对代理的直接支持
+ 在一个类中包含另外一个对象，通过这个类的调用间接调用包含对象的方法
    + 可以隐藏实现细节
    + 可以实现只提供代理对象的方法的某一个 **子集**



## 7.4 结合使用组合和继承

### 7.4.1 确保正确清理

```java
try{
    // stmt
}finally{
    // clear
}
```

+ 注意基类对象和导出类对象的清理顺序
    + 先子类，后父类



### 7.4.2 名称屏蔽

+ 导出类重新定义的方法 **不会** 屏蔽其在基类中的任何版本
+ `c++`  在子类重载了方法之后，基类的所有重载方法失效
+ [TestOverride.cpp](testOverride.cpp)

```C++
#include <stdio.h>

class Test{};

class A{
    public:
    void fun(int a){
        printf("A:%d\n", a);
    }
    void fun(Test a){
        printf("Test\n");
    }
};

class B : A{
    public:
    void fun(float a){
        printf("B:%f\n", a);
    }
};

int main() {
    A testA = A();
    B testB = B();
    testA.fun(1.0f); // warning C4244: '参数' : 从 'float' 转换到 'int', 可能丢失数据
    testA.fun(1);
    testA.fun(Test());
    
    printf("------\n");
    testB.fun(1.0f);
    testB.fun(1);
    // testB.fun(Test()); 
    // error C2664: 'void B::fun(float)' : 无法将参数 1 从 'Test' 转换为 'float'
    
    return 0;
}
```

```output
A:1
A:1
Test
------
B:1.000000
B:1.000000
```

+ `java` 不会，`java` 保留父类的所有重载方法
+ [TestOverride.java](TestOverride.java)

```java
class Test{}

class A{
    public void fun(int a){
        System.out.printf("A:%d\n", a);
    }
    public void fun(Test a){
        System.out.printf("Test\n");
    }
};

class B extends A{
    public void fun(float a){
        System.out.printf("B:%f\n", a);
    }
}

public class TestOverride{
    public static void main(String[] args){
        A testA = new A();
        B testB = new B();
        // testA.fun(1.0f);
        /*
         * TestOverride.java:26: 错误: 对于fun(float), 找不到合适的方法
         * 方法 A.fun(int)不适用
         * (参数不匹配; 从float转换到int可能会有损失)
         * 方法 A.fun(Test)不适用
         * (参数不匹配; float无法转换为Test)
         */ 
        testA.fun(1);
        testA.fun(new Test());
        
        System.out.printf("------\n");
        testB.fun(1.0f);
        testB.fun(1);
        testB.fun(new Test());
    }
}
```

```output
A:1
Test
------
B:1.000000
A:1
Test
```



## 7.5 在组合与继承之间选择

+ 在新的类中放置子对象
    + 组合 : 显式
    + 继承 : 隐式
+ 组合
    + 一种设计方法
        + `private` 字段，然后在方法中调用
        + 隐藏基类具体的实现细节
+ 继承



## 7.6 protected 关键字

## 7.7 向上转型 upcasting

+ 导出类是一个基类

## 7.8 final 关键字

+ **效率**、**设计**

### 7.8.1 final 数据

+ 告知编译器某一块数据是不会改变的
    + **编译时常量**
    + 运行时初始化，但是不希望其被改变
+ 对象引用是基本类型数据的时候，表示这个数据无法被改变
+ 引用对象是非基本类型数据的时候，则无法改变这个数据指向另外的对象
    + 但是这个对象指向的数据区域仍然可以被改变
+ 一个既是 `static` 又是 `final` 的域只占据一段不能改变的存储空间
    + **编译期常量**
    + 一般习惯上将其命名为 **大写**  `EXAMPLE`
    + 字与字之间用下划线分隔 `EXAMPLE_ONE`



#### **7.8.1.1 空白 final** 

+ 声明时没有赋值，在后面只允许赋值一次
    + 类中的 `final` 字段在声明时不赋值，但是在构造函数中赋值
    + [TestFinal.java](TestFinal.java)



#### 7.8.1.2 final 参数

+ 无法改变这个参数
+ [TestFinalArg.java](TestFinalArg.java)

```java
/**
 * @author banbao
 */

class B{
    public int a;
    public B(int a){
        this.a = a;
    }
}

class A{
    public static void testFinal(final B b){
        // b = new B(10);
        // 错误: 不能分配最终参数 b
        b.a = 2; // OK, 没有改变 b 指向的对象是哪一个,只是改变了 b 指向对象的值
    }
}

public class TestFinalArg{
    public static void main(String[] args){
        B b = new B(1);
        A.testFinal(b);
        System.out.println(b.a);
    }
}
```



### 7.8.2 final 方法

+ 好处
    + 锁定方法
        + 在继承中方法行为保持不变，并且不会被覆盖
    + （早期）提高效率
        + 将 `final` 方法转为内嵌代码，消除方法调用造成的开销
        + 现在不需要了



#### 7.8.2.1 final 与 private

+ 类中所有的 `private` 方法都隐式指定为 `final`

    + 注意覆盖只有在某方法是基类接口的一部分时才会出现

        即，必须能将一个对象向上转型成为它基本类型并调用相同的方法

    + 注意 `private` 方法在子类中的同名方法，只是同名而已，并 **不是** 覆盖

    + 如下代码是  `OK` 的

    ```java
    class A{
        private void func(){
            System.out.println("A:private");
        }
    }
    
    class B{
        // @Override // 错误: 方法不会覆盖或实现超类型的方法
        public void func(){
            System.out.println("B:public");
        }
    }
    
    class Test{
        public static void main(String[] args){
            new B().func();
        }
    }
    ```

+ 注意如下代码

+ 子类重载方法的访问权限 **不得低于** 基类，但是 **可以高于** 基类

```java
class A{
    protected void func(){}
}

class B extends A{
    @Override
    public void func(){}
}

class C{
    public void func(){}
}

class D extends C{
    @Override
    protected void func(){}
    // 错误: D中的func()无法覆盖C中的func()
    // 正在尝试分配更低的访问权限; 以前为public
}
```



### 7.8.3 final 类

+ 不允许被继承，不允许具有子类
    + 于是 `final` 类的方法都会被隐式的指定为 `final`
+ `final` 类中的字段可以指定为 `final` 或者不是 `final`  



## 7.9 初始化及类的加载

+ 初次使用的时候，所有的`static` 对象和 `static` 代码段都会在加载时依 **程序中的顺序** 依次初始化
    + 只初始化一次
+ 注意在加载子类的 `static` 代码段时，必然会先加载基类的 `static` 代码段
    + 即使是不会发生基类对象的生成
+ `static` 方法的加载早于 `static` 字段
+ [TestStatic.java](TestStatic.java)

```java
public class TestStatic{
    public static void main(String[] args){
        System.out.println("-----1");
        // A.c = 0; // 注释语句(1)
        // 注释语句(1)验证对于 static 变量的引用或者类对象的生成都会触发类的加载
        System.out.println("-----2");
        new A(); // 会先检查基类的 static 代码
        System.out.println("-----3");
        new A(); // 没有输出, static 代码块只会执行初始化一次
    }
}

class B{
    static{
        System.out.println("B:static test!"); // 输出顺序: 1
    }
}

class A extends B{
    static{
        System.out.println("A:static test 1!"); // 输出顺序: 2
    }
    static{
        System.out.println("A:static test 2!"); // 输出顺序: 3(顺序输出)
    }
    static int c = 0;
    static {
        int d = c + 1;
        // int b = a + 1; // 错误: 非法前向引用
    }
    static int a = 0;
    static int d = func(); // static 方法的加载早于 static 字段
    static int func(){
        return 0;
    }
}
```

```output
将 "注释语句(1)" 注释
-----1
-----2
B:static test!
A:static test 1!
A:static test 2!
-----3

不将 "注释语句(1)" 注释
-----1
B:static test!
A:static test 1!
A:static test 2!
-----2
-----3
```

