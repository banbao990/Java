/**
 * @author banbao
 * @comment 修改自示例代码
 */
import number.Number;
public class SimpleEnum {
    public static void main(String...args) {
        final String seg = "----------------------";
        for(Number num : Number.values()) {
            System.out.println(
                num + " ordinal: " + num.ordinal() + "\n"
                + num.compareTo(Number.ZERO) + " " 
                + num.equals(Number.ZERO) + " "
                + (num == Number.ZERO) + "\n"
                + num.getDeclaringClass() + "\n"
                + num.name() + "\n"
                + seg
            );
        }
        // 从字符串中生成
        for(String s : "ZERO ONE TWO".split(" ")) {
            Number num = Enum.valueOf(Number.class, s);
            System.out.println(num);
        }
        System.out.println(seg);
        // 直接用名称
        System.out.println(Number.ZERO);
    }
}


/* Output
ZERO ordinal: 0
0 true true
class number.Number
ZERO
----------------------
ONE ordinal: 1
1 false false
class number.Number
ONE
----------------------
TWO ordinal: 2
2 false false
class number.Number
TWO
----------------------
THREE ordinal: 3
3 false false
class number.Number
THREE
----------------------
FOUR ordinal: 4
4 false false
class number.Number
FOUR
----------------------
FIVE ordinal: 5
5 false false
class number.Number
FIVE
----------------------
ZERO
ONE
TWO
----------------------
ZERO
*/
