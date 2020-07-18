import java.io.File;

/**
 * @author banbao
 * @version 1
 * 1.1 批量文件格式转换
 */

public class FormatChangeBat{
    static FormatChange fc = new FormatChange();
    static String format1, format2;
    static int total = 0;
    // 接受命令行参数
    public static void main(String...args) throws Exception{
        if(args.length == 2) {
            if("U2G".equals(args[0])) {
                format1 = "UTF-8"; format2 = "GBK";
            }
            else if("G2U".equals(args[0])){
                format2 = "UTF-8"; format1 = "GBK";
            } else{
                throw new NotSupportedException();
            }
            change(new File(args[1])); // Exception
        } else {
            System.err.println("usage:java FormatChangeBat <U2G|G2U> dirName\n");
        }
        System.out.println("total:" + total);
    }

    // 递归进行转化
    public static void change(File dir)
        throws Exception {
        // 文件或者不存在
        if(!dir.exists() || !dir.isDirectory()) return;
        // 打开文件夹
        for(String fileInDir : dir.list()){
            File file = new File(dir, fileInDir);
            if(file.isFile()){
                fc.change(dir + "\\" + file.getName(), null, format1, format2);
                ++total;
            }else{
                // 对于子目录,进行递归调用
                change(file);
            }
        }
    }
}