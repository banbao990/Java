// 示例代码

import java.io.*;
import java.util.*;

class Apple{}
public class NeedCasting {
    // @SuppressWarnings("unchecked")
    public void f(String[] args) throws Exception {
        // 1 warning
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(args[0]));
        List<Apple> fruit = (List<Apple>)in.readObject();
        // readObject 不知道它读的是什么,因此必须转型
        // 但是转型了又会报警告
        // 警告: [unchecked] 未经检查的转换
        
        // 2 warning
        List<Apple> fruit2 = List.class.cast(in.readObject());
        
        // 3 warning
        List<Apple> fruit3 
            = (List<Apple>) List.class.cast(in.readObject());
    }
}
