/**
 * @author banbao
 */

import java.util.*;

class FE{
    public String name;
    public FE(String name){ this.name = name; }
    @Override
    public String toString(){
        return this.name;
    }
    public boolean equals(String otherName){
        return this.name.equals(otherName);
    }
}

public class TestListIterator{
    
    public static void main(String...args){
        ArrayList<FE> set = new ArrayList<>();
        set.add(new FE("FE1"));
        set.add(new FE("FE2"));
        set.add(new FE("FE3"));
        set.add(new FE("FE4"));
        set.add(new FE("FE5"));
        set.add(new FE("FE6"));
        System.out.println(set);
        // 1
        System.out.println("Iterator!");
        ListIterator<FE> iterator = set.listIterator(); // iterator
        // ListIterator<FE> iterator = set.listIterator(0); // �ȼ�
        while(iterator.hasNext()){ // hasNext
            System.out.print(iterator.next() + " "); // next
        }
        System.out.println();
        // 2
        iterator = set.listIterator();
        while(iterator.hasNext()){
            FE fe = iterator.next();
            if(fe.equals("FE3")){
                // �Ƴ����һ�����ʵ�Ԫ��
                iterator.remove(); // remove
            }
        }
        System.out.println(set);
        System.out.println("ListIterator!");
        // 3
        while(iterator.hasPrevious()){ // hasPrevious
            System.out.print(iterator.previous() + " "); // previous
        }
        System.out.println();
        // 4
        iterator = set.listIterator(2); // ������ʼλ��Ϊ 2
        int count = 0; // ֤������Ϊ 2 ��Ч
        while(iterator.hasNext()){
            FE fe = iterator.next();
            if(fe.equals(set.get(2).name)){
                count += 100;
            }
            ++ count;
            if(fe.equals("FE6")){
                // �������һ�����ʵ�Ԫ��
                System.out.println("count:" + count);
                iterator.set(new FE("FE66")); // set
                break;
            }
        }
        System.out.println(set);
        // 5
        while(iterator.hasPrevious()){
            FE fe = iterator.previous();
            if(fe.equals("FE2")){
                // �Ƴ����һ�����ʵ�Ԫ��
                iterator.remove(); // remove
            }
            if(fe.equals("FE1")){
                // �������һ�����ʵ�Ԫ��
                iterator.set(new FE("FE11")); // set
                break;
            }
        }
        System.out.println(set);
    }
}

/* 
 * [FE1, FE2, FE3, FE4, FE5, FE6]
 * Iterator!
 * FE1 FE2 FE3 FE4 FE5 FE6 
 * [FE1, FE2, FE4, FE5, FE6]
 * ListIterator!
 * FE6 FE5 FE4 FE2 FE1 
 * count:103
 * [FE1, FE2, FE4, FE5, FE66]
 * [FE11, FE4, FE5, FE66]
 */