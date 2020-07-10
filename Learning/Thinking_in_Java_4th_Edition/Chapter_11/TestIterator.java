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

public class TestIterator{
    
    public static void display(Iterator<FE> iterator){
        while(iterator.hasNext()){ // hasNext
            System.out.println(iterator.next()); // next
        }
    }
    
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
        Iterator<FE> iterator = set.iterator(); // iterator
        display(iterator);
        // 2
        iterator = set.iterator();
        // iterator.remove(); 
        // java.lang.IllegalStateException
        while(iterator.hasNext()){
            FE fe = iterator.next();
            if(fe.equals("FE3")){
                // remove 的元素是上一个 next 返回的元素
                iterator.remove(); // remove
            }
        }
        System.out.println(set);
    }
}

/* 
 * [FE1, FE2, FE3, FE4, FE5, FE6]
 * FE1
 * FE2
 * FE3
 * FE4
 * FE5
 * FE6
 * [FE1, FE2, FE4, FE5, FE6]
*/