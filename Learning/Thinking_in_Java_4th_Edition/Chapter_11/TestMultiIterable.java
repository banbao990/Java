/** 
 * @author banbao
 */

import java.util.*;

public class TestMultiIterable implements Iterable<Integer>{
    public int a, b, c;
    public TestMultiIterable(int a, int b, int c){
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
    
    // 另外返回一个 Iterable 实现反向 foreach
    public Iterable<Integer> reverse(){
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator(){
                return new Iterator<Integer>(){
                    private int index = 2;
                    @Override
                    public boolean hasNext(){
                        if(index == -1){
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
                        --index;
                        return ret;
                    }
                };
            }
        };
    }
    
    public static void main(String...args){
        TestMultiIterable test = new TestMultiIterable(1, 2, 3);
        for(Integer n : test){
            System.out.println(n);
        }
        for(Integer n : test.reverse()){
            System.out.println(n);
        }
    }
}

/*
 * 1
 * 2
 * 3 
 * 3
 * 2
 * 1
 */
