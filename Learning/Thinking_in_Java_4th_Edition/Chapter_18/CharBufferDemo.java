/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.nio.*;
public class CharBufferDemo {
    private static final int BSIZE = 1024;
    private static void printPos(CharBuffer ib, ByteBuffer bb) {
        System.out.println(
            "pos(ib): "+ib.position() 
            + ", pos(bb) : " + bb.position());
    }
    public static void main(String[] args) {
        ByteBuffer bb = ByteBuffer.allocate(BSIZE);
        CharBuffer ib = bb.asCharBuffer();
        printPos(ib, bb); // print position
        // Store an array of int:
        ib.put(new char[]{ 'a','b','c','d' });
        printPos(ib, bb); // print position
        // Absolute location read and write:
        System.out.println(ib.get(3));
        printPos(ib, bb); // print position
        ib.put(3, 'x');
        printPos(ib, bb); // print position
        // Setting a new limit before rewinding the buffer.
        ib.flip();
        printPos(ib, bb); // print position
        while(ib.hasRemaining()) {
            char i = ib.get();
            System.out.println(i);
        }
    }
} /* Output:
pos(ib): 0, pos(bb) : 0
pos(ib): 4, pos(bb) : 0
d
pos(ib): 4, pos(bb) : 0
pos(ib): 4, pos(bb) : 0
pos(ib): 0, pos(bb) : 0
a
b
c
x
*///:~
