/**
 * @author banbao
 * @comment 修改自"Thinking in Java"示例代码
 * 利用反射展示类的各种方法(包括继承自父类的方法)
 */

import java.lang.reflect.*;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

public class ShowMethods {
    // 提示信息
    private static String usage =
        "usage:(FULL NAME)\n"
        + "    (1)java ShowMethods ClassName\n"
        + "    (2)java ShowMethods ClassName returnType";
    private static Pattern p = Pattern.compile("\\w+\\.");
    private static boolean oneArgs = true;
    private static String returnType = "";
    // main
    public static void main(String[] args) {
        // args ERROR
        if(args.length != 1 && args.length != 2) {
            System.err.println(usage);
            System.exit(0);
        }
        List<String> staticMethods = new ArrayList<>(); 
        List<String> nonStaticMethods = new ArrayList<>();
        List<String> constructors = new ArrayList<>();
        try {
            // Class
            Class<?> c = Class.forName(args[0]);
            // methods, constructors
            Method[] methods = c.getMethods();
            Constructor[] ctors = c.getConstructors();
            for(Method method : methods) {
                    // 将匹配上的都替换为 "", 去除修饰符(java.lang.String -> String)
                String m = p.matcher(method.toString()).replaceAll("");
                if(m.indexOf("static") != -1) {
                    staticMethods.add(m);
                } else {
                    nonStaticMethods.add(m);
                }
            }
            for(Constructor ctor : ctors) {
                constructors.add(p.matcher(ctor.toString()).replaceAll(""));
            }
            // 2 args
            oneArgs = (args.length == 1);
            if(!oneArgs) { returnType = args[1]; }
            print("static method", staticMethods);
            print("non-static method", nonStaticMethods);
            print("constructor", constructors);
        } catch(ClassNotFoundException e) {
            System.out.println("No such class: " + e);
        }
    }
    
    private static void print(String modifier, List<String> list) {
        System.out.println("// ~~~~~" + modifier + "~~~~~ //");
        for(String method : list) {
            if(oneArgs || method.indexOf(returnType) == -1) {
                System.out.println(method);
            }
        }
        System.out.println();
    }
}
