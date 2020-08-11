# Chapter_18 Java I/O系统 [part2]

> [总目录](../README.md)

---

[TOC]

---

[part1](README.md)



## 18.11 压缩

+ `Java I/O` 类库中的类支持读写压缩格式的数据流
+ 可以对他们使用其他的 `I/O` 类进行封装，提供压缩功能
+ 属于 `InputStream/OutputStream` 继承结构中的一部分

|        压缩类        |                            功能                             |
| :------------------: | :---------------------------------------------------------: |
|  CheckedInputStream  | GetCheckSum() 为任何 InputStream 产生校验和（不仅是解压缩） |
| CheckedOutputStream  | GetCheckSum() 为任何 OutputStream 产生校验和（不仅是压缩）  |
| DeflaterOutputStream |                        压缩类的基类                         |
|   ZipOutputStream    |  一个 DeflaterOutputStream，用于将数据压缩成 Zip 文件格式   |
|   GZipOutputStream   |  一个 DeflaterOutputStream，用于将数据压缩成 GZip 文件格式  |
| InflaterOutputStream |                       解压缩类的基类                        |
|    ZipinputStream    |  一个 InflaterOutputStream，用于解压缩 Zip 文件格式的数据   |
|   GZipinputStream    |  一个 InflaterOutputStream，用于解压缩 GZip 文件格式的数据  |

+ `Zip/GZip` 比较常用



### 18.11.1 用 GZip 进行简单压缩

+ 如果我们相对单个数据流（不是多个互异数据流）进行压缩，这是比较合适的选择
+ 包装流的时候多加一步即可

```java
public static void compress(String src, String dst)
    throws Exception {
    BufferedInputStream in =
        new BufferedInputStream(
        new FileInputStream(src));
    BufferedOutputStream out =
        new BufferedOutputStream(
        new GZIPOutputStream(
            new FileOutputStream(dst)));
    System.err.println("Writing file");
    int c;
    while((c = in.read()) != -1) {
        out.write(c);
        // 会进行自动的转换 out.write((byte)c)
    }
    out.close();
    in.close();
}

public static void decompress(String src, String dst)
    throws Exception {
    System.out.println("Extract file");
    BufferedInputStream in =
        new BufferedInputStream(
        new GZIPInputStream(
            new FileInputStream(src)));
    BufferedOutputStream out =
        new BufferedOutputStream(
        new FileOutputStream(dst));
    int s;
    while((s = in.read()) != -1)
        out.write(s);
    out.close();
    in.close();
}
```



### 18.11.2 使用 Zip 进行多文件保存

+ **多文件压缩**
+ `java.util.zip.*`
    + 比 `GZip` 更加完善
+ `Checksum` 类计算和检验文件的校验和方法
    + `Adler32`：快
    + `CRC32`：准
+ 对于每一个需要加入压缩档案的文件，都必须调用  `putNextEntry()`，并将其传递给 `ZipEntry` 对象
+ `ZipEntry` 包含了一个功能很广泛的接口
    + 允许获取和设置 `Zip` 文件内特定项上所有可利用的数据
        + 名称、压缩的和未压缩的文件大小，日期，`CRC` 校验和、额外字段数据、注释
        + 压缩方法、是否是一个目录入口 ······
+ `ZipEntry` 只支持 `CRC` 接口
+ `ZipEntry` **不支持密码**



### 18.11.3 Java 档案文件

+ `JDK` 自带的 `jar` 工具



## 18.12 对象序列化

+ 目的是为了在程序不存在时仍然能够保存数据
+ 需要实现 `Serializable` 接口
    + 对象转换为字节序列，并且能够通过这个字节完全恢复至原来对象
    + 可以通过网络进行，弥补了不同操作系统之间的差异
+ 实现 **轻量级持久性**（`lightweight persistence`）
+ 对象序列化主要为了支持两个功能
    + `Java` 远程方法调用（`RMI:Remote Method Invocation`）
    + `Java Beans`
+  `Serializable`
    + 只是一个标记接口，没有内容
    + 不需要手动实现 **序列化、反序列化** 的过程
+ 序列化一个对象过程
    + 首先要创建某些 `OutputStream` 对象
    + 然后将其封装在一个 `ObjectStream` 对象中
    + 调用 `writeObject()` 将其序列化
    + 发送给 `OutputStream `（基于字节）
+ 反序列化类似
    + 最终得到的是一个 `Object` ，需要需要向下转型
+ 对象序列化一个很棒的地方
    + 不仅保存了对象的全景图
    + 还保存了对象内的所有引用对应的对象
    + **对象网**（递归）
+ [TestSerializable](TestSerializable.java)
    + 在对象反序列化恢复的过程中，没有调用任何的构造器
    + `static` 数据不影响
    + 检查成员对象是否实现 `Serializable` 接口是在运行时
        + `java.io.NotSerializableException`

```java
/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;
import java.io.*;

class TestSerializable1 implements Serializable {
    public static int cnt = 0;
    private int ID;
    public TestSerializable1() {
        this.ID = ++ cnt;
        System.out.println(
            "default constructor for TestSerializable1");
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[~TestSerializable1~]\n");
        sb.append("    ID:");
        sb.append(this.ID);
        sb.append("\n    cnt:");
        sb.append(cnt);
        return sb.toString();
    }
}

class TestSerializable2 implements Serializable {
    private TestSerializable1 t1;
    private int specialNum;
    public TestSerializable2() {
        this.specialNum =  new Random().nextInt(500);
        System.out.println(
            "default constructor for TestSerializable2");
        t1 = new TestSerializable1();
    }

    public TestSerializable2(int specialNum) {
        this.specialNum = specialNum;
        System.out.println(
            "arg(int) constructor for TestSerializable2");
        t1 = new TestSerializable1();
    }

    @Override
    public String toString() {
        return "specialNum:" + this.specialNum + "\n" + t1;
    }
}

public class TestSerializable implements Serializable {
    private TestSerializable2 t2;
    private static void error() {
        System.err.println("args:1->store,2->restore");
        System.exit(1);
    }

    public static void main(String...args) throws Exception {
        // test static cnt
        System.out.println(
            "TestSerializable1.cnt(static):"
            + TestSerializable1.cnt);

        TestSerializable ts = new TestSerializable();
        if(args.length != 1) { error(); }
        if("1".equals(args[0])) { ts.store(); }
        else if("2".equals(args[0])) { ts.restore(); }
        else{ error(); }

        // test static cnt
        System.out.println(
            "TestSerializable1.cnt(static):"
            + TestSerializable1.cnt);
    }

    private void store() throws Exception {
        System.err.println("Store...");
        t2 = new TestSerializable2();
        System.out.println(t2);
        // serializable
        ObjectOutputStream out =
            new ObjectOutputStream(
                new FileOutputStream("TestSerializable.out"));
        out.writeObject("Store an Object!");
        out.writeObject(t2);
        out.close(); // auto flush
    }

    private void restore() throws Exception {
        System.err.println("Restore...");
        ObjectInputStream in =
            new ObjectInputStream(
                new FileInputStream("TestSerializable.out"));
        String s = (String)in.readObject();
        t2 = (TestSerializable2)in.readObject();
        System.out.println(s);
        System.out.println(t2);
    }
}
```

```output
/// java TestSerializable 1 ///
TestSerializable1.cnt(static):0
Store...
default constructor for TestSerializable2
default constructor for TestSerializable1
specialNum:144
[~TestSerializable1~]
    ID:1
    cnt:1
TestSerializable1.cnt(static):1

/// java TestSerializable 1 ///
TestSerializable1.cnt(static):0
Restore...
Store an Object!
specialNum:144
[~TestSerializable1~]
    ID:1
    cnt:0
TestSerializable1.cnt(static):0
```



### 18.12.1 寻找类

```java
in.readObject();
```

+ 如果正在读取的类的 `.class` 文件不在 `classpath` 中，则会抛出异常

```java
Exception in thread "main" java.lang.ClassNotFoundException: Alien
    at java.net.URLClassLoader.findClass(URLClassLoader.java:382)
    at java.lang.ClassLoader.loadClass(ClassLoader.java:418)
    at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:355)
    at java.lang.ClassLoader.loadClass(ClassLoader.java:351)
    at java.lang.Class.forName0(Native Method)
    at java.lang.Class.forName(Class.java:348)
    at java.io.ObjectInputStream.resolveClass(ObjectInputStream.java:719)
    at java.io.ObjectInputStream.readNonProxyDesc(ObjectInputStream.java:1924)
    at java.io.ObjectInputStream.readClassDesc(ObjectInputStream.java:1807)
    at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2098)
    at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1624)
    at java.io.ObjectInputStream.readObject(ObjectInputStream.java:464)
    at java.io.ObjectInputStream.readObject(ObjectInputStream.java:422)
    at ThawAlien.main(ThawAlien.java:12)
```



### 18.12.2 序列化的控制

+ 例如需要考虑特殊的安全问题，让对象的其中某一部分不被序列化
+ 或者说在反序列化的时候，希望某个子对象被重新创建
+ 实现 `Externalizable` 接口（代替实现 `Serializable` 接口）
    + `Externalizable`  实现了 `Serializable` ，同时添加了两个方法
        + `writeExternal()`
        + `readExternal()`
+ `Externalizable`
    + 恢复一个 `Externalizable` 对象时
        + 所有默认的构造器都会被调用（包括字段定义时的初始化）
        + 然后调用  `readExternal()`
    + [Blips](Blips.java)

```java
import java.io.*;

public class Blip1 implements Externalizable {
    public int cc = 0;
    public Blip1() {
        System.out.println("Blip1 Constructor");
    }
    public void writeExternal(ObjectOutput out)
        throws IOException {
        System.out.println("Blip1.writeExternal");
    }
    public void readExternal(ObjectInput in)
        throws IOException, ClassNotFoundException {
        System.out.println("Blip1.readExternal");
    }
    public static void main(String[] args)
    throws IOException, ClassNotFoundException {
        System.out.println("Constructing objects:");
        Blip1 b1 = new Blip1();
        System.out.println("b1.cc:" + b1.cc);
        b1.cc = 10086;
        System.out.println("b1.cc:" + b1.cc);
        ObjectOutputStream o = new ObjectOutputStream(
            new FileOutputStream("Blips.out"));
        System.out.println("Saving objects:");
        o.writeObject(b1);
        o.close();
        // Now get them back:
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream("Blips.out"));
        System.out.println("Recovering b1:");
        b1 = (Blip1)in.readObject();
        System.out.println("b1.cc:" + b1.cc);
    }
}
```

```java
Constructing objects:
Blip1 Constructor
b1.cc:0
b1.cc:10086
Saving objects:
Blip1.writeExternal
Recovering b1:
Blip1 Constructor
Blip1.readExternal
b1.cc:0
```

+ 正确恢复一个 `Externalizable` 对象

```java
public void writeExternal(ObjectOutput out)
    throws IOException {
    System.out.println("Blip2.writeExternal");
    out.writeInt(cc); // OK
}
public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException {
    System.out.println("Blip2.readExternal");
    cc = in.readInt(); // OK
}
```



#### 18.12.2.1 transient 关键字

+ `transient` 瞬时的
+ 当实现 `Serializable` 接口时使用 `transint` 表示不序列化

+ [TransientTest](TransientTest.java)

```java
public class User implements Serializable {
    public final String username;
    private transient String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public String toString() {
        return "username:" + this.username
                + ", password:" + this.password;
    }
}
```

````java
import java.util.*;
import java.io.*;
public class TransientTest {
    public User user;
    public static void main(String...args)
    throws Exception {
        TransientTest tt = new TransientTest();
        tt.user = new User("banbao", "banbao990");
        store(tt.user);
        restore(tt.user);
    }

    public static void store(Object o) throws Exception {
        System.err.println("Store...");
        System.out.println(o);
        // serialization
        ObjectOutputStream out =
            new ObjectOutputStream(
                new FileOutputStream("TransientTest.out"));
        out.writeObject(o);
        out.close(); // auto flush
    }

    public static void restore(Object o) throws Exception {
        System.err.println("Restore...");
        // deserialization
        ObjectInputStream in =
            new ObjectInputStream(
                new FileInputStream("TransientTest.out"));
        System.out.println(in.readObject());
    }
}
````



#### 18.12.2.2 Externalizable 的替代方法

+ 在实现 `Serializable` 的同时**添加**方法 `readObject() + writeObject()`
+ 方法签名必须一致

```java
private void writeObject(ObjectOutputStream stream)
    throws IOException;

private void readObject(ObjectInputStream stream)
    throws IOException, ClassNotFoundException;
```

+ 这里本质上是 `ObjectOutputStream` 在调用 `writeObject()` 时
+ 利用反射调用了该对象的 `writeObject()` 方法
    + 其首先会检查该类是否定义了 `writeObject()` 方法
    + 若定义，则放弃默认的序列化，否则则用默认的序列化
+ `trick`：`stream.defaultReadObject()`
+ [User2](User2.java)

```java
import java.io.*;
public class User2 implements Serializable {
    public final String username;
    private transient String password;
    public User2(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public String toString() {
        return "username:" + this.username
                + ", password:" + this.password;
    }

    private void writeObject(ObjectOutputStream stream)
    throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(password);
    }

    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException{
        stream.defaultReadObject();
        password = (String)stream.readObject();
    }

    public static void main(String...args)
        throws Exception {
        User2 user = new User2("banbao", "banabo990");
        TransientTest.store(user);
        TransientTest.restore(user);
    }
}
/*
Output:
Store...
username:banbao, password:banabo990
Restore...
username:banbao, password:banabo990
*/
```



#### 18.12.2.3 版本控制

+ 支持但少用，比较难
+ 具体看 `JDK`



### 18.12.3 使用持久性

+ `deep copy` 深拷贝
+ 拷贝整个对象网络
+ [SerializableNet](SerializableNet.java)

```java
// 输入网络(AB序列化到一个文件中)
A---->B

// 输出网络
A---->B
```

```java
// 输入网络(AC序列化到一个文件中)
A---->B<----C

// 输出网络
A---->B<----C
```

```java
// 输入网络(A,C序列化到不同文件里)
A---->B<----C

// 输出网络
A---->B1
B2<----C
```

+ `Serializable` 可以继承
    + 父类 `Serializable`  `=>`  子类 `Serializable`



## 18.13 XML

+ `javax.xml.*`

+ 现在流行的还是 `json`
+ [Book](Book.java)

```java
// 依赖库: nu.xom.*
// http://www.xom.nu
import nu.xom.*;
import java.util.*;
import java.io.*;

import java.util.Arrays;

public class Book {
    private String name, author;
    // constructor1
    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }
    // constructor2
    public Book(Element book) {
        this.name = book.getFirstChildElement("name").getValue();
        this.author = book.getFirstChildElement("author").getValue();
    }
    @Override
    public String toString() {
        return "[ " + this.name + " " + this.author + " ]";
    }
    // produce XML
    public Element getXML() {
        Element book = new Element("book");
        Element ele1 = new Element("name");
        ele1.appendChild(name);
        Element ele2 = new Element("author");
        ele2.appendChild(author);
        book.appendChild(ele1);
        book.appendChild(ele2);
        return book;
    }
    // os
    public static void format(OutputStream os, Document doc)
        throws Exception {
        Serializer serializer= new Serializer(os, "UTF-8");
        serializer.setIndent(4);
        serializer.setMaxLength(60);
        serializer.write(doc);
        serializer.flush();
    }
    public static void main(String...args) throws Exception {
        List<Book> shelf = Arrays.asList(
            new Book("Effective Java", "Joshua Bloch"),
            new Book("Thinking in Java", "Bruce Eckel"),
            new Book("Head First  Java", "Kathy Sierra")
        );
        Element root = new Element("shelf");
        for(Book book : shelf) {
            root.appendChild(book.getXML());
        }
        Document doc = new Document(root);
        format(System.out, doc);
        // format(new BufferedOutputStream(
                // new FileOutputStream("shelf.xml")), doc);
    }
}
/* Output
<?xml version="1.0" encoding="UTF-8"?>
<shelf>
    <book>
        <name>Effective Java</name>
        <author>Joshua Bloch</author>
    </book>
    <book>
        <name>Thinking in Java</name>
        <author>Bruce Eckel</author>
    </book>
    <book>
        <name>Head First Java</name>
        <author>Kathy Sierra</author>
    </book>
</shelf>
*/
```



## 18.14 Preferences

+ 与对象的持久性更密切
+ 不过只能用于小的、受限的数据集合（**基本类型、字符串**）
+ 键值组合
+ `get` 要有默认值
+ 系统资源分配
    + `windows`：注册表

```java
Preferences prefs = Preferences
    .userNodeForPackage(PreferencesDemo.class);

prefs.put("Footwear", "Ruby Slippers");
prefs.putInt("Companions", 4);

int usageCount = prefs.getInt("UsageCount", 0); // 0 为默认值

prefs.clear();
```

