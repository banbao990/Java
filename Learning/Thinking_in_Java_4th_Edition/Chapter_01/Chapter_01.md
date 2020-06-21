# Chapter_01 对象导论

> [总目录](../README.md)

---

[TOC]

---

+ `OOP` : `Object-oriented Programming` 
    + 面向对象编程

## 1.1 抽象过程

+ `Smalltalk` : 第一个成功的面向对象语言
    + 万物皆为对象
        + 概念构建化
    + 程序是对象的集合，他们通过发送消息来告知彼此要做的
    + 每个对象都有自己的由其他对象所构成的存储
    + 每个对象都拥有其类型
        + 类的实例
    + 每一个特定类型的所有对象都可以接收同样的消息

+ `Booch` : 独享具有 **状态**、**行为**和**标识**



## 1.2 每个对象都有一个接口

+ `interface`

+ 接口确定了对某一特定对象所能发出的请求
+ 需要有特定实现
+ `UML` : `Unified Modelling Language`
    + 统一建模语言



## 1.3 每一个对象都提供服务

+ `method`

+ 内聚性



## 1.4 被隐藏的具体实现

+ `Java`
    + `private/protected/public`
    + `default`



## 1.5 复用具体实现

+ 成员对象
+ 组合 `composition`
    + 动态 : 聚合 `aggregation`
+ 新类的成员对象通常都被声明为 `private`，使得新类的客户端程序员不能访问他们



## 1.6 继承

+ 典例
    + 垃圾回收机
    + 几何形
+ 覆盖 `override`
+ 纯粹替代 
    + 子类只是覆盖父类的方法，而不产生新方法



## 1.7 伴随多态的可互换对象

+ 非面向对象 : **前期绑定**
+ **后期绑定** / **动态绑定**
    + `C++` : `virtual`
    + `Java` : 默认就是后期绑定
+ `upcasting` : **向上转型** / **上溯造型**
    + 子类视作基类



## 1.8 单根继承结构

+ `Object`
+ 利于垃圾回收器的实现



## 1.9 容器

+ `C++` : `STL` 的一部分
    + `STL` : `Standard Template Library`
+ `Object Pascal` : `VCL` 的一部分
    + `VCL` : `Visual Component Library`
+ 参数化类型 : **泛型** / **范型**



## 1.10 对象的创建和生命期

+ `Java` : `new`
    + 堆
+ 垃圾自动回收



## 1.11 异常处理 : 处理错误

+ 强制使用



## 1.12 并发编程

+ 线程
+ 并行
+ 共享资源变得麻烦
+ `Java` 内置



## 1.13 Java 与 Internet

+ 客户/服务器系统
+ `html` : `hyperText Makeup Language`
+ `CGI` : `common gateway interface`
+ `Perl`
+ `Python`
+ 客户端编程
    + Web浏览器
    + 插件 `plug-in`
    + 浏览器脚本语言 : `scripting language`
        + `Javascript`
    + `Java` : `applet`
        + 现在基本上不用了
    + `Flash`
        + `Google Chrome` : `2020` 年`12` 月也停止支持了
    + `Flex`
    + `.NET`，`C#`
    + `Intranet` : 企业内部网
+ 服务器端编程


