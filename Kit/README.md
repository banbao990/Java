# Kit
> **@comment : 一些不便分类的小工具**
>
> **@author : banbao**

---

[TOC]

---



## 1. [ShowMethods](ShowMethods.java)

+ 利用反射展示类的各种方法(包括继承自父类的方法)



## 2. [OSExecute](OSExecute/Main.java)

+ 调用 `cmd`
+ 这里包装成一个包，需要将该包 `OSExecute` 加入到 `classpath` 中

```powershell
javac -cp "../" OSExecute.java
```

+ 运行时也需要在包所在目录运行

```powershell
java OSExecute.OSExecute
java OSExecute.Main
```



