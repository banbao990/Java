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
 * 1.1 û�н��в����(δ��������ļ���ʽ)
 * 1.2 ����ļ�����Ŀ¼���Բ�����
 * 1.3 �����Ե�ǰĿ¼Ϊ������Ŀ¼
 */

public class FormatChange{
    final static String newDir = "banbao\\";
    
    // �����������ļ����ļ���
    static {
        File newFileDir = new File(newDir);
        if(!newFileDir.exists()) {
            newFileDir.mkdirs();
        }
    }
    
    // ���������в���
    public static void main(String...args) {
        FormatChange fc = new FormatChange();
        try {
            // ��������Ƿ���ȷ
            boolean OK = false;
            // ����ļ���
            String outputFileName = null;
            if(args.length == 2 || args.length == 3){
                // ����ļ���
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
                    throw new NotSupportedException("ֻ֧��UTF8,GBK");
                }
            }
            // ���벻��ȷ
            if(!OK) {
                fc.help();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // �����ʾ��Ϣ
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
    
    // ��ʽת��
    public void change(
            String inputFileName, String outputFileName, 
            String format1, String format2
        ) throws IOException, NotSupportedException {
        // null
        if(outputFileName == null) {
            outputFileName = inputFileName;
        }
        // �������ļ�����Ŀ¼�Ƿ����
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
    
    // �������ļ�����Ŀ¼�Ƿ����
    public String check(String outputFileName)
        throws NotSupportedException{
        // null
        if(outputFileName == null) {
            return null;
        }
        // deal
        String ret = null;
        int index = outputFileName.indexOf(":");
        // ����·��(J)
        // (J)case 1:����':', �����ļ�������':'
        if(index != -1) {
            ret = newDir + outputFileName.substring(index + 1);
        }
        // case 2:��'\' ��ͷ��Ӱ��(������)
        // ���·��
        else {
            // ��"..\" ��ͷ�����·����֧��
            if(outputFileName.startsWith("..\\"))
                throw new NotSupportedException("��֧��\"../\"��ʽ�����·��");
            ret = newDir + outputFileName;
        }
        // �ݹ��½��ļ���
        // ��ͷ�������� "\"
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