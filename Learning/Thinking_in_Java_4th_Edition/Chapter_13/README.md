# Chapter_13 字符串

> [总目录](../README.md)

---

[TOC]

---

## 13.1 不可变 String

+ `String` 类中修改 `String` 的方法都是创建了一个新的 `String` 对象



## 13.2 重载 `+` 与 `StringBuilder`

+ `+`、`+=`
+ 如下代码底层引入了可变的 `StringBuilder` ，为了避免过多的垃圾回收
    + [TestString.s](TestString.s)
    + `javap -c TestString.class`

```java
String a = "aa";
String b = "aaa" + a + "aaa" + a;
```

+ 但是编译器的优化是有限的，如果修改过多，那么建议使用 `StringBuilder`
+ `StringBuffer` 线程安全，`StringBuilder` 线程不安全



## 13.3 无意识的递归

+ [TestRecursion.java](TestRecursion.java)

```java
public class TestRecursion {
    @Override
    public String toString(){
        // this 会调用 toString() 方法导致递归
        // return "Addr:" + this;
        return "Addr:" + super.toString();
    }

    public static void main(String...arg){
        TestRecursion tr = new TestRecursion();
        System.out.println(tr);
    }
}
```

```output
Addr:TestRecursion@15db9742
```



## 13.4 String 上的操作

+ 常用构造函数

```java
String(); // ""
String(String string); // 浅拷贝,由于不可变,深拷贝没有意义
String(char value[]);
String(char value[], int offset, int count);
String(byte bytes[], String charsetName);
```

+ 其他函数

```java
int length(){ return value.length; } // 返回长度,感觉还比较快
boolean isEmpty() { return value.length == 0; }
char charAt(int index); // 有边界检查

// codePoint
// 需要理解下 UTF-16 编码格式
// 有些特殊的unicode字符需要用两个char存储
int codePointAt(int index);
int codePointBefore(int index);
int codePointCount(int beginIndex, int endIndex);

// 输出字符数组
void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin);

// 获取字节流
byte[] getBytes(String charsetName);
byte[] getBytes(Charset charset);
byte[] getBytes();// 默认 "ISO-8859-1"

// equals
//     1. 比较相等,先看是否相同
//     2. 若是 String, 接着逐字符比较
// contentEquals 可以是 StringBuffer,StringBuilder,String,CharSequence
boolean equals(Object anObject);
boolean contentEquals(CharSequence cs);
boolean contentEquals(StringBuffer cs);
// 忽略大小写
boolean equalsIgnoreCase(String anotherString);
int compareTo(String anotherString);// this < anotherString
int compareToIgnoreCase(String str);

// 以 prefix 开头
boolean startsWith(String prefix);
boolean startsWith(String prefix, int toffset); // 从 toffset 开始计算
boolean endsWith(String suffix); // 调用上述函数

// hashCode 只计算一次
int hashCode();
/*
 * for (int i = 0; i < value.length; i++) {
 *     h = 31 * h + val[i];
 * }
 */

// 查找字符所在位置(-1)
// 会检测是否需要代理(UTF-16)
int indexOf(int ch);
int indexOf(int ch, int fromIndex);
int lastIndexOf(int ch);
int lastIndexOf(int ch, int fromIndex); // 后往前
// 使用的是暴力的匹配算法
int indexOf(String str);
int indexOf(String str, int fromIndex);
static int indexOf(char[] source, int sourceOffset, int sourceCount,
                   String target, int fromIndex);
static int indexOf(char[] source, int sourceOffset, int sourceCount,
                   char[] target, int targetOffset, int targetCount,
                   int fromIndex);
// 类似的有 4 个 lastIndexOf() 方法

// 子串
// CharSequence 是个 interface
String substring(int beginIndex, int endIndex);
String substring(int beginIndex);
CharSequence subSequence(int beginIndex, int endIndex); // 没有简化版

// 拼接
String concat(String str);

// 替换字符
// 替换是所有匹配上的字符
String replace(char oldChar, char newChar);

// 调用正则表达式进行匹配 Pattern
boolean matches(String regex);

// 是否包含某个字符串
// 调用 indexOf
boolean contains(CharSequence s);

// 替换字符串
String replaceFirst(String regex, String replacement); // 替换第一次匹配上的
String replaceAll(String regex, String replacement); // 全替换
String replace(CharSequence target, CharSequence replacement); // 全替换,Pattern

// 拆解字符串
// 快速匹配 fastpath, 否则 Pattern
String[] split(String regex, int limit);
/* fastpath if the regex is a
(1)one-char String and this character is not one of the
RegEx's meta characters ".$|()[{^?*+\\", or
(2)two-char String and the first char is the backslash and
the second is not the ascii digit or ascii letter.
*/
String[] split(String regex); // 等价 split(regex, 0);

// 以 delimiter 为分隔符拼接若干字符串
static String join(CharSequence delimiter, CharSequence... elements);
static String join(CharSequence delimiter,
            Iterable<? extends CharSequence> elements);

// 大写转小写
String toLowerCase(); // toLowerCase(Locale.getDefault());
String toLowerCase(Locale locale);
/*
1.检查是否需要替换(UTF-16)
2.替换(一些特殊字符的检查)
'\u03A3' GREEK CAPITAL LETTER SIGMA
'\u0130' LATIN CAPITAL LETTER I WITH DOT ABOVE
*/

// 去除两端的空白字符
String trim();// <= ' '

// 转化为 char[]
char[] toCharArray();

// 格式控制 Formatter
static String format(String format, Object... args);
static String format(Locale l, String format, Object... args);

// valueOf
// 基本上就是 toString
// Object,char[],int,...

// 常量池
native String intern()
```

```java
native String intern();
/*
Returns a canonical representation for the string object.
A pool of strings, initially empty, is maintained privately by the class String.

When the intern method is invoked, if the pool already contains a string equal to
this String object as determined by the equals(Object) method, then the string from
the pool is returned. Otherwise, this String object is added to the pool and a
reference to this String object is returned.

It follows that for any two strings s and t, s.intern() == t.intern() is true if and
only if s.equals(t) is true.

All literal strings and string-valued constant expressions are interned. String
literals are defined in section 3.10.5 of the The Java™ Language Specification.

Returns:
a string that has the same contents as this string, but is guaranteed to be from
a pool of unique strings.
*/
```



## 13.5 格式化输出

### 13.5.1 printf()

+ `C` 语言风格

### 13.5.2 System.out.format()

+ 可以用于 `PrintStream/PrintWriter/System.out`
+ 模仿自 `C` 语言的 `printf()`
    + 使用上和 `printf()` 等价

### 13.5.3 Formatter 类

+ [TestFormatOutput](TestFormatOutput.java)

```java
import java.util.Formatter;
import java.io.PrintStream;
public class TestFormatOutput {
    // 设计技巧
    private Formatter format;

    public static void main(String...args){
        new TestFormatOutput().run(System.out);
    }

    public void run(PrintStream p){
        char c = '\u03A3';
        int i = 10;
        float f = 1.0f;
        System.out.println(c + " " + i + " " + f);
        System.out.printf("%c %d %.1f\n", c, i, f);
        System.out.format("%c %d %.1f\n", c, i, f);
        // java.util.Formatter
        this.format = new Formatter(p);
        this.format.format("%c %d %.1f\n", c, i, f);
    }
}
```

```output
Σ 10 1.0
Σ 10 1.0
Σ 10 1.0
Σ 10 1.0
```



### 13.5.4 格式化说明符

```java
"%[argument_index$][flags][width][.precision]coversion"
```

+ `precision`
    + `String`：最大字符个数
    + `float/double`：小数位数

```java
// 默认右对齐,'-'左对齐
System.out.printf("\"%10.6f\"\n", b);       // "  4.440000"
System.out.printf("\"%-10.6f\"\n", b);      // "4.440000  "
System.out.printf("\"%010.6f\"\n", b);      // "004.440000"

// hashCode
FE fe = new FE("a");
System.out.format("%h\n", fe);              // 232204a1
System.out.println(fe);                     // FE@232204a1

// %b:false <=> null
System.out.printf("%b\n", 0);               // true
```

+ [TestFormatOutput2](TestFormatOutput2.java)



### 13.5.5 Formatter 转换

| 字符  |           含义           |
| :---: | :----------------------: |
| **d** |    **整型（10进制）**    |
| **c** | **Unicode 字符（char）** |
| **b** |       **boolean**        |
| **s** |        **String**        |
| **f** |   **浮点数（10进制）**   |
| **e** | **浮点数（科学计数法）** |
| **x** |    **整数（16进制）**    |
| **h** |  **hashcode（16进制）**  |
| **%** |       **字符'%'**        |


### 13.5.6 String.format()

+ 返回一个 `String` 对象
+ `dump` 工具，`16` 进制



## 13.6 正则表达式

+ `String/StringBuffer/StringTokenizer`

### 13.6.1 基础

+ **有特殊用法的字符都需要转义**

```java
?       // 0 个或者 1 个
+       // 1 个或者多个
\\\\    // 正常反斜线'\'(String中有转义)
d       // 数字
()      // 分组
|       // 或
```

 ```java
PrintStream p = System.out;
p.println("\\".matches("\\\\?"));            // true
p.println("\\abc".matches("\\\\?"));         // false
p.println("123".matches("(\\\\?)|(\\d+)"));  // true
 ```

```java
String[] split(String regex, int limit);
```



### 13.6.2 创建正则表达式

+ [Pattern.html](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html)
+ **字符**

| 字符 | 含义 |
| :-----------: | :----------------------------------------------------------- |
| *x*           | 指定字符 *x*                                        |
| `\\`          | 反斜杠                                   |
| `\0`*n*       | 八进制表示为 `0`*n* 的字符 (0 `<=` *n* `<=` 7) |
| `\0`*nn*      | 八进制表示为 `0`*nn* 的字符 (0 `<=` *n* `<=` 7) |
| `\0`*mnn*     | 八进制表示为 `0`*mnn* 的字符 (0 `<=` *m* `<=` 3, 0 `<=` *n* `<=` 7) |
| `\x`*hh*      | 十六进制表示为 `0`*hh* 的字符 |
| `\u`*hhhh*    | 十六进制表示为 `0`*hhhh* 的字符 |
| `\x`*{h...h}* | 十六进制表示为 `0`*{h...h*} 的字符(`U+0000`<= `0x`*h...h* <=`U+10FFFF`) |
| `\t`          | (`'\u0009'`) 制表符 Tab               |
| `\n`          | (`'\u000A'`) 换行            |
| `\r`          | (`'\u000D'`) 回车            |
| `\f`          | (`'\u000C'`) 换页                      |
| `\a`          | The alert (bell) character (`'\u0007'`)                      |
| `\e`          | The escape character (`'\u001B'`)                            |
| `\c`*x*       | 匹配 *x* 指示的控制字符。例如，`\cM` 匹配 `Control-M` 或回车符。*x* 的值必须在 `A-Z` 或 `a-z` 之间。如果不是这样，则假定 `c` 就是`'c'`字符本身。 |

+ **字符类**

| 字符类 | 含义 |
| --------------- | ------------------------------------------------------------ |
| `[abc]`         | `a`, `b`, or `c` (simple class)                              |
| `[^abc]`        | Any character except `a`, `b`, or `c` (negation)             |
| `[a-zA-Z]`      | `a` through `z` or `A` through `Z`, inclusive (range)        |
| `[a-d[m-p]]`    | `a` through `d`, or `m` through `p`: `[a-dm-p]` (union)      |
| `[a-z&&[def]]`  | `d`, `e`, or `f` (intersection)                              |
| `[a-z&&[^bc]]`  | `a` through `z`, except for `b` and `c`: `[ad-z]` (subtraction) |
| `[a-z&&[^m-p]]` | `a` through `z`, and not `m` through `p`: `[a-lq-z]`(subtraction) |

+ **预定义字符**

| Predefined character classes |                                                              |
| ---------------------------- | ------------------------------------------------------------ |
| `.`                          | Any character (may or may not match [line terminators](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#lt)) |
| `\d`                         | A digit: `[0-9]`                                             |
| `\D`                         | A non-digit: `[^0-9]`                                        |
| `\h`                         | A horizontal whitespace character: `[ \t\xA0\u1680\u180e\u2000-\u200a\u202f\u205f\u3000]` |
| `\H`                         | A non-horizontal whitespace character: `[^\h]`               |
| `\s`                         | A whitespace character: `[ \t\n\x0B\f\r]`                    |
| `\S`                         | A non-whitespace character: `[^\s]`                          |
| `\v`                         | A vertical whitespace character: `[\n\x0B\f\r\x85\u2028\u2029]` |
| `\V`                         | A non-vertical whitespace character: `[^\v]`                 |
| `\w`                         | A word character: `[a-zA-Z_0-9]`                             |
| `\W`                         | A non-word character: `[^\w]`                                |

+ **POSIX character classes (US-ASCII only)**

| **POSIX character classes (US-ASCII only)** |                                                        |
| ------------------------------------------- | ------------------------------------------------------ |
| `\p{Lower}`                                 | A lower-case alphabetic character: `[a-z]`             |
| `\p{Upper}`                                 | An upper-case alphabetic character:`[A-Z]`             |
| `\p{ASCII}`                                 | All ASCII:`[\x00-\x7F]`                                |
| `\p{Alpha}`                                 | An alphabetic character:`[\p{Lower}\p{Upper}]`         |
| `\p{Digit}`                                 | A decimal digit: `[0-9]`                               |
| `\p{Alnum}`                                 | An alphanumeric character:`[\p{Alpha}\p{Digit}]`       |
| `\p{Punct}`                                 | Punctuation: One of `!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~` |
| `\p{Graph}`                                 | A visible character: `[\p{Alnum}\p{Punct}]`            |
| `\p{Print}`                                 | A printable character: `[\p{Graph}\x20]`               |
| `\p{Blank}`                                 | A space or a tab: `[ \t]`                              |
| `\p{Cntrl}`                                 | A control character: `[\x00-\x1F\x7F]`                 |
| `\p{XDigit}`                                | A hexadecimal digit: `[0-9a-fA-F]`                     |
| `\p{Space}`                                 | A whitespace character: `[ \t\n\x0B\f\r]`              |

+ 不常用

```java
java.lang.Character classes (simple java character type)

\p{javaLowerCase}   Equivalent to java.lang.Character.isLowerCase()
\p{javaUpperCase}   Equivalent to java.lang.Character.isUpperCase()
\p{javaWhitespace}  Equivalent to java.lang.Character.isWhitespace()
\p{javaMirrored}    Equivalent to java.lang.Character.isMirrored()
```

```java
Classes for Unicode scripts, blocks, categories and binary properties

\p{IsLatin}         A Latin script character (script)
\p{InGreek}         A character in the Greek block (block)
\p{Lu}              An uppercase letter (category)
\p{IsAlphabetic}    An alphabetic character (binary property)
\p{Sc}              A currency symbol
\P{InGreek}         Any character except one in the Greek block (negation)
[\p{L}&&[^\p{Lu}]]  Any letter except an uppercase letter (subtraction)
```

+ **边界匹配**

| Boundary matchers |                                                              |
| ----------------- | ------------------------------------------------------------ |
| `^`               | The beginning of a line                                      |
| `$`               | The end of a line                                            |
| `\b`              | A word boundary                                              |
| `\B`              | A non-word boundary                                          |
| `\A`              | The beginning of the input                                   |
| `\G`              | The end of the previous match                                |
| `\Z`              | The end of the input but for the final [terminator](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#lt), if any |
| `\z`              | The end of the input                                         |

| Linebreak matcher |                                                              |
| ----------------- | ------------------------------------------------------------ |
| `\R`              | Any Unicode linebreak sequence, is equivalent to `\u000D\u000A|[\u000A\u000B\u000C\u000D\u0085\u2028\u2029]` |

```java
Logical operators
| XY      | X followed by Y           |
| X|Y     | Either X or Y             |
| (X)     | X, as a [capturing group] |
```


| Back references |                                                              |
| --------------- | ------------------------------------------------------------ |
| `\`*n*          | Whatever the *n*th [capturing group](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#cg) matched |
| `\`*k*<*name*>  | Whatever the [named-capturing group](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#groupname) "name" matched |

| Quotation |                                               |
| --------- | --------------------------------------------- |
| `\`       | Nothing, but quotes the following character   |
| `\Q`      | Nothing, but quotes all characters until `\E` |
| `\E`      | Nothing, but ends quoting started by `\Q`     |

| Special constructs (named-capturing and non-capturing) |                                                              |
| ------------------------------------------------------ | ------------------------------------------------------------ |
| `(?`*X*`)`                                             | *X*, as a named-capturing group                              |
| `(?:`*X*`)`                                            | *X*, as a non-capturing group                                |
| `(?idmsuxU-idmsuxU) `                                  | Nothing, but turns match flags [i](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#CASE_INSENSITIVE) [d](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#UNIX_LINES) [m](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#MULTILINE) [s](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#DOTALL) [u](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#UNICODE_CASE) [x](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#COMMENTS) [U](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#UNICODE_CHARACTER_CLASS) on - off |
| `(?idmsux-idmsux:`*X*`)`                               | *X*, as a [non-capturing group](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#cg) with the given flags [i](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#CASE_INSENSITIVE) [d](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#UNIX_LINES) [m](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#MULTILINE) [s](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#DOTALL) [u](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#UNICODE_CASE) [x](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#COMMENTS) on - off |
| `(?=`*X*`)`                                            | *X*, via zero-width positive lookahead                       |
| `(?!`*X*`)`                                            | *X*, via zero-width negative lookahead                       |
| `(?<=`*X*`)`                                           | *X*, via zero-width positive lookbehind                      |
| `(?<!`*X*`)`                                           | *X*, via zero-width negative lookbehind                      |
| `(?>`*X*`)`                                            | *X*, as an independent, non-capturing group                  |

### 13.6.3 量词

+ 量词描述一个模式吸收文本的方式
+ **Greedy**  贪婪型
    + 尽可能多的匹配字符（带回溯）
+ **Reluctant**  勉强型 `?`
    + 尽可能少的匹配字符
+ **Possessive**  占有型 `+`
    + 尽可能多的匹配字符（不带回溯）
    + 现在只有 `Java` 使用

```java
String text = "xfooxxxxxxfoo";
p.println(text.matches(".*foo"));                // true(Greedy)
p.println(text.matches(".*?foo"));               // true(Reluctant)
p.println(text.matches(".*+foo"));               // false(Positive)
```

| Greedy quantifiers |                                               |
| ------------------ | --------------------------------------------- |
| *X*`?`             | *X*, once or not at all                       |
| *X*`*`             | *X*, zero or more times                       |
| *X*`+`             | *X*, one or more times                        |
| *X*`{`*n*`}`       | *X*, exactly *n* times                        |
| *X*`{`*n*`,}`      | *X*, at least *n* times                       |
| *X*`{`*n*`,`*m*`}` | *X*, at least *n* but not more than *m* times |

| Reluctant quantifiers |                                               |
| --------------------- | --------------------------------------------- |
| *X*`??`               | *X*, once or not at all                       |
| *X*`*?`               | *X*, zero or more times                       |
| *X*`+?`               | *X*, one or more times                        |
| *X*`{`*n*`}?`         | *X*, exactly *n* times                        |
| *X*`{`*n*`,}?`        | *X*, at least *n* times                       |
| *X*`{`*n*`,`*m*`}?`   | *X*, at least *n* but not more than *m* times |

| Possessive quantifiers |                                               |
| ---------------------- | --------------------------------------------- |
| *X*`?+`                | *X*, once or not at all                       |
| *X*`*+`                | *X*, zero or more times                       |
| *X*`++`                | *X*, one or more times                        |
| *X*`{`*n*`}+`          | *X*, exactly *n* times                        |
| *X*`{`*n*`,}+`         | *X*, at least *n* times                       |
| *X*`{`*n*`,`*m*`}+`    | *X*, at least *n* but not more than *m* times |



### 13.6.4 CharSequence

```java
public interface CharSequence {
    int length();
    char charAt(int index);
    CharSequence subSequence(int start, int end);
    public String toString();
    // @since 1.8
    public default IntStream chars() {...}
    // @since 1.8
    public default IntStream codePoints() {...}
}
```

+ `CharBuffer/String/StringBuilder/StringBuffer`
+ 多数正则表达式都支持 `CharSequence`



### 13.6.5 Pattern Matcher

+ `java.util.regex.*;`
+ [TestRegex](TestRegex.java)
+ 将多个空格替换成一个空格

```java
s = s.replaceAll(" {2,}", " ");
```

+ `Matcher` 方法

```java
boolean find();
boolean find(int start);
boolean lookingAt();
boolean matches();
boolean hitEnd();
/*
* If hitEnd is true, and a match was found, then more input
* might cause a different match to be found.
* If hitEnd is true and a match was not found, then more
* input could cause a match to be found.
* If hitEnd is false and a match was found, then more input
* will not change the match.
* If hitEnd is false and a match was not found, then more
* input will not cause a match to be found.
*/
```



+ `Pattern` 标记
| 标记（int） | 含义 |
| ---- | ---- |
| Pattern.CANON_EQ      | 考虑规范等价性 |
| Pattern.CASE_INSENSITIVE  | 大小写不敏感（`ASCII`） |
| Pattern.COMMENTS      | 忽略空格符和 `#` 注释行 |
| Pattern.DOTALL        | `.`匹配所有字符，包括行终结符 |
| Pattern.MULTILINE     | 多行模式，`^$`还匹配一行的开始和结束 |
| Pattern.UNICODE_CASE  | 将大小写不敏感的范围扩展至 `unicode` |
| Pattern.UNIX_LINES    | `.^$` 的行为中只识别行终结符`\n` |



### 13.6.5 split()

```java
String[] split(String regex, int limit);
```



### 13.6.6 替换操作

```java
String replaceFirst(String regex, String replacement); // 替换第一次匹配上的
String replaceAll(String regex, String replacement); // 全替换
String replace(CharSequence target, CharSequence replacement); // 全替换,Pattern

// Matcher
// 对匹配上的字符、剩余字符进行操作
public Matcher appendReplacement(StringBuffer sb, String replacement);
public StringBuffer appendTail(StringBuffer sb);
```



### 13.6.7 reset()

```java
public Matcher reset(CharSequence input);
public Matcher reset();
```



## 13.7 扫描输入

+ `Scanner` 类

```java
import java.util.Scanner;
```

+ `Scanner` 默认输入结束后会抛出 `IOException`
    + 因此会自动吞掉，不需要 `try...catch...`
    + 可以通过如下方法检查

```java
public IOException ioException() {
    return lastException;
}
```



### 13.7.1 Scanner 定界符

+ 默认空白字符

```java
public Scanner useDelimiter(Pattern pattern) {
    delimPattern = pattern;
    return this;
}
public Scanner useDelimiter(String pattern) {
    delimPattern = patternCache.forName(pattern);
    return this;
}
```



### 13.7.2 用正则表达式扫描

+ 注意正则表示式中不能含有分词，因为 `Scanner` 是针对下一个输入分词进行匹配
+ [TestScanner](TestScanner.java)



## 13.8 StringTokenizer

+ 被正则表达式和 `Scanner` 取代了，功能不够强大

```java
// import java.util.StringTokenizer;
String input = "StringTokennizer is deprecated!";
StringTokenizer stoke = new StringTokenizer(input);
while(stoke.hasMoreElements())
    System.out.print(stoke.nextToken() + " ");
System.out.println();
System.out.println(Arrays.toString(input.split(" ")));
Scanner scanner = new Scanner(input);
while(scanner.hasNext())
    System.out.print(scanner.next() + " ");
```

