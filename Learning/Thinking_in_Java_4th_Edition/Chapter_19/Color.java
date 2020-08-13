/**
 * @author banbao
 * @comment 修改自示例代码
 */

public interface Color {
    public enum Red implements Color {
        RED,
        PINK,
        MAROON, // 栗色
        SCARLET, // 绯红, 猩红
        ROSYBROWN, // 红褐色
    }
    public enum Blue implements Color {
        BLUE,
        SKYBLUE,
        SLATEBLUE, // 石蓝色
    }
    public enum Green implements Color {
        GREEN,
        SPRINGGREEN, // 春绿色
        SEAGREEN,
    }
    public static void main(String...args) {
        Color color = Green.GREEN;
        System.out.println((color = Enums.random(Red.class)));
        System.out.println((color = Enums.random(Green.class)));
        System.out.println((color = Enums.random(Blue.class)));
    }
}


/* Output
SCARLET
SEAGREEN
SKYBLUE
*/