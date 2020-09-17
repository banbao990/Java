/**
 * @author banbao
 */

public class MyLinkedStack<T> {
    private class Node {
        T item;
        Node next;
        Node() { 
            this.item = null; 
            this.next = null;
        }
        Node (T item, Node next) {
            this.item = item; 
            this.next = next;
        }
        boolean isEnd() {
            return item == null && next == null; // 哨兵
        }
    }
    private Node top; // 链表头
    MyLinkedStack() {
        this.top = new Node(); // 哨兵
    }
    // top
    public T top() {
        return this.top.item;
    }
    // pop
    public T pop() {
        T ret = top.item;
        if(!this.top.isEnd()) {
            top = top.next;
        }
        return ret;
    }
    // push
    public void push(T item) {
        this.top = new Node(item, this.top);
    }
    // empty
    public boolean empty() {
        return this.top.isEnd();
    }
    // test
    public static void main(String...args) {
        MyLinkedStack<String> stack  = new MyLinkedStack<>();
        for(String s : "test string for class MyLinkedStack".split(" ")){
            stack.push(s);
        }
        while(!stack.empty()) {
            System.out.println(stack.pop());
        }
    }
}
/* Output
MyLinkedStack
class
for
string
test
*/
