/**
 * @author banbao
 * 多维数组
 */
import java.util.Arrays;

public class MultiLength {
    public static void main(String...args) {
        // 方便的创建方式
        SerialA[][] sa = {
            {new SerialA()},
            {new SerialA(), new SerialA() },
            {new SerialA(), new SerialA(), new SerialA() },
        };
        System.out.println(Arrays.deepToString(sa));
        
        // 其他方式
        int[][] a = new int[10][10];
        // int[][] b = new int[][10]; // ERROR
        int[][] c = new int[10][];
        int d[][] = new int[10][];
        int[] e[] = new int[10][];

        System.out.println(Arrays.deepToString(a));
        // System.out.println(Arrays.deepToString(b));
        System.out.println(Arrays.deepToString(c));
        System.out.println(Arrays.deepToString(d));
        System.out.println(Arrays.deepToString(e));
    }
}
/* Output
[[A_serialNumber_1], [A_serialNumber_2, A_serialNumber_3], [A_serialNumber_4, A_serialNumber_5, A_serialNumber_6]]
[[0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]
[null, null, null, null, null, null, null, null, null, null]
[null, null, null, null, null, null, null, null, null, null]
[null, null, null, null, null, null, null, null, null, null]
*/