/**
 * @author banbao
 * @comment ��ʽ����
 */

import java.io.PrintStream;
public class TestFormatOutput2 {
    private class FE{}
    public static void main(String...args){
        new TestFormatOutput2().run();
    }
    
    public void run(){
        // ����
        PrintStream p = System.out;
        // һЩ����
        int a = 10086;
        float b = 4.44f;
        // "%[argument_index$][flags][width][.precision]coversion"
        // Ĭ���Ҷ���,'-'�����
        p.printf("\"%10.6f\"\n", b);
        p.printf("\"%-10.6f\"\n", b);
        // ȱʧ�� 0
        p.printf("\"%010.6f\"\n", b);
        // %%
        p.printf("%%\n", b);
        // %h
        FE fe = new FE();
        p.format("%h\n", fe);
        p.println(fe);
        // %b:false <=> null
        p.printf("%b\n", 0);
        
    }
}

/* output
"  4.440000"
"4.440000  "
"004.440000"
%
232204a1
TestFormatOutput2$FE@232204a1
true
*/