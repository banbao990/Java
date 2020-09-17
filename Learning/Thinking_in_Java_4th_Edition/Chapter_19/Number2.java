/**
 * @author banbao
 * @comment 修改自示例代码
 */

public class Number2 extends Number {
    public void test(Number other) {
        System.out.println("Number2 Number");
    }
    public void test(Number1 other) {
        System.out.println("Number2 Number1");
    }
    public void test(Number2 other) {
        System.out.println("Number2 Number2");
    }
}
