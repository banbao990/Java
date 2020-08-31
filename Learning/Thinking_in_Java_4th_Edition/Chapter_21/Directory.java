/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.io.File;
import java.util.ArrayList;

public class Directory {
    public static int cnt = 0;    
    public static void main(String...args) {
        // default
        String strPath = "D:\\my_files\\GIT\\-\\Java\\Learning\\Thinking_in_Java_4th_Edition\\Book_answer";
        // walk
        File path = new File(strPath);
        walk(path);
        System.out.println(cnt);
    }
    
    public static void walk(File dir) {
        // 不加入文件夹
        File[] files = dir.listFiles();
        if(files == null) return; // 空文件夹
        int length = files.length;
        for(int i = 0;i < length; ++i) {
            File now = files[i];
            if(now.isDirectory()) {
                walk(now);
            } else {
                ++cnt;
                // String simpleName = now.getName();
                // if(simpleName.matches("[^\\(]*\\([^\\)]*\\).*")) {
                    // String oldName = now.getAbsolutePath();
                    // int l1 = oldName.indexOf("(");
                    // int l2 = oldName.indexOf(")");
                    // String newName = oldName.substring(0, l1) + oldName.substring(l2 + 1);
                    // now.renameTo(new File(newName));
                // }
            }
        }
    }
}


/* Output
*/