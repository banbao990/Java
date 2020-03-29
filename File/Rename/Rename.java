import java.io.*;

/**
 * @author banbao
 * @encode ANSI
*/

public class Rename{
    // bbq: 为什么 "  a.txt b.txt" 匹配长度会是3?
    static final String regex = "[\\s]+";
    static int lineCount = 0;

    private static void helpInfo(){
        System.out.println("\nRename file as \"key-values.txt\" told!");
        System.out.println("Each line assigns a rule!");
        System.out.println("Each rule consists of two parameters!");
        System.out.println("one is Old-file-name, the other is New-file-name!");
        System.out.println("Two parameters should be separated by blank!");
        System.out.println("As we all know, no output means alright!");
        System.out.println("TIP:");
        System.out.println("    You can add \"#\" to the beginning of one line to comment it!");
        System.out.println("    That is to say, you old-file-name can't be started with \"#\"!\n");
    }

    private static void errorPrinter(String stringToBePrinted){
        System.out.println("Line " + lineCount + ": " + stringToBePrinted);
    }

    private static void work(){
        try {
            FileReader input = new FileReader("key-value.txt");
            BufferedReader br = new BufferedReader(input);
            while (true) {
                String s = br.readLine();
                if(s == null){
                    break;
                }
                ++ lineCount;
                String fileNameBefore, fileNameAfter;
                s = s.trim();// 去除两边空格
                String[] infos = s.split(regex);

                // 是注释
                if(s.startsWith("#")){
                    continue;
                }

                // 参数不为 2
                if(infos.length != 2){
                    errorPrinter("Need two paramaters!");
                    continue;
                }

                // 获取新旧文件名
                fileNameBefore = infos[0];
                fileNameAfter = infos[1];

                // 新旧文件重名
                if(!fileNameBefore.equals(fileNameAfter)){
                    errorPrinter("The new file name is equals to the old one!");
                    continue;
                }

                // 获取新旧文件对象
                File oldfile = new File(fileNameBefore);
                File newfile = new File(fileNameAfter);

                // 旧文件不存在
                if(!oldfile.exists()){
                    errorPrinter("File \"" + fileNameBefore +  "\" is not exsited!");
                    continue;
                }

                // 新文件名已经存在
                if(newfile.exists()){
                    errorPrinter(fileNameAfter + " is already exsited!");
                    continue;
                }

                // 终于可以重命名了
                oldfile.renameTo(newfile);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String...arg){
        // 提示信息
        helpInfo();
        // 重命名
        work();
    }
}