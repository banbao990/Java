/**
 * @author banbao
 * @comment �޸���ʾ������
 */

public class Number1 extends Number {
    public void test(Number other) {
        System.out.println("Number1 Number");
    }
    public void test(Number1 other) {
        System.out.println("Number1 Number1");
    }
    public void test(Number2 other) {
        System.out.println("Number1 Number2");
    }
}
