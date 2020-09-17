/**
 * @author banbao
 */

import java.util.Formatter;
import java.io.PrintStream;
public class TestFormatOutput {
    // 设计技巧
    private Formatter format;
    
    public static void main(String...args){
        new TestFormatOutput().run(System.out);
    }
    
    public void run(PrintStream p){
        char c = '\u03A3';
        int i = 10;
        float f = 1.0f;
        System.out.println(c + " " + i + " " + f);
        System.out.printf("%c %d %.1f\n", c, i, f);
        System.out.format("%c %d %.1f\n", c, i, f);
        // java.util.Formatter
        this.format = new Formatter(p);
        this.format.format("%c %d %.1f\n", c, i, f);
    }
}

/* output
Σ 10 1.0
Σ 10 1.0
Σ 10 1.0
Σ 10 1.0
*/
