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



## 3. Compress

+ 关于压缩文件

### 3.1 [GZip.java](./Compress/GZip.java)

+ 单文件压缩
+ `windows` 自带工具 `gzip`

```txt
Usage:
    (1) compress : java GZIP -c srcFileName [dstFileName]
    (2) decompress : java GZIP -d srcFileName [dstFileName]
        if src's suffix is not ".gz", dst is needed!
```

```powershell
## 压缩文件 ##
java GZip -c GZip.java
# 等价于 java GZip -c GZip.java GZip.java.gz

## 解压缩文件 ##
java GZip -d GZip.java.gz
# 等价于 java GZip -c GZip.java.gz GZip.java

# 如果 test 是 "gzip" 格式的
java GZip -d test 			# 报错
java GZip -d test test.dat 	# 正确执行
```



### 3.2 [Zip.java](./Compress/Zip.java)

+ 多文件压缩

```txt
Usage:
    (1) compress : java Zip -c file1 file2 ... -a dst [-m comment]
        can not change the order!
    (2) decompress : java Zip -d file
```



