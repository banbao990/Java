/**
 * @author banbao
 * @comment �޸���ʾ������
 */

public interface Color {
    public enum Red implements Color {
        RED,
        PINK,
        MAROON, // ��ɫ
        SCARLET, // 糺�, �ɺ�
        ROSYBROWN, // ���ɫ
    }
    public enum Blue implements Color {
        BLUE,
        SKYBLUE,
        SLATEBLUE, // ʯ��ɫ
    }
    public enum Green implements Color {
        GREEN,
        SPRINGGREEN, // ����ɫ
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