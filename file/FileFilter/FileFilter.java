import java.io.*;
import java.nio.file.*;
import java.nio.*;
import java.util.*;

/**
 * ��Դ�ļ���(src)�е������ļ����ƶ���Ŀ���ļ�����(dst)
 * �����ʽ: ANSI
 * @author banbao
 */

public class FileFilter {
    static String src;// Դ�ļ���
    static String dst;// Ŀ���ļ���
    static Path source;
    static Path destination;
    static int random = 0;
    static int count = 0;
    static Scanner sc = new Scanner(System.in);
    public static void main(String args[]){
        /* ��ȡ��������ļ��� */
        System.out.println("������Դ�ļ���:");
        while(true) {
            src = sc.next();
            source = Paths.get(src);
            if(Files.exists(source)) break;
            /* ��Դ�ļ��в����� */
            System.out.println("Դ�ļ��в�����, ����������:");
        }
        System.out.println("������Ŀ���ļ���:");
        dst = sc.next();
        destination = Paths.get(dst);
        /* ��Ŀ���ļ��в�����, ����Ŀ���ļ��� */
        try {
            if (!Files.exists(destination))
                Files.createDirectory(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* �ݹ鴦���ļ��� */
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            for (Path entry : stream) {
                move(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        /* �����ʾ��Ϣ */
        System.out.println("һ���ƶ���" + count + "���ļ�");
    }
    
    public static void move(Path old) throws IOException {
        if(Files.isDirectory(old)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(old)) {
                for (Path entry : stream) {
                    move(entry);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            String oldFile = old.getFileName().toString();
            /* ͬ���ļ�����, ��������� */
            while(Files.exists(Paths.get(dst + '\\' + oldFile)))
                oldFile = (++random) + '_' + oldFile;
            System.out.println((count++) + ":" + old.toString() + " -> " + dst + '\\' + oldFile);
            Files.move(old, Paths.get(dst + '\\' + oldFile));
        }
    }
}
