/** 
 * @author banbao
 */

import java.util.*;

public class TestArray{
    public static <T> void testIterable(Iterable<T> it){
        System.out.println("OK!");
    }
    
    public static void main(String...args){
        String[] test = "I am a boy!".split(" ");
        for(String n : test){
            System.out.println(n);
        }
        // testIterable(test);
        // 错误: 无法将类 Test中的方法 testIterable应用到给定类型
        testIterable(Arrays.asList(test));
    }
}

/*
 * I
 * am
 * a
 * boy!
 * OK!
 */
