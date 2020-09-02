# Chapter_20 注解

> [总目录](../README.md)

---

[TOC]

---

+ **注解（元数据）为我们在代码中添加信息提供了一种形式化的方法，**
+ **使我们可以在稍后某个时刻非常方便地使用这些数据**

```java
@Override
@Deprecated
@SuppressWarning
```



## 20.1 基本语法

+ 语法角度上，使用起来和修饰符类似



### 20.1.1 定义注解

+ 看上去像接口的定义
+ 注解也会被编译成 `.class` 文件
+ **元注解**
    + `@Target`：这个注解应该被用在什么地方（方法，域）
    + `@Retention`：在哪一个级别可用
        + `SOURCE`、`CLASS`、`RUNTIME`
+ 在注解中一般都会包含一些元素以表示某些值，在分析处理注解的时候，程序或者工具可以使用它
    + 可以为元素指定默认值
+ **标记注解**：没有元素的注解
+ [Test](../Example_Code/net/mindview/atunit/Test.java)

```java
import java.lang.annotation.*;
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {}
```

+ 使用注解：[Testable](Testable.java)

```java
public class Testable {
    public void execute() {
        System.out.println("Executing..");
    }
    @Test
    void testExecute() { execute(); }
    public static void main(String...args) {
        new Testable().testExecute();
    }
}
```

+ 一个能够使用到注解功能的例子
+ [UseCase](UseCase.java)

```java
import java.lang.annotation.*;
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
    public int id();
    public String description() default "no description";
}
```

+ 使用注解元素
+ [PasswordUtils](PasswordUtils.java)

```java
import java.util.*;
public class PasswordUtils {
    @UseCase(id = 47, description =
    "Passwords must contain at least one numeric")
    public boolean validatePassword(String password) {
        return (password.matches("\\w*\\d\\w*"));
    }
    @UseCase(id = 48)
    public String encryptPassword(String password) {
        return new StringBuilder(password).reverse().toString();
    }
    @UseCase(id = 49, description =
    "New passwords can't equal previously used ones")
    public boolean checkForNewPassword(
        List<String> prevPasswords, String password) {
        return !prevPasswords.contains(password);
    }
}
```



### 20.1.2 元注解

+ `Java SE5` 只内置了 `3` 种 标准注解以及 `4` 种元注解
+ 元注解专职负责注解其他注解的注解
+ `@Target`：表示注解能够使用的地方

```java
// java.lang.annotation.ElementType.java
public enum ElementType {
    /** Class, interface (including annotation type), or enum declaration */
    TYPE,

    /** Field declaration (includes enum constants) */
    FIELD,

    /** Method declaration */
    METHOD,

    /** Formal parameter declaration */
    PARAMETER,

    /** Constructor declaration */
    CONSTRUCTOR,

    /** Local variable declaration */
    LOCAL_VARIABLE,

    /** Annotation type declaration */
    ANNOTATION_TYPE,

    /** Package declaration */
    PACKAGE,

    /**
     * Type parameter declaration
     * @since 1.8
     */
    TYPE_PARAMETER,

    /**
     * Use of a type
     * @since 1.8
     */
    TYPE_USE
}
```

+ `@Retention`：via哦是需要在什么级别保存该注解信息

```java
// java.lang.annotation.RetentionPolicy.java
public enum RetentionPolicy {
    /**
     * Annotations are to be discarded by the compiler.
     */
    SOURCE,

    /**
     * Annotations are to be recorded in the class file by the compiler
     * but need not be retained by the VM at run time.  This is the default
     * behavior.
     */
    CLASS,

    /**
     * Annotations are to be recorded in the class file by the compiler and
     * retained by the VM at run time, so they may be read reflectively.
     *
     * @see java.lang.reflect.AnnotatedElement
     */
    RUNTIME
}
```

+ `@Documented`：将此注解包含在 `Javadoc` 中
+ `Inherited`：允许子类继承父类中的注解

+ 大多数时候，程序员主要是定义自己的注解，并编写自己的处理器来处理他们



## 20.2 编写注解处理器

+ **如果没有用来读取注解的工具，那么注解也不会比注释更加有用**
+ 利用反射机制查询注解
+ [UseCaseTracker.java](UseCaseTracker.java)

```java
// 示例代码
import java.lang.reflect.*;
import java.util.*;
public class UseCaseTracker {
    public static void
    trackUseCases(List<Integer> useCases, Class<?> cl) {
        for(Method m : cl.getDeclaredMethods()) {
            UseCase uc = m.getAnnotation(UseCase.class);
            if(uc != null) {
                System.out.println("Found Use Case:" + uc.id() +
                    " " + uc.description());
                useCases.remove(new Integer(uc.id()));
            }
        }
        for(int i : useCases) {
            System.out.println("Warning: Missing use case-" + i);
        }
    }
    public static void main(String[] args) {
        List<Integer> useCases = new ArrayList<Integer>();
        Collections.addAll(useCases, 47, 48, 49, 50);
        trackUseCases(useCases, PasswordUtils.class);
    }
}
/* Output
Found Use Case:49 New passwords can't equal previously used ones
Found Use Case:48 no description
Found Use Case:47 Passwords must contain at least one numeric
Warning: Missing use case-50
*/
```

+ `UseCase uc = m.getAnnotation(UseCase.class)`
    + `getAnnotation()`：`Field/Method/Class` 都实现了这个方法



### 20.2.1 注解元素

+ 注解元素可用的类型
    + 所有的基本类型 `int/long/...` （不能是包装类型）
    + `String`
    + `Class`
    + `enum`
    + `Annotation`：允许注解嵌套
    + 以上类型的数组

```java
import java.lang.annotation.*;
@Target(ElementType.METHOD) // 一个或者多个,默认是全部元素
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationType {
    public static final String nullStr = ""; // OK
    public int id1() default -1;
    public String id2() default nullStr;
    // public String id2() default nullStr; // 等价于 ""
    public Class id3() default Null.class;
    public CanUseEnum id4() default CanUseEnum.NULL;
    // public CanUseClass id5(); // 错误: 注释类型元素 {0} 的类型无效
    public CanUseAnnotation id6() default @CanUseAnnotation();
    // public CanUseAnnotation id6() default @CanUseAnnotation; // 都表示使用默认值
    public int[] id7() default {-1};
    // public Integer id8(); // 错误: 注释类型元素 {0} 的类型无效
}
class Null {}
enum NullEnum {}
enum CanUseEnum { NULL, }
class CanUseClass {}
@interface CanUseAnnotation {}
@interface NullAnnotation {}
```



### 20.2.2 默认值限制

+ 元素不能有不确定的值
    + 要么是默认值，要么在使用时提供注解使用的值
+ 不能使用 `null`
+ 一般的习惯是自定义默认值
    + `-1`，`""`，`None.class`



### 20.2.3 生成外部文件

+ 一个 `SQL` 的例子
    + [database](../Example_Code/annotations/database)

```java
//: annotations/database/DBTable.java
package annotations.database;
import java.lang.annotation.*;
@Target(ElementType.TYPE) // Applies to classes only
@Retention(RetentionPolicy.RUNTIME)
public @interface DBTable {
    public String name() default "";
}
```

```java
//: annotations/database/Constraints.java
package annotations.database;
import java.lang.annotation.*;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraints {
    boolean primaryKey() default false;
    boolean allowNull() default true;
    boolean unique() default false;
}
```

```java
//: annotations/database/SQLInteger.java
package annotations.database;
import java.lang.annotation.*;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLInteger {
  String name() default "";
  Constraints constraints() default @Constraints;
}
```

```java
//: annotations/database/SQLString.java
package annotations.database;
import java.lang.annotation.*;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLString {
  int value() default 0;
  String name() default "";
  Constraints constraints() default @Constraints;
}
```

```java
//: annotations/database/Uniqueness.java
package annotations.database;
public @interface Uniqueness {
  Constraints constraints()
    default @Constraints(unique=true);
}
```

```java
//: annotations/database/Member.java
package annotations.database;
@DBTable(name = "MEMBER") // 表名
public class Member {
    // 快捷方式,当只有名为 value 变量需要赋值时,可以使用简化版
    @SQLString(30) String firstName;
    @SQLString(50) String lastName;
    @SQLInteger Integer age;
    @SQLString(value = 30,
    constraints = @Constraints(primaryKey = true))
    String handle;
    static int memberCount;
    public String getHandle() { return handle; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String toString() { return handle; }
    public Integer getAge() { return age; }
}
```

+ [注解处理器](../Example_Code/annotations/database/TableCreator.java)

#### 20.2.3.1 变通的方法

+ 多注解修饰一个变量



### 20.2.4 注解不支持继承

```java
// 错误: 对于 @interfaces, 不允许 'extends'
// public @interface SQLInteger extends SQLString { /*...*/ }
```



### 20.2.5 实现处理器

+ [注解处理器](../Example_Code/annotations/database/TableCreator.java)
+ 检查注解并生成 `SQL` 语句



## 20.3 使用 apt 工具处理注解

+ `Java8` 之后没了
+ **注解处理工具 apt**

+ 操作 `java` 源文件，而不是编译后的类
+ `apt` 在默认情况下，会在处理完源文件之后编译它们
+ 当注解处理器生成一个新的源文件时
    + 该文件会在新一轮（`round`）的注解处理中接受检查
    + 直至不再产生源文件
    + 编译所有的源文件
+ 当使用 `apt` 的时候，必须指明一个工厂类，或者指明工厂类的路径
    + 否则 `apt` 将会踏上一个神秘的探索之旅
+ 不能使用反射，替代方案：`mirror API`
    + 能够在未经编译的源代码中查看方法、域、类型
+ 一个例子，提取一个类中的所有 `public` 方法

```java
//: annotations/ExtractInterface.java
//: annotations/Multiplier.java
//: annotations/InterfaceExtractorProcessor.java
//: annotations/InterfaceExtractorProcessorFactory.java
```

+ 定义注解：[ExtractInterface.java](../Example_Code/annotations/ExtractInterface.java)
+ 使用注解，用于测试的 `java` 源文件：[Multiplier.java](../Example_Code/annotations/Multiplier.java)
+ 处理器：[InterfaceExtractorProcessor.java](../Example_Code/annotations/InterfaceExtractorProcessor.java)

```java
public class InterfaceExtractorProcessor
    implements AnnotationProcessor { /* ... */ }
```

```java
package com.sun.mirror.apt;
public interface AnnotationProcessor {
    void process();
}
```

+ 处理器类的构造器以 `AnnotationProcessorEnvironment` 对象为参数
    + 通过该对象，我们可以知道 `apt` 正在处理的所有类型（类定义）
    + 通过其可以获得 `Messager`、`Filer` 对象
        + `Messager` 对象可以用来向用户报告信息，例如处理过程的错误，错误位置等
        + `Filer` 对象是一种 `PrintWriter`，可以用于创建新文件
            + 不使用一般的 `PrintWriter` 是因为，只有这样，`apt` 才知道并对这个文件进行后续处理
            + 代码的具体生成还需要自己掌控，缺少自动的工具（`IR`）
+ 参数
    + `-s`：新生成文件放置目录
+  配套的工厂：[InterfaceExtractorProcessorFactory.java](../Example_Code/annotations/InterfaceExtractorProcessorFactory.java)

```java
public class InterfaceExtractorProcessorFactory
    implements AnnotationProcessorFactory { /* ... */ }
```

```java
package com.sun.mirror.apt;
public interface AnnotationProcessorFactory {
    // 检查是否所有控制台输入的参数都是提供支持的选项
    Collection<String> supportedOptions();
    // 检查是否素有的注解都有相应的处理器
    // 若没有注解完整类名，则只是报警告退出
    Collection<String> supportedAnnotationTypes();
    // 返回注解处理器
    AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds,
                                        AnnotationProcessorEnvironment env);
}
```



## 20.4 将观察者模式用于 apt

+ `visit-accept` 设计模式
+ [TableCreationProcessorFactory.java](../Example_Code/annotations/database/TableCreationProcessorFactory.java)



## 20.5 基于注解的单元测试

+ 这一部分基本上是在学习 `@Unit`

    + 作者自己的一套测试框架
+ `JUnit`

+ 必须使用 `test()` 作为方法名

+ `@Unit`

    + 使用 `@Unit` 测试的包必须定义在某个包内

    + 返回值必须为 `boolean/void`

        + 通过返回值表示测试是否成功
        + 通过 `assert`，异常

        ```java
        ClassLoader.getSystemClassLoader()
            .setDefaultAssertionStatus(true); // Enable asserts
        ```

    + 上述要求是由代码框架要求的：[net/mindview/atunit](../Example_Code/net/mindview/atunit)

        + **可以自己设计一套测试方案**

+ 需要导入包 `net.mindview.atunit`

+ `@Test` 标记测试方法，无参数，返回 `boolean` 表示测试是否成功

```java
//: annotations/AtUnitExample1.java
package annotations;
import net.mindview.atunit.*;
import net.mindview.util.*;

public class AtUnitExample1 {
    public String methodOne() {
        return "This is methodOne";
    }
    public int methodTwo() {
        System.out.println("This is methodTwo");
        return 2;
    }
    @Test
    boolean methodOneTest() {
        return methodOne().equals("This is methodOne");
    }
    @Test
    boolean m2() { return methodTwo() == 2; }
    @Test
    private boolean m3() { return true; }
    // Shows output for failure:
    @Test
    boolean failureTest() { return false; }
    @Test
    boolean anotherDisappointment() { return false; }
    public static void main(String[] args) throws Exception {
        // 原来的代码好像找不到路径
        OSExecute.command(
            "java net.mindview.atunit.AtUnit "
            + "..\\..\\..\\annotations\\AtUnitExample1"
        );
    }
}
```

+ **继承是一种取消父类测试方法的一种方式之一**
    + 当然跟测试框架有关，如果框架中获取了所有方法，那么就需要可以测试父类方法
+ **组合是一种取消父类测试方法的一种方式之一**
    + 持有元素
+ 继承通过重载来进行父类方法测试
    + 可以用于自己没有修改权限，但是可以继承的类的测试
+ `@TestObjectCreate`
    + `static`
    + 调用指定方法构造对象，而不是构造默认对象

```java
@TestObjectCreate static AtUnitExample3 create() {
    return new AtUnitExample3(47);
}
```

+ `@TestObjectCleanup`
    + `static`
    + 调用指定方法清理对象

```java
@TestObjectCleanup static void cleanup(AtUnitExample5 tobj) {
    System.out.println("Running cleanup");
    output.close();
}
```

+ `@TestProperty`
    + 只用于测试时
    + `AtUnitRemover` 会移除方法

```java
@TestProperty static PrintWriter output;
```



### 20.5.1 将 @Unit 用于泛型

+ 让测试类继承自泛型的一个特定版本
+ 缺点，不能测试 `private` 方法
    + 要么修改为 `protected`
    + 要么添加一个 `@TestProperty` 的方法，由它来调用 `private` 方法



### 20.5.2 不需要任何套件

+ `suites`
+ 优于 `JUnit`，不需要套件来组织测试



### 20.5.3 实现 @Unit

```java
//: net/mindview/atunit/Test.java
//: net/mindview/atunit/TestObjectCreate.java
//: net/mindview/atunit/TestObjectCleanup.java
//: net/mindview/atunit/TestProperty.java

//: net/mindview/atunit/AtUnit.java

//: net/mindview/atunit/ClassNameFinder.java
```

+ `@Test`、`@TestObjectCreate`、`@TestObjectCleanup`、`@TestProperty`
    +  必须是 `RUNTIME`，因为 `@Unit` 系统是在编译后的代码中查询注解
+ `AtUnit.java`
    + 通过反射机制抽取注解，并且运行测试
+ `ClassNameFinder.java`
    + 从 `*.class` 文件中找到类名
    + 展示了一部分字节码的储存规范

```java
//: net/mindview/atunit/ClassNameFinder.java
package net.mindview.atunit;
import java.io.*;
import java.util.*;
import net.mindview.util.*;
import static net.mindview.util.Print.*;

public class ClassNameFinder {
    public static String thisClass(byte[] classBytes) {
        Map<Integer,Integer> offsetTable =
            new HashMap<Integer,Integer>();
        Map<Integer,String> classNameTable =
            new HashMap<Integer,String>();
        try {
            DataInputStream data = new DataInputStream(
                new ByteArrayInputStream(classBytes));
            int magic = data.readInt();    // 0xcafebabe
            int minorVersion = data.readShort();
            int majorVersion = data.readShort();
            int constant_pool_count = data.readShort();
            int[] constant_pool = new int[constant_pool_count];
            for(int i = 1; i < constant_pool_count; i++) {
                int tag = data.read();
                int tableSize;
                switch(tag) {
                    case 1: // UTF
                        int length = data.readShort();
                        char[] bytes = new char[length];
                        for(int k = 0; k < bytes.length; k++)
                            bytes[k] = (char)data.read();
                        String className = new String(bytes);
                        classNameTable.put(i, className);
                        break;
                    case 5: // LONG
                    case 6: // DOUBLE
                        data.readLong(); // discard 8 bytes
                        i++; // Special skip necessary
                        break;
                    case 7: // CLASS
                        int offset = data.readShort();
                        offsetTable.put(i, offset);
                        break;
                    case 8: // STRING
                        data.readShort(); // discard 2 bytes
                        break;
                    case 3:    // INTEGER
                    case 4:    // FLOAT
                    case 9:    // FIELD_REF
                    case 10: // METHOD_REF
                    case 11: // INTERFACE_METHOD_REF
                    case 12: // NAME_AND_TYPE
                        data.readInt(); // discard 4 bytes;
                        break;
                    default:
                        throw new RuntimeException("Bad tag " + tag);
                }
            }
            short access_flags = data.readShort();
            int this_class = data.readShort();
            int super_class = data.readShort();
            return classNameTable.get(
                offsetTable.get(this_class)).replace('/', '.');
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    // Demonstration:
    public static void main(String[] args) throws Exception {
        if(args.length > 0) {
            for(String arg : args)
                print(thisClass(BinaryFile.read(new File(arg))));
        } else
            // Walk the entire tree:
            for(File klass : Directory.walk(".", ".*\\.class"))
                print(thisClass(BinaryFile.read(klass)));
    }
}
```



### 20.5.4 移除测试代码

```java
//: net/mindview/atunit/AtUnitRemover.java
```

+ 操作字节码 `*.class` 文件
+ 移除带有 `@net.mindview.atunit.*` 注解的方法
+ 例子如下

```java
package test;
import net.mindview.atunit.*;
import java.io.*;
public class TestRemover {
    @TestProperty static PrintWriter output;
    @TestProperty static int counter;
    @TestProperty static void test1() {
        output = new PrintWriter(System.out);
        output.println("just test!");
    }
    @Test boolean test2() {
        return true;
    }
}
```

```java
// javap -p TestRemover.class
// Compiled from "TestRemover.java"
public class test.TestRemover {
    static java.io.PrintWriter output;
    static int counter;
    public test.TestRemover();
    static void test1();
    boolean test2();
}
```

+ 调用 `AtUnitRemover` 之后
    + 注意 `CLASSPATH` 的问题

```powershell
java net.mindview.atunit.AtUnitRemover -r ..\Chapter_20\test\TestRemover
```

```output
test.TestRemover Method: test1 @net.mindview.atunit.TestProperty
test.TestRemover Method: test2 @net.mindview.atunit.Test
```

```java
// javap -p TestRemover.class
// Compiled from "TestRemover.java"
public class test.TestRemover {
    static java.io.PrintWriter output;
    static int counter;
    public test.TestRemover();
}
```

+ 删除一个域比较麻烦，可能牵扯到关于各种静态初始化等方面
+ `javassist` 开源库，可以看看有无更新
