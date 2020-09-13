// makeREADME.java
/**
 * @author banbao
 */
import java.io.*;

public class makeREADME {
    final static String[] catalog = {
        "第1章 缓冲区的使用",
        "第2章 通道和 FileChannel 类的使用",
        "第3章 获取网络设备信息",
        "第4章 实现 Socket 通信",
        "第5章 选择器的使用",
        "第6章 AIO 的使用",
    };
    
    public static void makeMD(String dir, String head) {
        // make dir
        File newDir = new File(dir);
        if(!newDir.exists()) {
            newDir.mkdirs();
        }
        BufferedWriter br = null;
        // make file
        try {
             br = new BufferedWriter(
                  new FileWriter(dir + "README.md"));
        } catch(IOException e) {
            System.out.println("Create File Fail!");
            return;
        }
        
        try {
            write(br, "# " + head);
            write(br, "> [总目录](../README.md)");
            write(br, "---");
            write(br, "[TOC]");
            write(br, "---");
            write(br, "+ ");
        } catch(IOException e) {
            System.out.println("IO Interrupted!");
        } finally {
            try {
                br.close();
            } catch(IOException e) {
                System.out.println("Close Interrupted!");
            }
        }
    }
    
    public static void write(BufferedWriter br, String content) 
        throws IOException {
        br.write(content);
        br.newLine();
    }
    
    public static void main(String...args) {
        int index = 0;
        for(String head : catalog) {
            makeMD(
                String.format("../Chapter_%02d/", ++index),
                head);
        }
    }
}
