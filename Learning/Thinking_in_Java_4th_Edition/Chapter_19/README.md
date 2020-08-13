# Chapter_19 枚举类型

> [总目录](../README.md)

---

[TOC]

---

+ 关键字 `enum` 可以将一组具名的值的优先级和创建为一种新的类型，
+ 而这些具名的值可以作为常规的程序组件使用，很棒！



## 19.1 基本 enum 特性

+ `enum` 类继承自 `java.lang.Enum`

```java
public abstract class Enum<E extends Enum<E>>
    implements Comparable<E>, Serializable {}
```

+ [Number](Number.java)

```java
package number;
public enum Number {
    ZERO, ONE, TWO, THREE, FOUR, FIVE,
}
```

+ [SimpleEnum](SimpleEnum.java)

```java
import number.Number;
public class SimpleEnum {
    public static void main(String...args) {
        final String seg = "----------------------";
        for(Number num : Number.values()) {
            System.out.println(
                num + " ordinal: " + num.ordinal() + "\n"
                + num.compareTo(Number.ZERO) + " "
                + num.equals(Number.ZERO) + " "
                + (num == Number.ZERO) + "\n"
                + num.getDeclaringClass() + "\n"
                + num.name() + "\n"
                + seg
            );
        }
        // 从字符串中生成
        for(String s : "ZERO ONE TWO".split(" ")) {
            Number num = Enum.valueOf(Number.class, s);
            System.out.println(num);
            // toString()  <=>  name()  <=>  name
        }
        System.out.println(seg);
        // 直接用名称
        System.out.println(Number.ZERO);
    }
}
```

```outptu
ZERO ordinal: 0
0 true true
class number.Number
ZERO
----------------------
ONE ordinal: 1
1 false false
class number.Number
ONE
----------------------
TWO ordinal: 2
2 false false
class number.Number
TWO
----------------------
THREE ordinal: 3
3 false false
class number.Number
THREE
----------------------
FOUR ordinal: 4
4 false false
class number.Number
FOUR
----------------------
FIVE ordinal: 5
5 false false
class number.Number
FIVE
----------------------
ZERO
ONE
TWO
----------------------
ZERO
```



### 19.1.1 将静态导入用于 enum

+ [StaticImportEnum](StaticImportEnum.java)

```java
import static number.Number.*;
public class StaticImportEnum {
    public static void main(String...args) {
        System.out.println(number.Number.ONE);
        System.out.println(ONE);
    }
}
```



## 19.2 向 enum 中添加新的方法

+ 除了不能继承自一个 `enum` 之外，我们基本上可以将 `enum` 看作一个常规的类
+ 可以加方法
+ 实例必须在方法之前

```java
enum EnumMethod {
    R("red"), G("green"), B("blue");
    public final String description;
    // Constructor must be package or private access:
    private EnumMethod(String description) {
        this.description = description;
    }
    public static void main(String...args) {
        for(EnumMethod em : EnumMethod.values()) {
            System.out.println(
                em.toString() + ":"
                + em.description + "\n"
                + "--------------"
            );
        }
    }
}
/* Output
R:red
--------------
G:green
--------------
B:blue
--------------
*/
```

### 19.2.1 覆盖 enum 的方法

+ 例如 `toString()` ，从而产生更加合理的输出



## 19.3 switch 语句中使用 enum

+ [Signal](Signal.java)

```java
public enum Signal {
    RED, GREEN, YELLOW,
}
```

+ [TrafficLight](TrafficLight.java)

```java
public class TrafficLight {
    Signal color = Signal.RED;
    public void change() {
        switch(color) {
            // 不能声明为 Signal.RED
            case RED:
                color = Signal.GREEN;
                break;
            case GREEN:
                color = Signal.YELLOW;
                break;
            case YELLOW:
                color = Signal.RED;
                break;
        }
    }
    public String toString() {
        return "The traffic light is " + color;
    }
    public static void main(String[] args) {
        TrafficLight t = new TrafficLight();
        for(int i = 0; i < 7; i++) {
            System.out.println(t);
            t.change();
        }
    }
}
/* Output
The traffic light is RED
The traffic light is GREEN
The traffic light is YELLOW
The traffic light is RED
The traffic light is GREEN
The traffic light is YELLOW
The traffic light is RED
*/
```

+ 在 `switch` 的 `case` 语句中不能声明为 `Signal.RED`，而是 `RED`
+ 一个神奇的地方，编译上述 `TrafficLight.java` 时会产生两个文件
    + `TrafficLight.class`
    + `TrafficLight$1.class` （因为 `switch` 语句）
        + `swtich(int)` 的时候不会生成多余的类

```java
// javap -p TrafficLight$1.java
// Compiled from "TrafficLight.java"
class TrafficLight$1 {
  static final int[] $SwitchMap$Signal;
  static {};
}
```

```java
// javap -c TrafficLight$1.java
// Compiled from "TrafficLight.java"
class TrafficLight$1 {
  static final int[] $SwitchMap$Signal;

  static {};
    Code:
       0: invokestatic  #1                  // Method Signal.values:()[LSignal;
       3: arraylength
       4: newarray       int
       6: putstatic     #2                  // Field $SwitchMap$Signal:[I
       9: getstatic     #2                  // Field $SwitchMap$Signal:[I
      12: getstatic     #3                  // Field Signal.RED:LSignal;
      15: invokevirtual #4                  // Method Signal.ordinal:()I
      18: iconst_1
      19: iastore
      20: goto          24
      23: astore_0
      24: getstatic     #2                  // Field $SwitchMap$Signal:[I
      27: getstatic     #6                  // Field Signal.GREEN:LSignal;
      30: invokevirtual #4                  // Method Signal.ordinal:()I
      33: iconst_2
      34: iastore
      35: goto          39
      38: astore_0
      39: getstatic     #2                  // Field $SwitchMap$Signal:[I
      42: getstatic     #7                  // Field Signal.YELLOW:LSignal;
      45: invokevirtual #4                  // Method Signal.ordinal:()I
      48: iconst_3
      49: iastore
      50: goto          54
      53: astore_0
      54: return
    Exception table:
       from    to  target type
           9    20    23   Class java/lang/NoSuchFieldError
          24    35    38   Class java/lang/NoSuchFieldError
          39    50    53   Class java/lang/NoSuchFieldError
}
```



## 19.4 values() 神秘之处

+ `java.lang.Enum` 源代码中没有 `values()` 方法
+ [Reflection](Reflection.java)
+ 通过反射发现 `values()` 方法是编译器加入的一个 `static` 方法
    + 同时加入了 `valuesOf(String)` 的方法
    + `Enum`：`valuesOf(String,int)`
    + 因此如果将 `Signal` 向上转型为 `Enum` 则无法访问 `valuesOf()` 方法
        + 可以通过**反射**访问所有的  `enum` 实例
            + `Class`：`getEnumConstants()`
+ 例如之前的 `Signal.java`
    + 注意 `Signal` 类是 `final` 的，因此无法被继承

```java
// javap -p Signal.class
// Compiled from "Signal.java"
public final class Signal extends java.lang.Enum<Signal> {
  public static final Signal RED;
  public static final Signal GREEN;
  public static final Signal YELLOW;
  private static final Signal[] $VALUES;
  public static Signal[] values();
  public static Signal valueOf(java.lang.String);
  private Signal();
  static {};
}
```



## 19.5 实现，而非继承

+ `enum` 类都继承自 `java.lang.Enum`
    + 因此自定义的 `enum` 类不能再继承字其他类
    + `public enum Signal extends java.lang.Enum  {}` 也是不行的
+ 可以实现接口 `public enum Signal implments InterfaceA  {//...}`



## 19.6 随机选取

+ 通用的随机选取方法
    + 泛型
+ [Enums](Enums.java)

```java
import java.util.Random;
public class Enums {
    private static Random rand = new Random(47);
    public static <T extends Enum<T>> T random(Class<T> enumClass) {
        return random(enumClass.getEnumConstants());
    }
    public static <T> T random(T[] values) {
        if(values == null || values.length == 0) {
            return null;
        }
        return values[rand.nextInt(values.length)];
    }
    // test
    public static void main(String...args)
        throws Exception {
        for(int i = 0;i < 10; ++i) {
            System.out.println(Enums.random(Signal.class));
        }
    }
}


/* Output
YELLOW
YELLOW
GREEN
YELLOW
GREEN
YELLOW
GREEN
YELLOW
RED
GREEN
*/
```

+ `<T extends Enum<T>>` 表明是一个 `Enum` 实例
    + 参见 `java.lang.Enum`



## 19.7 使用接口组织枚举

+ **注意 `enum` 类是 `final` 的，因此无法被继承**
+ 我们怎么实现对于一个 `enum` 类中的成员进行 **分类** 或者 **扩展** 呢？
+ 接口
+ [Color](Color.java)

```java
public interface Color {
    public enum Red implements Color {
        RED,
        PINK,
        MAROON, // 栗色
        SCARLET, // 绯红, 猩红
        ROSYBROWN, // 红褐色
    }
    public enum Blue implements Color {
        BLUE,
        SKYBLUE,
        SLATEBLUE, // 石蓝色
    }
    public enum Green implements Color {
        GREEN,
        SPRINGGREEN, // 春绿色
        SEAGREEN,
    }
    public static void main(String...args) {
        Color color = Green.GREEN;
        System.out.println((color = Enums.random(Red.class)));
        System.out.println((color = Enums.random(Green.class)));
        System.out.println((color = Enums.random(Blue.class)));
    }
}
/* Output
SCARLET
SEAGREEN
SKYBLUE
*/
```

+ 但是上述实现，显然丢弃了很多 `enum` 的优点
+ 可以再使用一层枚举嵌套
+ [EnumOfColor](EnumOfColor.java)

```java
public enum EnumOfColor {
    RED(Color.Red.class),
    BLUE(Color.Blue.class),
    GREEN(Color.Green.class);
    public Color[] values;
    private EnumOfColor(Class<? extends Color> kind) {
        values = kind.getEnumConstants();
    }
    public static void main(String...args) {
        for(int i = 0;i < 5; ++i) {
            EnumOfColor eoc = Enums.random(EnumOfColor.class);
            Color color = Enums.random(eoc.values);
            System.out.println(color);
        }
    }
}
/* Output
SEAGREEN
SLATEBLUE
SLATEBLUE
SLATEBLUE
MAROON
*/
```

+ `private`
    + [EnumOfColor2](EnumOfColor2.java)

```java
public enum EnumOfColor2 {
    RED(Color.Red.class),
    BLUE(Color.Blue.class),
    GREEN(Color.Green.class);
    private Color[] values; // read only
    private EnumOfColor2(Class<? extends Color> kind) {
        values = kind.getEnumConstants();
    }
    public Color randomSelection() {
        return Enums.random(values);
    }
    public static void main(String...args) {
        for(int i = 0;i < 5; ++i) {
            EnumOfColor2 eoc = Enums.random(EnumOfColor2.class);
            Color color = eoc.randomSelection();
            System.out.println(color);
        }
    }
}
/* Output
SEAGREEN
SLATEBLUE
SLATEBLUE
SLATEBLUE
MAROON
*/
```

+ 直接 `enum` 嵌套 `enum`
    + [EnumInEnum](EnumInEnum.java)
    + 感觉没啥区别，就是把上面几个结构套在一起
    + 确实就是重新组织了下代码



## 19.8 使用 EnumSet 替代标志

+ 作为 `enum` 的一种替代品被引入

    + 方便删除或增加元素

+ 元素只能是 `enum` 的成员

    + 而且只能来自于**一个** `enum`

+ 内部表示使用 `long` 存储的 `bit` 向量

    + `Enum sets are represented internally as bit vectors. `

    ```java
    // <= 64(long)
    class RegularEnumSet<E extends Enum<E>> extends EnumSet<E> { /*...*/ }
    // > 64
    class RegularEnumSet<E extends Enum<E>> extends EnumSet<E> { /*...*/ }
    ```

    + 时空效率都很高

+ [EnumSets](EnumSets.java)

```java
import java.util.*;
import animals.*;
import static animals.Animal.*;
import static animals.Animal2.*;

// Animal : HORSE, COW, BIRD, DEER, GOOSE,
// Animal2 : DUCK,
public class EnumSets {
    private static EnumSet<Animal> animals;
    public static void print() {
        System.out.println(animals);
    }
    public static void main(String[] args) {
        animals = EnumSet.noneOf(Animal.class); // Empty set
        print();
        animals.add(HORSE);
        print();
        animals.addAll(EnumSet.of(COW, BIRD));
        print();
        animals = EnumSet.allOf(Animal.class); // FULL
        animals.removeAll(EnumSet.of(DEER, GOOSE));
        print();
        animals.removeAll(EnumSet.range(HORSE, COW)); // 左右都取到
        print();
        animals = EnumSet.complementOf(animals); // 取补集
        print();
        // animals.add(DUCK); // CE
    }
}
/* Output:
[]
[HORSE]
[HORSE, COW, BIRD]
[HORSE, COW, BIRD]
[BIRD]
[HORSE, COW, DEER, GOOSE]
*/
```

+ 发现 `EnumSet` 中的 `of` 方法被疯狂重载，但是效果一致
    + 竟然是**性能**原因，指定参数比可变参数更快
    + 这样子使得更常用的少数元素添加变得更快

```java
public static EnumSet of(Enum,Enum,Enum,Enum,Enum)
public static EnumSet of(Enum,Enum[])
public static EnumSet of(Enum)
public static EnumSet of(Enum,Enum)
public static EnumSet of(Enum,Enum,Enum)
public static EnumSet of(Enum,Enum,Enum,Enum)
```

+ `add` 的顺序没有紧要，底层实现决定



## 19.9 使用 EnumMap

+ `key` 必须是 `enum` 对象
+ 内部由数组实现，很快
+ **命令设计模式**
+ [EnumMaps](EnumMaps.java)

```java
/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;
import animals.*;
import static animals.Animal.*;
import static animals.Animal2.*;

// Animal : HORSE, COW, BIRD, DEER, GOOSE,
public class EnumMaps {
    public static void main(String[] args) {
        EnumMap<Animal, Command> em =
            new EnumMap<Animal, Command>(Animal.class);
        em.put(HORSE, new Command() {
            public void action() {
                System.out.println("Horse Running!");
            }
        });
        em.put(BIRD, new Command() {
            public void action() {
                System.out.println("Bird Flying!");
            }
        });
        for(Map.Entry<Animal,Command> e : em.entrySet()) {
            System.out.print(e.getKey() + ": ");
            e.getValue().action();
        }
        try { // If there's no value for a particular key:
            em.get(COW).action();
            em.get(DUCK).action();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
/* Output
HORSE: Horse Running!
BIRD: Bird Flying!
java.lang.NullPointerException
*/
```

+ 与**常量相关的方法**相比，`EnumMap` 允许修改 `value`，但是常量相关的方法是在编译期确定的



## 19.10 常量相关的方法

+ `constant-specific method`
+ 一个有趣的特性
    + 可以为 `enum` 实例编写方法，从而为每个 `enum` 实例赋予不同的行为
    + 具体方法为先为 `enum` 定义一个或者多个方法，然后为在每个 `enum` 实例里覆盖之
        + 也可以是 `enum` 的每个实例实现 `enum` 的 `abstract` 方法
+ [ConstantSpecificMethod](ConstantSpecificMethod.java)

```java
import java.util.*;
import java.text.*;

public enum ConstantSpecificMethod {
    DATE_TIME {
        @Override
        public String getInfo() {
            return
                DateFormat.getDateInstance().format(new Date());
        }
    },
    JAVA_HOME {
        @Override
        public String getInfo() {
            return System.getenv("JAVA_HOME");
        }
    },
    VERSION {
        @Override
        public String getInfo() {
            return System.getProperty("java.version");
        }
    },
    NOTHING;
    public String getInfo() { return "default"; }
    public static void main(String[] args) {
        for(ConstantSpecificMethod csm : values()) {
            System.out.println(csm + " : " + csm.getInfo());
        }
    }
}
/* Output
DATE_TIME : 2020-8-12
JAVA_HOME : D:\installed-application\Java\JDK
VERSION : 1.8.0_251
NOTHING : default
*/
```

+ 这也被称为表驱动的代码（`table-driven code`）
+ 这似乎是一种多态，定义的 `enum` 和实例之间似乎存在父子类关系，但实际上实例并不是一个类

```java
public enum ConstantSpecificMethod {
    // ...
    public void f1(ConstantSpecificMethod.NOTHING instance) {}
}
// 错误: 找不到符号
// public void f1(ConstantSpecificMethod.NOTHING instance) {}
//                                      ^
// 符号:   类 NOTHING
// 位置: 类 ConstantSpecificMethod
```

+ `javap -p` 也可以看出只是 `static final` 的对象罢了
+ 可以用 `enum`  实现类似匿名内部类的功能
+ [EnumLikeInnerClass](EnumLikeInnerClass.java)

```java
/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.util.EnumSet;
public class EnumLikeInnerClass {
    public enum Func {
        FLY {
            public void func() {
                System.out.println("I can fly!");
            }
        },
        RUN {
            public void func() {
                System.out.println("I can run!");
            }
        },
        SWIM {
            public void func() {
                System.out.println("I can swim!");
            }
        }
        ;
        abstract void func();
    }
    public static void main(String...args) {
        EnumLikeInnerClass elic = new EnumLikeInnerClass();
        EnumSet<Func> es = EnumSet.of(Func.FLY);
        es.add(Func.SWIM);
        es.add(Func.SWIM); // ignore
        es.add(Func.RUN); // 添加顺序无关
        System.out.println(es);
    }
}
/* Output
[FLY, RUN, SWIM]
*/
```



### 19.10.1 使用 enum 的职责链

+ **职责链** （`Chain of Responsibility`）**设计模式**
    + 程序员以多种方式解决一个问题，将他们链接在一起
    + 当一个请求到达时，逐个访问这些方法，知道有一个方法能处理这个请求
    + `socket` 编程中的请求边有这种处理方式
+ 示例代码 `PostOffice.java`



### 19.10.2 使用 enum 的状态机

+ 非常适合用于构建状态机
+ **瞬时状态**（`transient states`）
    + 一旦任务执行完毕，状态机就会立刻离开瞬时状态
+ [State](evenAB/State.java)
    + `DFA` 接受偶数个 `a` , 偶数个 `b` 的序列

```java
package evenAB;
import static evenAB.AB.*;
public enum State {
    AEBE {
        @Override
        State next(AB ab) {
            State ret = null;
            switch(ab) {
            case A: ret = AOBE;
            case B: ret = AEBO;
            }
            return ret;
        }
    },
    AEBO {
        @Override
        State next(AB ab) {
            State ret = null;
            switch(ab) {
            case A: ret = AOBO;
            case B: ret = AEBE;
            }
            return ret;
        }
    },
    AOBE {
        @Override
        State next(AB ab) {
            State ret = null;
            switch(ab) {
            case A: ret = AEBE;
            case B: ret = AOBO;
            }
            return ret;
        }
    },
    AOBO {
        @Override
        State next(AB ab) {
            State ret = null;
            switch(ab) {
            case A: ret = AEBO;
            case B: ret = AOBE;
            }
            return ret;
        }
    };
    abstract State next(AB ab);
}
```



## 19.11 多路分发

+ `Java` 只支持**单路分发**
+ 也就是说要执行的操作包含了不止一个类型位置的对象时，`Java` 的动态绑定机制只能处理期中的一个类型
+ [Number.java](Number.java)
    + [Number1.java](Number1.java)
    + [Number2.java](Number2.java)

```java
public class Number {
    public void test(Number other) {
        System.out.println("Number Number");
    }
    public void test(Number1 other) {
        System.out.println("Number Number1");
    }
    public void test(Number2 other) {
        System.out.println("Number Number2");
    }
}
```

+ [MutliRoad.java](MutliRoad.java)

```java
public class MutliRoad {
    public static void test(Number num1, Number num2) {
        num1.test(num2);
    }
    public static void main(String...args) {
        test(new Number1(), new Number2()); // Number1 Number
    }
}
```

+ 一种解决方法，加上 `instanceof`

```java
public static void test2(Number num1, Number num2) {
    System.out.println("~~~~~ test2 ~~~~~");
    if(num2 instanceof Number1) num1.test((Number1)num2);
    else if(num2 instanceof Number2) num1.test((Number2)num2);
    else if(num2 instanceof Number) num1.test((Number)num2);
}

test2(new Number1(), new Number2()); // Number1 Number2
```

+ 一个关于强制类型转换的测试

```java
(new Number1()).test(new Number2());                       // Number1 Number2
((Number)(new Number1())).test(new Number2());             // Number1 Number2
(new Number1()).test((Number)(new Number2()));             // Number1 Number
((Number)(new Number1())).test((Number)(new Number2()));   // Number1 Number
```

+ 另外解决方法：**使用两次单路分发**
    + 相当于每次将一个未知信息转化为已知信息
+ [NNumber.java](NNumber.java)
    + [NNumber1.java](NNumber1.java)
    + [NNumber2.java](NNumber2.java)

```java
/**
 * @author banbao
 * @comment 修改自示例代码
 */

public class NNumber {
    // 为了防止递归
    public void reverseTest(NNumber other) {
        System.out.println("reverse reverse");
        other.test(this);
    }
    private int set = 0;
    public void test(NNumber other) {
        if(set != 0) {
            System.out.println("NNumber NNumber");
        } else {
            set = 1;
            System.out.println("reverse");
            other.reverseTest(this);
            set = 0;
        }
    }
    public void test(NNumber1 other) {
        System.out.println("NNumber NNumber1");
    }
    public void test(NNumber2 other) {
        System.out.println("NNumber NNumber2");
    }
}
```

```java
test(new NNumber(), new NNumber());
test(new NNumber(), new NNumber1());
test(new NNumber(), new NNumber2());
test(new NNumber1(), new NNumber());
test(new NNumber1(), new NNumber1());
test(new NNumber1(), new NNumber2());
test(new NNumber2(), new NNumber());
test(new NNumber2(), new NNumber1());
test(new NNumber2(), new NNumber2());
/*
reverse
reverse reverse
NNumber NNumber
reverse
reverse reverse
NNumber NNumber1
reverse
reverse reverse
NNumber NNumber2
reverse
reverse reverse
NNumber NNumber
reverse
reverse reverse
NNumber1 NNumber1
reverse
reverse reverse
NNumber1 NNumber2
reverse
reverse reverse
NNumber NNumber
reverse
reverse reverse
NNumber2 NNumber1
reverse
reverse reverse
NNumber2 NNumber2
*/
```



### 19.11.1 使用 enum 分发

+ 示例代码 ：[**石头剪刀布**](../Example_Code/enumerated)
+ [RoShamBo1](RoShamBo1.java)
    + 两次单路分发
+ `enum` 分发
    + `RoShamBo2.java`
    + 直接翻译不行（`enum` 不是类型），需要包装
    + 通过 `switch` 进行第二次分发



### 19.11.2 使用常量相关的方法

+ 比较复杂
+ `RoShamBo3.java`

```java
public enum RoShamBo3 implements Competitor<RoShamBo3> {
    PAPER {
        public Outcome compete(RoShamBo3 it) {
            switch(it) {
                default: // To placate the compiler
                case PAPER: return DRAW;
                case SCISSORS: return LOSE;
                case ROCK: return WIN;
            }
        }
    },
    // ...
}
```

+ `RoShamBo4.java`
    + `RoShamBo3.java` 的压缩版本
    + 直接比较了 `3` 个对象

```java
public enum RoShamBo4 implements Competitor<RoShamBo4> {
    // ...
    ROCK {
        public Outcome compete(RoShamBo4 opponent) {
            return compete(SCISSORS, opponent);
        }
    };
    Outcome compete(RoShamBo4 loser, RoShamBo4 opponent) {
        return ((opponent == this) ? Outcome.DRAW
            : ((opponent == loser) ? Outcome.WIN
                                   : Outcome.LOSE));
    }
    public static void main(String[] args) {
        RoShamBo.play(RoShamBo4.class, 20);
    }
}
```



### 19.11.3 使用 EnumMap 进行分发

+ `RoShamBo5.java`
    + 两层嵌套

```java
enum RoShamBo5 implements Competitor<RoShamBo5> {
    PAPER, SCISSORS, ROCK;
    static EnumMap<RoShamBo5,EnumMap<RoShamBo5,Outcome>>
        table = new EnumMap<RoShamBo5,
            EnumMap<RoShamBo5,Outcome>>(RoShamBo5.class);
    static {
        for(RoShamBo5 it : RoShamBo5.values())
            table.put(it,
                new EnumMap<RoShamBo5,Outcome>(RoShamBo5.class));
        initRow(PAPER, DRAW, LOSE, WIN);
        initRow(SCISSORS, WIN, DRAW, LOSE);
        initRow(ROCK, LOSE, WIN, DRAW);
    }
    static void initRow(RoShamBo5 it,
        Outcome vPAPER, Outcome vSCISSORS, Outcome vROCK) {
        EnumMap<RoShamBo5,Outcome> row =
            RoShamBo5.table.get(it);
        row.put(RoShamBo5.PAPER, vPAPER);
        row.put(RoShamBo5.SCISSORS, vSCISSORS);
        row.put(RoShamBo5.ROCK, vROCK);
    }
    public Outcome compete(RoShamBo5 it) {
        return table.get(this).get(it);
    }
    public static void main(String[] args) {
        RoShamBo.play(RoShamBo5.class, 20);
    }
}
```



### 19.11.4 使用二维数组

+ 简单粗暴
+ `RoShamBo6.java`

```java
enum RoShamBo6 implements Competitor<RoShamBo6> {
    PAPER, SCISSORS, ROCK;
    private static Outcome[][] table = {
        { DRAW, LOSE, WIN }, // PAPER
        { WIN, DRAW, LOSE }, // SCISSORS
        { LOSE, WIN, DRAW }, // ROCK
    };
    public Outcome compete(RoShamBo6 other) {
        return table[this.ordinal()][other.ordinal()];
    }
    public static void main(String[] args) {
        RoShamBo.play(RoShamBo6.class, 20);
    }
}
```

