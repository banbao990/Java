/**
 * @author banbao
 * @comment �޸���ʾ������
 */

public class NNumber1 extends NNumber {
    public void reverseTest(NNumber other) {
        System.out.println("reverse reverse");
        other.test(this);
    }
    private int set = 0;
    public void test(NNumber other) {
        if(set != 0) {
            System.out.println("NNumber NNumber");
        } else {
            set = 1;
            System.out.println("reverse");
            other.reverseTest(this);
            set = 0;
        }
    }
    public void test(NNumber1 other) {
        System.out.println("NNumber1 NNumber1");
    }
    public void test(NNumber2 other) {
        System.out.println("NNumber1 NNumber2");
    }
}
