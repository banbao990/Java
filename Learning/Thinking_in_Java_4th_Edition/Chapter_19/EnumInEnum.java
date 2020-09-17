/**
 * @author banbao
 * @comment 修改自示例代码
 */

public enum EnumInEnum {
    RED(EnumInEnum.Color.Red.class), 
    BLUE(EnumInEnum.Color.Blue.class), 
    GREEN(EnumInEnum.Color.Green.class);
    private Color[] values; // read only
    private EnumInEnum(Class<? extends Color> kind) {
        values = kind.getEnumConstants();
    }
    public Color randomSelection() {
        return Enums.random(values);
    }

    public interface Color {
        public enum Red implements Color {
            RED, PINK, MAROON, SCARLET, ROSYBROWN,
        }
        public enum Blue implements Color {
            BLUE, SKYBLUE, SLATEBLUE,
        }
        public enum Green implements Color {
            GREEN, SPRINGGREEN, SEAGREEN,
        }
    }
    
    public static void main(String...args) {
        for(int i = 0;i < 5; ++i) {
            EnumInEnum eoc = Enums.random(EnumInEnum.class);
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
