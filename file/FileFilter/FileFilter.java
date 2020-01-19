import java.io.*;
import java.nio.file.*;
import java.nio.*;
import java.util.*;

/**
 * 将源文件夹(src)中的所有文件都移动到目的文件夹下(dst)
 * 编码格式: ANSI
 * @author banbao
 */

public class FileFilter {
    static String src;// 源文件夹
    static String dst;// 目的文件夹
    static Path source;
    static Path destination;
    static int random = 0;
    static int count = 0;
    static Scanner sc = new Scanner(System.in);
    public static void main(String args[]){
        /* 获取输入输出文件夹 */
        System.out.println("请输入源文件夹:");
        while(true) {
            src = sc.next();
            source = Paths.get(src);
            if(Files.exists(source)) break;
            /* 若源文件夹不存在 */
            System.out.println("源文件夹不存在, 请重新输入:");
        }
        System.out.println("请输入目的文件夹:");
        dst = sc.next();
        destination = Paths.get(dst);
        /* 若目的文件夹不存在, 创建目的文件夹 */
        try {
            if (!Files.exists(destination))
                Files.createDirectory(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* 递归处理文件夹 */
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            for (Path entry : stream) {
                move(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        /* 输出提示信息 */
        System.out.println("一共移动了" + count + "个文件");
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
            /* 同名文件存在, 加上随机码 */
            while(Files.exists(Paths.get(dst + '\\' + oldFile)))
                oldFile = (++random) + '_' + oldFile;
            System.out.println((count++) + ":" + old.toString() + " -> " + dst + '\\' + oldFile);
            Files.move(old, Paths.get(dst + '\\' + oldFile));
        }
    }
}
