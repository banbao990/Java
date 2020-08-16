/**
 * @author banbao
 * @comment 修改自示例代码
 */
package test;
import net.mindview.atunit.*;
import java.io.*;
public class TestRemover {
    @TestProperty static PrintWriter output;
    @TestProperty static int counter;
    @TestProperty static void test1() {
        output = new PrintWriter(System.out);
        output.println("just test!");
    }
    @Test boolean test2() {
        return true;
    }
}