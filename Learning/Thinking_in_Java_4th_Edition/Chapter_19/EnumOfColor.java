/**
 * @author banbao
 * @comment 修改自示例代码
 */

public enum EnumOfColor {
    RED(Color.Red.class), 
    BLUE(Color.Blue.class), 
    GREEN(Color.Green.class);
    public Color[] values;
    private EnumOfColor(Class<? extends Color> kind) {
        values = kind.getEnumConstants();
    }
    public static void main(String...args) {
        for(int i = 0;i < 5; ++i) {
            EnumOfColor eoc = Enums.random(EnumOfColor.class);
            Color color = Enums.random(eoc.values);
            System.out.println(color);
        }
    }
}


/* Output
SEAGREEN
SLATEBLUE
SLATEBLUE
SLATEBLUE
MAROON
*/