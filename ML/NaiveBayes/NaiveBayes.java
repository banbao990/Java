/**
 * @author:banbao
 * @encode:ANSI
*/

import static java.lang.System.out;
import java.io.*;
import java.util.*;

public class NaiveBayes{
    // 去除空白符
    static String regex = "[\\s]+";
    // 输入文件列表操作相关
    static String inputFileName = "Data.txt";// 朴素贝叶斯的数据集
    static File inputFile;
    static BufferedReader in;
    static int totalElement = 0;
    static final int aspects = 4;
    static int days = 0;
    static int PT = 0;// play tennis
    
    // yes:1,no:0
    static int[][] count = new int[2][20];
    // 记录输入元素的对应编号
    static HashMap<String, String> m = new HashMap<>();
    // scanner
    static Scanner sc = new Scanner(System.in);
    
    //读入数据
    public static boolean readData(String[] args){
        boolean okRead = true;
        // 输入的第一个参数
        if(args.length >= 1){ inputFileName = args[0]; }
        try{
            // 打开文件流
            inputFile = new File(inputFileName);
            in = new BufferedReader(new FileReader(inputFile));
            
            // 读入数据开始操作
            // 去掉第一行标题
            String inputLine = in.readLine();
            inputLine = in.readLine();
            while(inputLine != null){
                ++ days;
                String[] infos = inputLine.split(regex);
                /* days,Outlook,Temperature,Humidity,Wind,PlayTennis */
                int yes = (("yes").equals(infos[5])) ? 1 : 0;
                PT += yes;
                for(int i = 1; i <= aspects; ++i){
                    // num 表示该关键字的编号
                    int num;
                    if(m.containsKey(infos[i])){ num = Integer.parseInt(m.get(infos[i])); }
                    else{
                        m.put(infos[i], String.valueOf(totalElement));
                        num = totalElement++;
                    }
                    ++count[yes][num]; 
                }
                inputLine = in.readLine();
            }
            // 关闭文件流
            in.close();
        }catch (FileNotFoundException e1) {
            System.err.println("File \"" + inputFileName + "\" not found!");
            okRead = false;
        } catch (IOException e2) {
            e2.printStackTrace();
            okRead = false;
        }
        return okRead;
    }
    
    // 提示信息
    public static void helpInfo(){
        out.println("Welcome to the Naive Bayes Small Analysis!");
        out.println("The Analysis contains four pamameters:Outlook,Temperature,Humidity,Wind.");
        out.println("Each contains elements as following shows.");
        out.println("Outlook:sunny,overcast,rainy.");
        out.println("Temperature:hot,mild,cool.");
        out.println("Humidity:high,normal");
        out.println("Wind:weak,strong");
        out.println("You can input a conbination of four aspects,");
        out.println("And we will show you the probability of Playing Tennis!");
        out.println("The input must be in a line separated by blank");
        out.println("Example(The order of the aspects could random):");
        out.println("    sunny hot high weak");
    }
    
    public static void showData(){
        Set<String> s = m.keySet();
        for(String a : s){
            int num = Integer.parseInt(m.get(a));
            out.println(a + ":\nyes:" + count[0][num] + " no:" + count[1][num]);
        }
    }
    
    public static void goodbye(){
        out.println("GoodBye!");
    }
    
    public static void errorPrinter(){
        out.println("Error Input!");
    }
    
    /*
     * 朴素贝叶斯算法使用样本中的概率直接估计真实概率 
     *     P(c_real) = P(c_sample)
     * 朴素贝叶斯算法假设条件独立
     *     P(a,b,c|d) = P(a|d)*P(b|d)*P(c|d)
     * 过程:
     * state = {l, m, n}
     * P(ans|state) = P(ans, state)/P(state)
     * P(ans, state) = P(state|ans) * P(ans)
     *     P(ans) 使用样本估计
     * P(state|ans) = P(l|ans)*P(m|ans)*P(n|ans)
     *     P(l|ans) 等使用样本估计
     * P(state) = \sum^{ans} P(ans, state)
     *     P(state) 将所有可能求和(归一化)
    */
    public static void naiveBayes(){
        String inputInfo;
        while(true){
            out.println("Please input your state.(\"quit\" to quit!)");
            inputInfo = sc.nextLine();
            if(("quit").equals(inputInfo)){ break; }
            String[] infos = inputInfo.split(regex);
            if(infos.length != aspects){ errorPrinter(); }
            int[] num = new int[aspects];
            int i;
            for(i = 0; i < aspects; ++i){
                if(!m.containsKey(infos[i])){
                    errorPrinter();
                    break;
                }
                num[i] = Integer.parseInt(m.get(infos[i]));
            }
            if(i != aspects){ continue; }
            int[] up = new int[2];
            int[] down = new int[2];
            for(i = 0; i < 2; ++i){
                int upBase =  i * PT + (1 - i)*(days - PT);
                up[i] = upBase;
                down[i] = days;
                for(int j = 0; j < aspects; ++j){
                    up[i] *= count[i][num[j]];
                    down[i] *= upBase;
                }
            }
            // out.printf("%d %d %d %d %d\n",PT,up[0],down[0],up[1],down[1]);
            int U = up[1]*down[0];
            int D = U + up[0]*down[1];
            double ans = 1.0*U/D;
            // double ans = 1.0*up[1]/down[1] / (1.0*up[1]/down[1] + 1.0*up[0]/down[0]);
            int val = gcd(U, D);
            out.printf("The probability to play tennis is (%d/%d)%f\n",U/val ,D/val ,ans);
        }
    }
    
    // 辗转相除法求最大公因数
    public static int gcd(int a, int b){
        int t, c;
        if(a > b){
            t = a;
            a = b;
            b = t;
        }
        c = b % a;
        while(c != 0){
            t = b - a;
            if(t > a){ b = t; }
            else{
                b = a;
                a = t;
            }
            c = b % a;
        }
        return a;
    }
    
    public static void main(String[] args){
        if(readData(args)){
            helpInfo();
            // showData();
            naiveBayes();
        }
        goodbye();
    }
}