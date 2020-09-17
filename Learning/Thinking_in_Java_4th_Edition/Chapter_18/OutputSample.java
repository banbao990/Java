/**
 * @author banbao
 * @comment 修改自示例代码(简单处理异常)
 * 依赖于 InputSample.java
 */

import java.io.*;

// @SuppressWarnings("unchecked")
public class OutputSample {
    private static String inputStr = null;
    public static void main(String...args) 
        throws Exception {
        String outputFileName = "SampleOutput.out";
        String inputFileName = "Sample_EN.txt";
        if(args.length >= 1) { outputFileName = args[0]; }
        // output
        basicFileOutput(inputFileName, outputFileName);
        
    }
    
    public static void basicFileOutput(
        String inputFileName, String outputFileName) 
        throws Exception {
        BufferedReader in = new BufferedReader(
            new FileReader(inputFileName)
        );
        PrintWriter out = new PrintWriter(
            new BufferedWriter(
                new FileWriter(outputFileName)
        ));
        // PrintWriter out = new PrintWriter(outputFileName);
        String line;
        while((line = in.readLine()) != null){
            out.println(line);
        }
        out.close();
        in.close();
    }
}


/* Output
*/
