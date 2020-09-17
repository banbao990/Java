/** 
 * @author banbao
 */

import java.util.*;

public class TestIterable implements Iterable<Integer>{
    public int a, b, c;
    public TestIterable(int a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public Iterator<Integer> iterator(){
        return new Iterator<Integer>(){
            private int index = 0;
            @Override
            public boolean hasNext(){
                if(index == 3){
                    return false;
                }
                return true;
            }
            @Override
            public Integer next(){
                int ret = -1;
                if(index == 0){
                    ret = a;
                } else if(index == 1){
                    ret = b;
                } else if(index == 2){
                    ret = c;
                } 
                ++index;
                return ret;
            }
        };
    }
    
    public static void main(String...args){
        for(Integer n : new TestIterable(1, 2, 3)){
            System.out.println(n);
        }
    }
}

/*
 * 1
 * 2
 * 3
 */
