// ʾ������

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
        // readObject ��֪����������ʲô,��˱���ת��
        // ����ת�����ֻᱨ����
        // ����: [unchecked] δ������ת��
        
        // 2 warning
        List<Apple> fruit2 = List.class.cast(in.readObject());
        
        // 3 warning
        List<Apple> fruit3 
            = (List<Apple>) List.class.cast(in.readObject());
    }
}
