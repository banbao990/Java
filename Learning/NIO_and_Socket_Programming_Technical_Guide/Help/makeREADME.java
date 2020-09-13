// makeREADME.java
/**
 * @author banbao
 */
import java.io.*;

public class makeREADME {
    final static String[] catalog = {
        "��1�� ��������ʹ��",
        "��2�� ͨ���� FileChannel ���ʹ��",
        "��3�� ��ȡ�����豸��Ϣ",
        "��4�� ʵ�� Socket ͨ��",
        "��5�� ѡ������ʹ��",
        "��6�� AIO ��ʹ��",
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
            write(br, "> [��Ŀ¼](../README.md)");
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
