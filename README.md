# Java
> **@comment : 一些 `java` 相关内容**
>
> **@author : banbao**



[TOC]

## 0 Attention

### 0.1 Java 安装

+ [JAVA-INSTALLATION](JAVA-INSTALLATION.md)



### 0.2 注意编码格式, 如果无法运行请转换成指定格式

+ 一般是 `UTF8`



### 0.3 在安装好 java 并配置好命令行之后, 可以进行如下操作

+ 编译运行 [*.java] 文件

```java
javac xxx.java
java xxx
```



+ 直接运行 [*.class] 文件

```java
java xxx
```



### 0.4 Learning

+ `Learning` 文件夹下是学习笔记



## 1 [FileFilter](./File/FileFilter/FileFilter.java)

+ 将源文件夹(src)中的所有文件都移动到目的文件夹下(dst)



## 2 [NaiveBayes](./ML/NaiveBayes/NaiveBayes.java)

+ 使用 `Data.txt` 文件中的数据进行朴素贝叶斯估计



## 3 [Rename](./File/Rename/Rename.java)

+ 按照 `key-values.txt` 中的规则对文件进行重命名
+ 其中每一行都包含两个参数(旧文件名,新文件名)
+ 两个参数之间用空格隔开(可以有多余的空格)
+ 例:`aa.txt bb.txt`,`  aa.txt   bb.txt   `



## 4 [FormatChange](./File/FormatChange)

+ `UTF8` 和 `GBK` 的编码转换

+ [FormatChange.java](./File/FormatChange/FormatChange.java)

    + 单个文件转换

    ```powershell
    java FormatChange <U2G|G2U> inputFileName [outputFileName]
    ```

+ [FormatChangeBat.java](./File/FormatChange/FormatChangeBat.java)

    + 批量文件转换

    ```powershell
    java FormatChangeBat <U2G|G2U> dirName
    ```




## 5. [Dump](File/Dump/Dump.java)

+ 将文件转为 `16` 进制输出

```powershell
java Dump FileName # 默认输出到标准输出
```



## 6. [Kit](./Kit/README.md)

+ 一些不便分类的小工具



## 7. [Application](./Application/)

+ [半自动虾米音乐专辑下载器](Application/xiamiAlbumCrawler2/README.md)
