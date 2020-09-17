/**
 * @author banbao
 * @version 1 
 * 1.0 输出样式模仿的是 notepad++
 * 1.1 文件转化为16进制输出
 * 1.2 输出到标准输出(可以重定向到文件)
 */

import java.io.InputStream;
import java.io.FileInputStream;
public class Dump {
    public static void main(String...args){
        if(args.length != 1) {
            System.err.println("usage: java Dump FileName");
            return;
        }
        try {
            dump(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void dump(String fileName) throws Exception {
        int line = 0;
        StringBuilder sb = new StringBuilder(); // 可变
        InputStream is = new FileInputStream(fileName); // IOException
        byte[] b = new byte[1024];
        int length;
        // head
        sb.append("Address |");
        for(int i = 0;i < 16; ++i) sb.append(String.format(" %x|", i));
        sb.append("\n");
        // read
        while((length = is.read(b)) != -1) {
            for(int i = 0; i < length; ++i) {
                if(i % 16 == 0) {
                    sb.append(String.format("%08x", (line++)*16));
                }
                sb.append(String.format(" %02x", b[i]));
                if((i + 1) % 16 == 0) sb.append("\n");
            }
        }
        is.close();
        System.out.println(sb);
    }
}
