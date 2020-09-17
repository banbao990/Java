import java.io.File;

/**
 * @author banbao
 * @version 1
 * 1.1 �����ļ���ʽת��
 * @version 2
 * 2.1 ����������ʽ����
 */

public class FormatChangeBat{
    static FormatChange fc = new FormatChange();
    static String format1, format2;
    static int total = 0;
    static String regex = "";
    // ���������в���
    public static void main(String...args) throws Exception{
        if(args.length == 3) {
            regex = args[2];
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
            System.err.println("usage:java FormatChangeBat <U2G|G2U> dirName regex\n");
            System.exit(1);
        }
        System.out.println("total:" + total);
    }

    // �ݹ����ת��
    public static void change(File dir)
        throws Exception {
        // �ļ����߲�����
        if(!dir.exists() || !dir.isDirectory()) return;
        // ���ļ���
        for(String fileInDir : dir.list()){
            File file = new File(dir, fileInDir);
            if(file.isFile()){
                if(!fileInDir.matches(regex)) { continue; }
                fc.change(dir + "\\" + file.getName(), null, format1, format2);
                ++total;
            }else{
                // ������Ŀ¼,���еݹ����
                change(file);
            }
        }
    }
}