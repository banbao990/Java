/**
 * @author banbao
 * @comment 修改自示例代码
 */

public enum EnumOfColor2 {
    RED(Color.Red.class), 
    BLUE(Color.Blue.class), 
    GREEN(Color.Green.class);
    private Color[] values; // read only
    private EnumOfColor2(Class<? extends Color> kind) {
        values = kind.getEnumConstants();
    }
    public Color randomSelection() {
        return Enums.random(values);
    }
    public static void main(String...args) {
        for(int i = 0;i < 5; ++i) {
            EnumOfColor2 eoc = Enums.random(EnumOfColor2.class);
            Color color = eoc.randomSelection();
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