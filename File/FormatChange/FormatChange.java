import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;

/**
 * @author banbao
 * @version 1
 * 1.1 没有进行查错处理(未检查输入文件格式)
 * 1.2 输出文件所在目录可以不存在
 * 1.3 建议以当前目录为根查找目录
 */

public class FormatChange{
    final static String newDir = "banbao\\";
    
    // 创建放置新文件的文件夹
    static {
        File newFileDir = new File(newDir);
        if(!newFileDir.exists()) {
            newFileDir.mkdirs();
        }
    }
    
    // 接受命令行参数
    public static void main(String...args) {
        FormatChange fc = new FormatChange();
        try {
            // 标记输入是否正确
            boolean OK = false;
            // 输出文件名
            String outputFileName = null;
            if(args.length == 2 || args.length == 3){
                // 输出文件名
                if(args.length == 3) outputFileName = args[2];
                // UTF8 to GBK
                if("U2G".equals(args[0])) {
                    OK = true;
                    fc.change(args[1], outputFileName, "UTF-8", "GBK");
                } 
                // GBK to UTF8
                else if("G2U".equals(args[0])) {
                    OK = true;
                    fc.change(args[1], outputFileName, "GBK", "UTF-8");
                } else {
                    throw new NotSupportedException("只支持UTF8,GBK");
                }
            }
            // 输入不正确
            if(!OK) {
                fc.help();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // 输出提示信息
    public void help(){
        System.err.println(
            "usage:\n"
            + "    (1)java FormatChange <U2G|G2U> inputFileName\n"
            + "    (2)java FormatChange <U2G|G2U> inputFileName outputFileName\n"
            + "        U2G:UTF8 to GBK\n"
            + "        G2G:GBK to UTF8\n"
            + "attention:relative path should use \"\\\"!\n"
        );
    }
    
    // 格式转换
    public void change(
            String inputFileName, String outputFileName, 
            String format1, String format2
        ) throws IOException, NotSupportedException {
        // null
        if(outputFileName == null) {
            outputFileName = inputFileName;
        }
        // 检查输出文件所在目录是否存在
        outputFileName = check(outputFileName);
        // input
        FileInputStream fis = new FileInputStream(inputFileName); // IOException
        BufferedReader br = new BufferedReader(
            new InputStreamReader(fis, format1)
        );
        // output 
        FileOutputStream fos = new FileOutputStream(outputFileName);
        BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(fos, format2)
        );
        // deal
        while(true){
            String s = br.readLine();
            if(s == null) break;
            bw.write(s);
            bw.newLine();
        }
        // reverse order
        bw.close();
        br.close();
    }
    
    // 检查输出文件所在目录是否存在
    public String check(String outputFileName)
        throws NotSupportedException{
        // null
        if(outputFileName == null) {
            return null;
        }
        // deal
        String ret = null;
        int index = outputFileName.indexOf(":");
        // 绝对路径(J)
        // (J)case 1:存在':', 正常文件不存在':'
        if(index != -1) {
            ret = newDir + outputFileName.substring(index + 1);
        }
        // case 2:以'\' 开头不影响(不处理)
        // 相对路径
        else {
            // 以"..\" 开头的相对路径不支持
            if(outputFileName.startsWith("..\\"))
                throw new NotSupportedException("不支持\"../\"方式的相对路径");
            ret = newDir + outputFileName;
        }
        // 递归新建文件夹
        // 开头不可能是 "\"
        index = 0;
        while(true) {
            index = ret.indexOf("\\" , index + 1);
            if(index == -1) break;
            File newFileDir = new File(ret.substring(0, index));
            if(!newFileDir.exists()) {
                newFileDir.mkdirs();
            }
        }
        return ret;
    }
}