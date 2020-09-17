/**
 * @author banbao
 * 多维数组
 */

public class SerialA {
    private static long serial = 0;
    public final long ID;
    public SerialA(){
        this.ID = ++serial;
    }
    @Override
    public String toString(){
        return "A_serialNumber_" + this.ID;
    }
}
