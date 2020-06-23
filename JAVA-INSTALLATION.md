# JAVA 安装(win10)

[TOC]

## 1. 官网下载 jdk + jre

+ 官网
    + https://java.sun.com
    + https://www.oracle.com/java/technologies/
+ `JDK/JRE`
    +  https://www.oracle.com/java/technologies/javase-downloads.html
    + 此处安装版本如下
        +  `jdk-8u251-windows-x64.exe`
        +  `jre-8u251-windows-x64.exe`



## 2. 新建安装目录

+ 注意文件路径中不能含有中文
+ 首先创建文件加用于设置安装目录

```shell
D:\installed-application\Java
```

+ 在上述文件路径下新建两个文件夹

```shell
D:\installed-application\Java\JDK
D:\installed-application\Java\JRE
```





## 3. 安装 JDK/JRE

+ 双击 `*.exe` 文件安装至上述 `JDK/JRE` 目录即可



## 4. 环境变量配置

### 4.1 手动配置

+ 打开 **文件资源管理器**
+ 左侧 **此电脑**，右键 **属性**
+ 左侧 **高级系统设置**
+ **环境变量**
+ **系统变量** 中新建 `JAVA_HOME`
    + 变量名 : `JAVA_HOME`
    + 值 : `D:\installed-application\Java\JDK`
    + 这里 `JAVA_HOME` 只是为了后面的设置可以使用相对路径，比较简单
    + 后面使用到 `%JAVA_HOME%` 只是字符串的简单拼接
+ **系统变量** 中新建 `CLASSPATH`
  
    + 变量名 : `CLASSPATH`
+ 值 : `.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;`
  
+ **系统变量** 中找到 **PATH** 变量

    + `PATH` 是 `cmd` 运行文件的查找路径，这一部分的配置是为了命令行使用
+ 只需要配置 `javac.exe`，`java.exe` 自动配置好了
    + 在最后新加如下值
    
    ```shell
    D:\installed-application\Java\JDK\bin
    D:\installed-application\Java\JDK\jre\bin
    ```

    + 或者是如下值
    
    ```shell
    %JAVA_HOME%\bin
    %JAVA_HOME%\jre\bin
    ```



### 4.2 测试

```shell
# 打开 cmd(win + R)

# 若输出一下内容则命令行配置成功(自动配置)
java -version

# 手动配置
javac -version
```

```shell
# java -version
java version "1.8.0_251"
Java(TM) SE Runtime Environment (build 1.8.0_251-b08)
Java HotSpot(TM) 64-Bit Server VM (build 25.251-b08, mixed mode)

# javac -version
javac 1.8.0_251
```



---

+ 基本配置就已经结束了，以下是其他的一些有的没的

---



## 5 配置一个库文件夹

+ 这主要是在命令行调用外部库的时候可以方便一点，而不需要一个一个打 `classpath`

+ 新建文件夹用于放置外部库

```shell
D:\installed-application\Java\OtherLib
```

+ **系统变量** 中新建变量 `otherJavaLib`
    + 变量名 : `otherJavaLib`
    + 值 : `D:\installed-application\Java\OtherLib`



### 5.1 例子

+ 以下以 `json.jar` 为例
+ 为了方便我们将 `json.jar` 文件放在上述文件夹下

```shell
D:\installed-application\Java\OtherLib
```

+ 如果以下的 `test.java`  文件中使用了 `json` 库

```java
import org.json.JSONException;
import org.json.*;
```

#### (1) 直接编译

+ 报错

```java
javac test.java

test.java:3: 错误: 程序包org.json不存在
import org.json.JSONException;

test.java:4: 错误: 程序包org.json不存在
import org.json.*;
```

### (2) classpath

+ 使用 `classpath` 可以正确编译

```java
javac -classpath D:\installed-application\Java\OtherLib\json.jar test.java
```



### (3) 上述方法

+ 在 **系统变量** `CLASSPATH` 的最后面 **添加** 如下路径

```shell
%otherJavaLib%\json.jar;
```

+ 正确编译

```shell
javac test.java
```



## 6. 其他

+ 以下文件夹下有 `java` 源代码

```shell
D:\installed-application\Java\JDK\src.zip
```