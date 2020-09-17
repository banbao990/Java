# Chapter_04 控制执行流程

> [总目录](../README.md)

---

[TOC]

---





```java
if-else
while
do-while
for
return
break
switch
// 注意没有 goto, 不过有限制版本的跳转
```



## 4.1 true 和 false

+ 判断 `a` 是否为 `0`

```java
if(!a)      // ERROR in Java, OK in C++/C
if(a != 0)  // both OK in Java and C++/C
```



## 4.2 if-else

```java
// type 1
if(boolean_exp) {stmt;}

// type 2
if(boolean_exp2) {stmt;}
else {stmt;}

// type 3
if(boolean_exp) {stmt;}
else if(boolean_exp) {stmt;}
...
else {stmt;}
```



## 4.3 迭代

```java
// for stmt
for(init_stmt;judge_stmt;iter_stmt) {stmt;}

// while stmt
while(judge_stmt) {stmt;}

// do-while stmt
do{stmt;} while(judge_stmt)
```

### 4.3.1 逗号操作符

+ 一个例子

```java
for(int i = 1, j = 10 - i; i < 10; ++i, --j)
    System.out.println(i + " " + j);
```



## 4.4 Foreach 语法

```java
import java.util.ArrayList;
ArrayList<String> test = new ArrayList<>();
test.add("a");

// ForEach
for(String t : test) System.out.println(t);
```



## 4.5 return

+ 返回值为 `void` 的时候可以不显式 `return`



## 4.6 break 和 continue



## 4.7 臭名昭著的goto

+ 加限制的 `goto`

```java
outer : for(int i = 0;i < 10; ++i){
    inner : for(int j = 0;j < 10; ++j){
        if(j == 0) continue;        // continue inner;
        if(j == 3) break;           // break inner
        if(i == 0) continue outer;
        if(i == 3) break outer;
    }
}
```

```output
1 1
1 2
2 1
2 2
```



## 4.8 switch

+ 只能选择 `int` ，`char` 等整数值



## 4.9 练习

+ [吸血鬼数字](Test01.java)

