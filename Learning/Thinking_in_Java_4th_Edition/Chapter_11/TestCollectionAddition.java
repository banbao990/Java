/**
 * @author banbao
 * @comment �޸���ʾ������
 */
 
import java.util.*;
public class TestCollectionAddition{
    public static void print(Collection<String> c){
        for(String s : c)
            System.out.println(s);
    }
    
    public static void main(String...args){
       Collection<String> collection = new ArrayList<>();
        // ��ӷ�ʽ 1
        collection.add("a");
        // ��ӷ�ʽ 2
        Collections.addAll(collection, "b", "c");
        // ��ӷ�ʽ 3
        String[] strings = {"d", "e"};
        collection.addAll(Arrays.asList(strings));
        // ��ӷ�ʽ 4
        collection.addAll(collection);
        // ��ӡ
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