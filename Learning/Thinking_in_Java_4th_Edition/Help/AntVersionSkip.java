/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class AntVersionSkip {
    private static String regex;
    public static final String detect =
            "<fail message=\"J2SE5 required\" unless=\"version1.5\"/>";
    public static void main(String...args) {
        // default
        String strPath = ".." + File.separator;
        regex = ".*\\.xml";
        // walk
        File path = new File(strPath);
        walk(path);
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
                    // deal
                    skip(now.getAbsolutePath());
                }
            }
        }
    }
    
    private static void skip(String now) {
        List<String> list = new ArrayList<>();
        // file
        File file = new File(now);
        // input
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                     new FileReader(now));
            String line;
            while((line = br.readLine()) != null) {
                if(detect.equals(line.trim())){
                    line = "<!--" + line + "-->"; 
                }
                list.add(line);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try{
                br.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        // delete
        file.delete();
        // output
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                     new FileWriter(now));
            for(String line : list) {
                bw.write(line);
                bw.write("\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try{
                bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}


/* Output
*/