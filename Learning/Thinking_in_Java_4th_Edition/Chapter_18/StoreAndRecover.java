/**
 * @author banbao
 * @comment 修改自示例代码
 * 面向字节,可以用于二进制存储
 */

import java.io.*;

public class StoreAndRecover {
    public static void main(String...args) throws Exception {
        // output
        DataOutputStream out = new DataOutputStream(
            new BufferedOutputStream(
                new FileOutputStream("Data.txt")));
        out.writeInt(5);
        for(int i = 0;i < 5; ++i) {
            out.writeDouble(3.14159);
            out.writeUTF("That was pi");
            out.writeDouble(1.41413);
            out.writeUTF("Square root of 2");
        }
        out.close();
        // input
        DataInputStream in = new DataInputStream(
            new BufferedInputStream(
                new FileInputStream("Data.txt")));
        int count = in.readInt();
        System.out.println("in.available():" + in.available());
        for(int i = 0; i < count; ++i) {
            System.out.println(in.readDouble());
            // Only readUTF() will recover the
            // Java-UTF String properly:
            System.out.println(in.readUTF());
            System.out.println(in.readDouble());
            System.out.println(in.readUTF());
        }
        System.out.println("in.available():" + in.available());
    }
}
/* Output
*/