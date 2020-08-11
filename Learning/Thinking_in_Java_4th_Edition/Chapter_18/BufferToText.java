/**
 * @author banbao
 * @comment �޸���ʾ������
 */

import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.io.*;

public class BufferToText {
    private static final int BSIZE = 1024;
    public static void main(String[] args) throws Exception {
        // 1. ֱ��ʹ����򵥵ķ���(�б�������)
        FileChannel fc = new FileOutputStream("data2.txt").getChannel();
        fc.write(ByteBuffer.wrap("Some text".getBytes()));
        fc.close();
        fc = new FileInputStream("data2.txt").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(BSIZE);
        fc.read(buff);
        buff.flip();
        // ��������
        System.out.println(buff.asCharBuffer());
        
        // 2. ʹ��ϵͳĬ�ϵı��뷽ʽ
        buff.rewind(); // ��ָ�����ڿ�ͷ,�������
        String encoding = System.getProperty("file.encoding");
        System.out.println("Decoded using " + encoding + ": "
            + Charset.forName(encoding).decode(buff));

        // 3. �ڴ洢ʱ�͸������뷽ʽΪ UTF-16BE(JavaĬ�ϱ��뷽ʽ)
        fc = new FileOutputStream("data2_sure.txt").getChannel();
        fc.write(ByteBuffer.wrap("Some text".getBytes("UTF-16BE")));
        fc.close();
        fc = new FileInputStream("data2_sure.txt").getChannel();
        buff.clear();
        fc.read(buff);
        buff.flip();
        System.out.println(buff.asCharBuffer());
        
        // 4. д���ʱ��ʹ�� CharBuffer (�� CharBuffer ʣ���null��д��ȥ��)
        fc = new FileOutputStream("data2_cb.txt").getChannel();
        buff = ByteBuffer.allocate(24); // More than needed
        String putStr = "Some text";
        CharBuffer cb = buff.asCharBuffer();
        cb.put(putStr); // ע������ֻ��ı� cb �� position, buff �Ĳ���
        buff.limit(cb.position()*2); 
        // Ϊʲô*2,��Դ���� put(char) ��ʱ�� position ��¼�Ķ���һ��
        // ��һ���� 1 char = 2 byte
        // ��Ҫ�ֶ����� `limit`, ���������հ�(Some text???)
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
î��?��
Decoded using GBK: Some text
Some text
Some text
*/