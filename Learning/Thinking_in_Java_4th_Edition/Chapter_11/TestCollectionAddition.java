/**
 * @author banbao
 * @comment 修改自示例代码
 */
 
import java.util.*;
public class TestCollectionAddition{
    public static void print(Collection<String> c){
        for(String s : c)
            System.out.println(s);
    }
    
    public static void main(String...args){
       Collection<String> collection = new ArrayList<>();
        // 添加方式 1
        collection.add("a");
        // 添加方式 2
        Collections.addAll(collection, "b", "c");
        // 添加方式 3
        String[] strings = {"d", "e"};
        collection.addAll(Arrays.asList(strings));
        // 添加方式 4
        collection.addAll(collection);
        // 打印
        print(collection);
    }
}
/*
 * output
 * 
 * a
 * b
 * c
 * d
 * e
 * a
 * b
 * c
 * d
 * e
 */