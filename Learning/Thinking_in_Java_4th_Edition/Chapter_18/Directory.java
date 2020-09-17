/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.io.File;
import java.util.ArrayList;

public class Directory {
    public static String regex;
    public static ArrayList<String> filesCaught = new ArrayList<>();
    
    public static void main(String...args) {
        // default
        String strPath = ".";
        regex = ".*\\.java";
        // arg
        if(args.length >= 1) { strPath = args[0]; }
        if(args.length >= 2) { regex = args[1]; }
        // pre
        filesCaught.clear();
        // walk
        File path = new File(strPath);
        walk(path);
        // sort and print
        // sortAndPrint();
    }
    
    public static void sortAndPrint() {
        filesCaught.sort((a,b)->(a.compareTo(b)));
        StringBuilder sb = new StringBuilder();
        for(String str : filesCaught){
            sb.append(str);
            sb.append("\n");
        }
        System.out.print(sb);
    }
    
    public static void walk(File dir) {
        // 不加入文件夹
        File[] files = dir.listFiles();
        if(files == null) return; // 空文件夹
        for(File now : files) {
            if(now.isDirectory()) {
                walk(now);
            } else {
                String simpleName = now.getName();
                if(simpleName.matches(regex)) {
                    // filesCaught.add(now.getPath());
                    System.out.println(
                        // now.getPath()
                        now.getAbsolutePath()
                    );
                }
            }
        }
    }
}


/* Output
*/
