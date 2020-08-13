/**
 * @author banbao
 * @comment 修改自示例代码(可以作为一个工具)
 */
import java.util.Random;
public class Enums {
    private static Random rand = new Random(47);
    public static <T extends Enum<T>> T random(Class<T> enumClass) {
        return random(enumClass.getEnumConstants());
    }
    public static <T> T random(T[] values) {
        if(values == null || values.length == 0) {
            return null;
        }
        return values[rand.nextInt(values.length)];
    }
    // test
    public static void main(String...args)
        throws Exception {
        for(int i = 0;i < 10; ++i) {
            System.out.println(Enums.random(Signal.class));
        }
    }
}


/* Output
YELLOW
YELLOW
GREEN
YELLOW
GREEN
YELLOW
GREEN
YELLOW
RED
GREEN
*/