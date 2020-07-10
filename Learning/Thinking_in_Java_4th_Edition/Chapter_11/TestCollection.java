/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;
public class TestCollection{
    public static void print(Collection<String> c){
        for(String s : c)
            System.out.println(c + ":" + s);
    }
    
    public static void main(String...args){
        String[] strings = {"d", "e"};
        // 添加方式 1
        Collection<String> collection1 = 
            new ArrayList<>(Arrays.asList(strings));
        // 添加方式 2
        Collection<String> collection2 = new ArrayList<>();
        collection2.addAll(Arrays.asList(strings));
        // 构造方式 3
        Collection<String> collection3 = Arrays.asList(strings);
        collection1.add("f");
        collection2.add("f");
        // collection3.add("f");
        // Exception in thread "main" java.lang.UnsupportedOperationException
        // 方式 3 的底层是数组,不允许修改大小
        print(collection1);
        print(collection2);
        print(collection3);
    }
}