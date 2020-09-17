/**
 * @author banbao
 * @comment 正则表达式
 */
import java.io.PrintStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TestRegex{
    // System.out
    private PrintStream p;
    TestRegex(){
        p = System.out;
    }

    // main
    public static void main(String...args){
        new TestRegex().run();
    }

    // run
    public void run(){
        p.println("~~~~~test 1~~~~~");
        p.println("\\".matches("\\\\?"));               // true
        p.println("\\abc".matches("\\\\?"));            // false
        p.println("123".matches("\\\\?|\\d+"));         // true
        p.println("\n".matches("\\n"));                 // true
        p.println("\n".matches("\\u000A"));             // true
        p.println("\r".matches("\\cM"));                // true
        p.println("a".matches("\\p{Lower}"));           // true

        // Greedy, Reluctant, Positive
        String[] patterns = {
            ".*foo",  // true(Greedy)
            ".*?foo", // true(Reluctant)
            ".*+foo"  // false(Positive)
        };
        p.println("~~~~~test 2~~~~~");
        String text = "xfooxxxxxxfoo";
        for(String s : patterns){
            p.println(text.matches(s)); // T,T,F
        }
        
        // 使用 Pattern Matcher 测试匹配情况
        p.println("~~~~~test 3~~~~~");
        for(String s : patterns){
            matchAndPrint(s, text, false);
        }
        
        // 将多个空格替换成一个空格
        p.println("~~~~~test 4~~~~~");
        text = "\"1 2  3   4    5     6\"";
        p.println(text.replaceAll(" {2,}", " "));  // "1 2 3 4 5 6"
        
        // 测试非捕获组特殊结构
        p.println("~~~~~test 5~~~~~");
        patterns = new String[] {
            "100(?=z)",   // 匹配100,要求后面紧跟z
            "100(?!z)",   // 匹配100,要求后面不能紧跟z
            "(?<=z)100",  // 匹配100,要求前面紧跟z
            "(?<!z)100"   // 匹配100,要求前面不能紧跟z
        };
        text = "a100z100a";
        for(String s : patterns){
            matchAndPrint(s, text, false);
        }
        
        // 捕获组 vs 非捕获组
        // group(0) 表示整个匹配
        p.println("~~~~~test 6~~~~~");
        patterns = new String[] {
            "(z)100",         // 捕获z(能够通过序号索引)
            "(?<z>z)100",     // 捕获z(能够通过序号/名称索引)
            "(?:z)100",       // 不捕获z
        };
        text = "a100z100a";
        for(String s : patterns){
            matchAndPrint(s, text, true);
        }
        
        // Matcher 方法
        p.println("~~~~~test 6~~~~~");
        testMatcherMethod();
        
        // Pattern 标记
        p.println("~~~~~test 7~~~~~");
        testPatternflag();
        
        // appendReplacement,appendTail
        p.println("~~~~~test 8~~~~~");
        testReplacement();
        
        // reset
        p.println("~~~~~test 9~~~~~");
        testReset();
    }
    
    public void testReset(){
        String text = "ABCDEFG";
        Pattern pat = Pattern.compile("[AEIOU]");
        Matcher m = pat.matcher(text);
        int count = 0; 
        while(m.find()){
            p.println((count++) + ":" + m.group());
            if(count == 1) {
                m.reset();
            }
        }
        text = "HIJKLMN";
        m.reset(text);
        while(m.find()){
            p.println((count++) + ":" + m.group());
        }
    }
    
    public void testReplacement(){
        Pattern pat = Pattern.compile("[AEIOU]");
        Matcher m = pat.matcher("ABCDEFG");
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            m.appendReplacement(sb, m.group().toLowerCase());
        }
        m.appendTail(sb);
        p.println(sb);
    }
    
    public void testPatternflag(){
        // flags
        int[] flags = {
            Pattern.CANON_EQ,
            Pattern.CASE_INSENSITIVE,
            Pattern.COMMENTS,
            Pattern.DOTALL,
            Pattern.MULTILINE,
            Pattern.UNICODE_CASE,
            Pattern.UNIX_LINES,
        };
        // CANON_EQ
        /* http://www.java2s.com/Code/JavaAPI/java.util.regex/PatternCANONEQ.htm 
         * CanonEqDemo - show use of Pattern.CANON_EQ, by comparing varous ways of
         * entering the Spanish word for "equal" and see if they are considered equal by
         * the RE-matching engine.
         *
         * égal matches input égal
         * égal matches input e?gal
         * égal does not match input eˊgal
         * égal does not match input e'gal
         * égal does not match input e?gal
         */
        String pattStr = "\u00e9gal"; // gal
        String[] input = { 
            "\u00e9gal",   // gal - this one had better match :-)
            "e\u0301gal",  // e + "Combining acute accent"
            "e\u02cagal",  // e + "modifier letter acute accent"
            "e'gal",       // e + single quote
            "e\u00b4gal",  // e + Latin-1 "acute"
        };
        boolean[] t = {true, false};
        for(boolean tt : t){
            // 默认 flag = 0
            Pattern pattern = Pattern.compile(pattStr, tt ? Pattern.CANON_EQ : 0);
            for (int i = 0; i < input.length; i++) {
                if (pattern.matcher(input[i]).matches()) {
                    p.println(pattStr + " matches input " + input[i]);
                } else {
                    p.println(pattStr + " does not match input " + input[i]);
                }
            }
        }
        // Pattern.MULTILINE
        p.println("~~~~~~~~~~");
        pattStr = "^a"; 
        String inputStr = "aaa\naaa";
        for(boolean tt : t){
            // 默认 flag = 0
            Pattern pattern = Pattern.compile(pattStr, tt ? Pattern.MULTILINE : 0);
            Matcher m = pattern.matcher(inputStr);
            p.println(tt?"Pattern.MULTILINE":"NO FLAGS!");
            while(m.find()) {
                p.println(m.group() + ":start(" + m.start() + 
                    "),end(" + m.end() + ")");
            }
        }
    }
        
    public void testMatcherMethod(){
        Pattern pattern = Pattern.compile("(?:a|b)(?:b|c)c"); // static
        Matcher m = pattern.matcher("abcc");
        // m.find()
        // m.find(int start)
        boolean[] t = {true, false};
        for(boolean b : t){
            if(b ? m.find() : m.find(1)) { 
                p.println(
                    m.group() + 
                    ":start(" + m.start() + 
                    "),end(" + m.end() + ")"
                );
            } else { p.println("false"); }
        }
        // 看开头一段能否匹配上
        p.println("m.lookingAt():"); 
        if(m.lookingAt()){
            p.println(m.group() + ":start(" + m.start() 
                + "),end(" + m.end() + ")");
        } else { p.println("false"); }
        // 看整个能否匹配上
        p.println("m.matches():");
        if(m.matches()){
            p.println(m.group() + ":start(" + m.start() 
                + "),end(" + m.end() + ")");
        } else { p.println("false"); }
    }
    
    public void matchAndPrint(String regex, 
        String matcher, boolean more) {

        Pattern pattern = Pattern.compile(regex); // static
        Matcher m = pattern.matcher(matcher);
        // m.find(int start),从第几个字符开始匹配
        while(m.find()){
            // start(),end()
            p.println(
                m.group() + 
                ":start(" + m.start() + 
                "),end(" + m.end() + ")"
            );
            // 命名组捕获
            try {
                if(more) p.println(m.group("z"));
            } catch (Exception e) {
                p.println("命名捕获失败!");
            }
            // 序号捕获
            try {
                if(more) p.println(m.group(1));
            } catch (Exception e) {
                p.println("序号捕获失败!");
            }
        }
        p.println("~~~~~~"); // end
    }
}
/* Output
~~~~~test 1~~~~~
true
false
true
true
true
true
true
~~~~~test 2~~~~~
true
true
false
~~~~~test 3~~~~~
xfooxxxxxxfoo:start(0),end(13)
~~~~~~
xfoo:start(0),end(4)
xxxxxxfoo:start(4),end(13)
~~~~~~
~~~~~~
~~~~~test 4~~~~~
"1 2 3 4 5 6"
~~~~~test 5~~~~~
100:start(1),end(4)
~~~~~~
100:start(5),end(8)
~~~~~~
100:start(5),end(8)
~~~~~~
100:start(1),end(4)
~~~~~~
~~~~~test 6~~~~~
z100:start(4),end(8)
命名捕获失败!
z
~~~~~~
z100:start(4),end(8)
z
z
~~~~~~
z100:start(4),end(8)
命名捕获失败!
序号捕获失败!
~~~~~~
~~~~~test 6~~~~~
abc:start(0),end(3)
bcc:start(1),end(4)
m.lookingAt():
abc:start(0),end(3)
m.matches():
false
~~~~~test 7~~~~~
égal matches input égal
égal matches input e?gal
égal does not match input eˊgal
égal does not match input e'gal
égal does not match input e?gal
égal matches input égal
égal does not match input e?gal
égal does not match input eˊgal
égal does not match input e'gal
égal does not match input e?gal
~~~~~~~~~~
Pattern.MULTILINE
a:start(0),end(1)
a:start(4),end(5)
NO FLAGS!
a:start(0),end(1)
~~~~~test 8~~~~~
aBCDeFG
~~~~~test 9~~~~~
0:A
1:A
2:E
3:I
*/
