/**
 * @author banbao
 * ��ά����
 */
import java.util.Arrays;

public class MultiArray {
    public static void main(String...args) {
        // ����Ĵ�����ʽ
        SerialA[][] a = {
            {new SerialA()},
            {new SerialA()},
        };
        System.out.println(Arrays.deepToString(a));
    }
}
/* Output
[[A_serialNumber_1], [A_serialNumber_2]]
*/