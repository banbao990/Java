/**
 * @author banbao
 * @comment �޸���ʾ������
 */ 

// ���������ģʽ

// Selector �ӿ�
interface Selector{
    boolean end();
    Object current();
    void next();
}

// ��ͼʹ�õ����� Selector
public class Sequence{
    private Object[] items;
    private int next = 0;
    public Sequence(int size){
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
    // ��ȡ������
    public Selector selector(){
        return new SequenceSelector();
    }
    // main
    public static void main(String...args){
        Sequence sequence = new Sequence(5);
        // ���Ԫ��
        for(int i = 0;i < 5; ++i){
            sequence.add(i);
        }
        // ��ȡ������
        Selector selector = sequence.selector();
        // ʹ�õ�����
        while(!selector.end()){
            System.out.println(selector.current());
            selector.next();
        }
    }
}