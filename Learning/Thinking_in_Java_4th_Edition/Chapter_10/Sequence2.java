/**
 * @author banbao
 */ 

// 迭代器设计模式

// Selector 接口
interface Selector{
    boolean end();
    Object current();
    void next();
}

// ReverseSelector 接口
interface ReverseSelector{
    boolean begin();
    Object current();
    void next();
}

// 试图使用迭代器 Selector
public class Sequence2{
    private Object[] items;
    private int next = 0;
    public Sequence2(int size){
        items = new Object[size];
    }

    // 添加元素
    public void add(Object x){
        if (next < items.length){
            items[next++] = x;
        }
    }

    // 内部类
    private class SequenceSelector implements Selector{
        private int i = 0;
        // 实现接口中的 3 个函数
        public boolean end(){
            return i == items.length;
        }
        public Object current(){
            return items[i];
        }
        public void next(){
            if(i < items.length){
                ++i;
            }
        }
    }
    
    // 获取反向迭代器
    public ReverseSelector reverseSelector(){
        // 匿名类
        return new ReverseSelector(){
            private int i = items.length - 1;
            public boolean begin(){
                return i == -1;
            }
            public Object current(){
                return items[i];
            }
            public void next(){
                if(i > -1){
                    --i;
                }
            }
        };
    }
    
    // 获取迭代器
    public Selector selector(){
        return new SequenceSelector();
    }
    
    // main
    public static void main(String...args){
        Sequence2 sequence = new Sequence2(5);
        // 添加元素
        for(int i = 0;i < 5; ++i){
            sequence.add(i);
        }
        System.out.println("selector");
        // 获取迭代器
        Selector selector = sequence.selector();
        while(!selector.end()){
            System.out.println(selector.current());
            selector.next();
        }
        System.out.println("reverse selector");
        // 获取反向迭代器
        ReverseSelector reverseSelector = sequence.reverseSelector();
        while(!reverseSelector.begin()){
            System.out.println(reverseSelector.current());
            reverseSelector.next();
        }
    }
}
