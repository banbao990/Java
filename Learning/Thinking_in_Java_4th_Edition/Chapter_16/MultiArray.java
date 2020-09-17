/**
 * @author banbao
 * 多维数组
 */
import java.util.Arrays;

public class MultiArray {
    public static void main(String...args) {
        // 方便的创建方式
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
