/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.io.File;
import java.util.Arrays;

public class TestFile {
    public static void main(String...args) {
        File path;
        // 分隔符 : Windows:\, Unix:/
        System.out.println("separatorChar" + File.separatorChar);
        // 原始
        path = new File(".");
        // test(path, "new File(\".\")");
        // getAbsoluteFile()
        path = new File(".").getAbsoluteFile();
        // test(path, "new File(\".\").getAbsoluteFile()");
        
        // mkdir() 只能创建一级目录,需要有父目录
        // System.out.println(new File("./a/b/c/d/e").mkdir());
        
        // mkdirs() 可以创建多级目录,不需要有父目录
        // System.out.println(new File("./a/b/c/d/e").mkdirs());

        System.out.println(Arrays.deepToString(File.listRoots()));

        // test(new File("./TestFile.java"), "");
        // test(new File(new File("./TestFile.java").getName()), "");
        // test(new File("./TestFile.java").getAbsoluteFile(), "");
        // test(new File(new File("./TestFile.java").getName()).getAbsoluteFile(), "");
    }
    
    public static void print(String o1, Object o2) {
        System.out.println(o1 + "():\n    " + o2);
    }
    
    public static void test(File path, String header) {
        System.out.println("~~~~~" + header + "~~~~~");
        print("getName", path.getName());
        print("getParent", path.getParent());
        // public File getParentFile();
        print("getPath", path.getPath());
        print("getAbsolutePath", path.getAbsolutePath());
        print("isDirectory", path.isDirectory());
        print("isFile", path.isFile());
        // public boolean delete() // 只能删除空文件夹
        // public boolean exists()
        // public boolean mkdir()
        // public boolean mkdirs()
    }
}


/* Output
*/