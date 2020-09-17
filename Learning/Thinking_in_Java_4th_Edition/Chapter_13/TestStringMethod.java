/**
 * @author banbao
 */

public class TestStringMethod{
    public static void printHash(String s){
        System.out.println(s.hashCode());
    }

    public static void print(Object s){
        System.out.println(s);
    }

    public static void seg(){
        System.out.println("~~~~~~~~~~");
    }

    public static void main(String...arg) throws Exception{
        // 该字符串只有一个特殊字符?，使用其utf-16值描述该字符
        String s = new String("\uB0A1".getBytes("EUC-CN"), "EUC-CN");  
        byte[] x = "\uB0A1".getBytes();
        for(int i = 0;i < x.length; ++i)
            System.out.print(x[i]);
        print(x.length);
        print(s);
    }
}
