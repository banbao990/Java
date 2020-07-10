/**
 * @author banbao
 * @comment �޸���ʾ������
 */

import java.util.*;
public class TestCollection{
    public static void print(Collection<String> c){
        for(String s : c)
            System.out.println(c + ":" + s);
    }
    
    public static void main(String...args){
        String[] strings = {"d", "e"};
        // ��ӷ�ʽ 1
        Collection<String> collection1 = 
            new ArrayList<>(Arrays.asList(strings));
        // ��ӷ�ʽ 2
        Collection<String> collection2 = new ArrayList<>();
        collection2.addAll(Arrays.asList(strings));
        // ���췽ʽ 3
        Collection<String> collection3 = Arrays.asList(strings);
        collection1.add("f");
        collection2.add("f");
        // collection3.add("f");
        // Exception in thread "main" java.lang.UnsupportedOperationException
        // ��ʽ 3 �ĵײ�������,�������޸Ĵ�С
        print(collection1);
        print(collection2);
        print(collection3);
    }
}