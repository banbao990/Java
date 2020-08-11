/**
 * @author banbao
 * @comment �޸���ʾ������
 */

import java.io.File;
import java.util.Arrays;

public class TestFile {
    public static void main(String...args) {
        File path;
        // �ָ��� : Windows:\, Unix:/
        System.out.println("separatorChar" + File.separatorChar);
        // ԭʼ
        path = new File(".");
        // test(path, "new File(\".\")");
        // getAbsoluteFile()
        path = new File(".").getAbsoluteFile();
        // test(path, "new File(\".\").getAbsoluteFile()");
        
        // mkdir() ֻ�ܴ���һ��Ŀ¼,��Ҫ�и�Ŀ¼
        // System.out.println(new File("./a/b/c/d/e").mkdir());
        
        // mkdirs() ���Դ����༶Ŀ¼,����Ҫ�и�Ŀ¼
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
        // public boolean delete() // ֻ��ɾ�����ļ���
        // public boolean exists()
        // public boolean mkdir()
        // public boolean mkdirs()
    }
}


/* Output
*/