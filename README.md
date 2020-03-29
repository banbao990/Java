# Java
some little java program

## 0 Attention

### 0.1 注意编码格式, 如果无法运行请转换成指定格式

### 0.2 在安装好 java 并配置好命令行之后, 可以进行如下操作

+ 编译运行 [*.java] 文件

```java
javac xxx.java
java xxx
```

+ 直接运行 [*.class] 文件

```java
java xxx
```

## 1 [FileFilter](./File/FileFilter/FileFilter.java)

+ 将源文件夹(src)中的所有文件都移动到目的文件夹下(dst)

## 2 [NaiveBayes](./ML/NaiveBayes/NaiveBayes.java)

+ 使用 `Data.txt` 文件中的数据进行朴素贝叶斯估计

## 3 [Rename](./File/Rename/Rename.java)

+ 按照 `key-values.txt` 中的规则对文件进行重命名
+ 其中每一行都包含两个参数(旧文件名,新文件名)
+ 两个参数之间用空格隔开(可以有多余的空格)
+ 例:`aa.txt bb.txt`,`  aa.txt   bb.txt   `