/**
 * @author banbao
 * @comment 修改自示例代码
 */ 

// 迭代器设计模式

// Selector 接口
interface Selector{
    boolean end();
    Object current();
    void next();
}

// 试图使用迭代器 Selector
public class Sequence{
    private Object[] items;
    private int next = 0;
    public Sequence(int size){
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
    // 获取迭代器
    public Selector selector(){
        return new SequenceSelector();
    }
    // main
    public static void main(String...args){
        Sequence sequence = new Sequence(5);
        // 添加元素
        for(int i = 0;i < 5; ++i){
            sequence.add(i);
        }
        // 获取迭代器
        Selector selector = sequence.selector();
        // 使用迭代器
        while(!selector.end()){
            System.out.println(selector.current());
            selector.next();
        }
    }
}
