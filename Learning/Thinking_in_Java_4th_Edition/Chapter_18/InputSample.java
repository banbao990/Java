/**
 * @author banbao
 * @comment �޸���ʾ������(�򵥴����쳣)
 */

import java.io.*;

// @SuppressWarnings("unchecked")
public class InputSample {

    public static void main(String...args)
        throws Exception {
        String fileName = "Sample_EN.txt";
        if(args.length >= 1){ fileName = args[0]; }
        // System.out.println(bufferedInputFile(fileName));
        // memoryInput(fileName);
        // formattedMemoryInput(fileName);
        formattedMemoryInput2(fileName);
    }

    // 18.6.1 ���������ļ�
    public static String bufferedInputFile (String fileName)
        throws Exception {
        BufferedReader in = new BufferedReader(
            new FileReader(fileName)
        );
        String s;
        StringBuilder sb = new StringBuilder();
        while((s = in.readLine()) != null){
            sb.append(s);
            sb.append("\n");
        }
        in.close();
        return sb.toString();
    }

    // 18.6.2 ���ڴ�����
    public static void memoryInput(String fileName)
        throws Exception {
        StringReader in = new StringReader(
            // return a String
            bufferedInputFile(fileName)
        );
        int c;
        while((c = in.read()) != -1){
            System.out.print((char)c);
        }
    }

    // 18.6.3 ��ʽ�����ڴ�����
    public static void formattedMemoryInput(String fileName)
        throws Exception {
        try {
            DataInputStream in = new DataInputStream(
                new ByteArrayInputStream(
                    bufferedInputFile(fileName).getBytes()
                )
            );
            while(true) {
                // ���Ļ������ unicode
                System.out.print((char)in.readByte());
            }
        } catch (EOFException e) {
            System.err.println("End Of Stream!");
        }
    }
    
    public static void formattedMemoryInput2(String fileName)
        throws Exception {
        DataInputStream in = new DataInputStream(
            new BufferedInputStream(
                new FileInputStream(fileName)
        ));
        while(in.available() != 0) {
            System.out.print((char)in.readByte());    
        }
        in.close();
    }
}


/* Output
*/