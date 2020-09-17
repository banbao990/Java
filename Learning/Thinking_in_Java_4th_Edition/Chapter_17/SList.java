/**
 * @author banbao
 * 单向链表(练习8),空头结点
 */
import java.util.*;
 
interface SListIterator<T> extends Iterator<T> {
    public void add(T t);
    public void set(int pos) throws RuntimeException;
}

public class SList<T> implements Iterable<T>{
    private class Node {
        Node next;
        T value;
        Node() { this(null, null); }
        Node(Node next, T value) { 
            this.next = next;
            this.value = value; 
        }
    }
    
    // List
    private int size;
    private Node head;
    SList() { head = new Node();}
    @Override
    public String toString() {
        if(this.size == 0) return "[]";
        Node now = head.next; // head is empty
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        while(now != null) {
            sb.append("    ");
            sb.append(now.value);
            sb.append("\n");
            now = now.next;
        }
        sb.append("]\n");
        return sb.toString();
    }
    
    @Override
    public SListIterator<T> iterator(){
        return new SListIterator<T>(){
            private Node now = head; // now.next 表示当前位置
            @Override
            public boolean hasNext(){
                return now.next != null && now.next.next != null;
            }
            
            // 返回当前位置(now.next)
            @Override
            public T next(){
                T ret = null;
                if(now.next != null) { 
                    now = now.next;
                    ret = now.value;
                }
                return ret;
            }
            @Override
            public void remove(){
                if(now.next != null) {
                    now.next = now.next.next;
                    -- size;
                }
            }
            @Override
            public void add(T value) {
                Node newNode = new Node(null, value);
                // head
                if(size == 0 && now.value == null) {
                    now.next = newNode;
                    now = now.next;
                    ++ size;
                    return;
                }
                newNode.next = now.next;
                now.next = newNode;
                ++ size;
            }
            @Override
            public void set(int pos) throws RuntimeException {
                if(pos >= size || pos < 0) {
                    throw new IndexOutOfBoundsException(
                        "index:" + pos + ", size:" + size
                    );
                }
                now = head;
                while(pos-- > 0) {
                    now = now.next;
                }
            }
        };
    }
    
    // test
    public static void main(String...args) {
        SList<String> sl = new SList<>();
        SListIterator<String> sli = sl.iterator();
        System.out.println(sl);
        sli.add("first");
        sli.add("second");
        System.out.println(sl);
        sli.set(1);
        sli.add("1.5");
        System.out.println(sl);
        sli.set(0);
        sli.add("0.5");
        System.out.println(sl);
        sli.set(1);
        sli.remove();
        System.out.println(sl);
    }
}


/* Output
[]
[
    first
    second
]

[
    first
    1.5
    second
]

[
    0.5
    first
    1.5
    second
]

[
    0.5
    1.5
    second
]
*/
