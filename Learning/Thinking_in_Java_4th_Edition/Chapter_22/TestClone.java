/**
 * @author banbao
 * @comment modified from example code
 */

class TCA {}
public class TestClone implements Cloneable{
    int a = 10;
    TCA aa = new TCA();
    public static void main(String...args) throws Exception {
        // run(new JFrame(), 100, 100);
        TestClone s1, s2;
        s1 = new TestClone();
        s2 = (TestClone)s1.clone();
        System.out.println(s1.a + " " + s2.a + " " + s1.aa + " " + s2.aa);
        s1.a = 100;
        System.out.println(s1.a + " " + s2.a + " " + s1.aa + " " + s2.aa);
    }
}
/* Output
10 10 TCA@15db9742 TCA@15db9742
100 10 TCA@15db9742 TCA@15db9742
*/
