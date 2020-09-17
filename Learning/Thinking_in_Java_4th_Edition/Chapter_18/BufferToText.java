/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.io.*;

public class BufferToText {
    private static final int BSIZE = 1024;
    public static void main(String[] args) throws Exception {
        // 1. 直接使用最简单的方法(有编码问题)
        FileChannel fc = new FileOutputStream("data2.txt").getChannel();
        fc.write(ByteBuffer.wrap("Some text".getBytes()));
        fc.close();
        fc = new FileInputStream("data2.txt").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(BSIZE);
        fc.read(buff);
        buff.flip();
        // 编码问题
        System.out.println(buff.asCharBuffer());
        
        // 2. 使用系统默认的编码方式
        buff.rewind(); // 将指针置于开头,舍弃标记
        String encoding = System.getProperty("file.encoding");
        System.out.println("Decoded using " + encoding + ": "
            + Charset.forName(encoding).decode(buff));

        // 3. 在存储时就给定编码方式为 UTF-16BE(Java默认编码方式)
        fc = new FileOutputStream("data2_sure.txt").getChannel();
        fc.write(ByteBuffer.wrap("Some text".getBytes("UTF-16BE")));
        fc.close();
        fc = new FileInputStream("data2_sure.txt").getChannel();
        buff.clear();
        fc.read(buff);
        buff.flip();
        System.out.println(buff.asCharBuffer());
        
        // 4. 写入的时候使用 CharBuffer (把 CharBuffer 剩余的null都写进去了)
        fc = new FileOutputStream("data2_cb.txt").getChannel();
        buff = ByteBuffer.allocate(24); // More than needed
        String putStr = "Some text";
        CharBuffer cb = buff.asCharBuffer();
        cb.put(putStr); // 注意这里只会改变 cb 的 position, buff 的不变
        buff.limit(cb.position()*2); 
        // 为什么*2,看源码在 put(char) 的时候 position 记录的都是一半
        // 另一方面 1 char = 2 byte
        // 需要手动设置 `limit`, 否则会输出空白(Some text???)
        // CharBuffer : Creates a view of this byte buffer as a char buffer.
        fc.write(buff);
        fc.close();
        // Read and display:
        fc = new FileInputStream("data2_cb.txt").getChannel();
        buff.clear();
        fc.read(buff);
        buff.flip();
        System.out.println(buff.asCharBuffer());
    }
} 
/* Output:
卯浥?數
Decoded using GBK: Some text
Some text
Some text
*/
