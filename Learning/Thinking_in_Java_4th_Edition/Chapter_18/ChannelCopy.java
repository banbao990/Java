/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.nio.*;
import java.nio.channels.*;
import java.io.*;

public class ChannelCopy {
    private static final int BSIZE = 1024;
    public static void main(String[] args) throws Exception {
        if(args.length == 2) {
            copyM(args[0], args[1]);
        } else {
            copyM("Sample.txt", "ChannelCopy.out");
        }
    }

    private static void copyM (String src, String dst) throws Exception {
        copy2(src, dst);
    }
    
    // 直接连接两个通道
    public static void copy2 (String src, String dst) throws Exception {
        FileChannel
            in = new FileInputStream(src).getChannel(),
            out = new FileOutputStream(dst).getChannel();
        in.transferTo(0, in.size(), out);
        // out.transferFrom(in, 0, in.size());
    }

    // 两个通道独立
    public static void copy (String src, String dst) throws Exception {
        FileChannel
            in = new FileInputStream(src).getChannel(),
            out = new FileOutputStream(dst).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
        while(in.read(buffer) != -1) {
            buffer.flip(); // Prepare for writing
            out.write(buffer);
            buffer.clear(); // Prepare for reading
        }
    }
}
