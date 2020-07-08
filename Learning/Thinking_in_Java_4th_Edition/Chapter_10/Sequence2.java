/**
 * @author banbao
 */ 

// ���������ģʽ

// Selector �ӿ�
interface Selector{
    boolean end();
    Object current();
    void next();
}

// ReverseSelector �ӿ�
interface ReverseSelector{
    boolean begin();
    Object current();
    void next();
}

// ��ͼʹ�õ����� Selector
public class Sequence2{
    private Object[] items;
    private int next = 0;
    public Sequence2(int size){
        items = new Object[size];
    }

    // ���Ԫ��
    public void add(Object x){
        if (next < items.length){
            items[next++] = x;
        }
    }

    // �ڲ���
    private class SequenceSelector implements Selector{
        private int i = 0;
        // ʵ�ֽӿ��е� 3 ������
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
    
    // ��ȡ���������
    public ReverseSelector reverseSelector(){
        // ������
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
    
    // ��ȡ������
    public Selector selector(){
        return new SequenceSelector();
    }
    
    // main
    public static void main(String...args){
        Sequence2 sequence = new Sequence2(5);
        // ���Ԫ��
        for(int i = 0;i < 5; ++i){
            sequence.add(i);
        }
        System.out.println("selector");
        // ��ȡ������
        Selector selector = sequence.selector();
        while(!selector.end()){
            System.out.println(selector.current());
            selector.next();
        }
        System.out.println("reverse selector");
        // ��ȡ���������
        ReverseSelector reverseSelector = sequence.reverseSelector();
        while(!reverseSelector.begin()){
            System.out.println(reverseSelector.current());
            reverseSelector.next();
        }
    }
}