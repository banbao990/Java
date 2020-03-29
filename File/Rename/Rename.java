import java.io.*;

/**
 * @author banbao
 * @encode ANSI
*/

public class Rename{
    // bbq: Ϊʲô "  a.txt b.txt" ƥ�䳤�Ȼ���3?
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
                s = s.trim();// ȥ�����߿ո�
                String[] infos = s.split(regex);

                // ��ע��
                if(s.startsWith("#")){
                    continue;
                }

                // ������Ϊ 2
                if(infos.length != 2){
                    errorPrinter("Need two paramaters!");
                    continue;
                }

                // ��ȡ�¾��ļ���
                fileNameBefore = infos[0];
                fileNameAfter = infos[1];

                // �¾��ļ�����
                if(!fileNameBefore.equals(fileNameAfter)){
                    errorPrinter("The new file name is equals to the old one!");
                    continue;
                }

                // ��ȡ�¾��ļ�����
                File oldfile = new File(fileNameBefore);
                File newfile = new File(fileNameAfter);

                // ���ļ�������
                if(!oldfile.exists()){
                    errorPrinter("File \"" + fileNameBefore +  "\" is not exsited!");
                    continue;
                }

                // ���ļ����Ѿ�����
                if(newfile.exists()){
                    errorPrinter(fileNameAfter + " is already exsited!");
                    continue;
                }

                // ���ڿ�����������
                oldfile.renameTo(newfile);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String...arg){
        // ��ʾ��Ϣ
        helpInfo();
        // ������
        work();
    }
}