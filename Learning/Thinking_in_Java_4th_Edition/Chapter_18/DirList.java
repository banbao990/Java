/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.Arrays;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DirList {
       
    public static void main(String...args) {
        // 传入当前作为基目录
        File path = new File(".");
        // 所有文件
        System.out.println("All Files:");
        String[] list = path.list();
        print(list);

        // FilenameFilter
        String needed = ".*\\.java";
        System.out.println(needed + ":");
        // 匿名类
        list = path.list(
            new FilenameFilter() {
                private Pattern regex = Pattern.compile(needed);
                @Override
                public boolean accept(File dir, String name) {
                    return regex.matcher(name).matches();
                }
            }
        );
        print(list);
    }
    
    public static void print(String[] list) {
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for(String dirItem : list) {
            System.out.println("    " + dirItem);
        }
    }
}


/* Output
*/
