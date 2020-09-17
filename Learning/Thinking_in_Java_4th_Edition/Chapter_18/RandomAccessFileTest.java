/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;
import java.io.*;

public class RandomAccessFileTest {
    public static void main(String...args) throws Exception {
        RandomAccessFile rf = new RandomAccessFile("rf.dat", "rw");
        rf.setLength(0);
        double[] ds = {10.0,1.0,0.1,0.01,0.001};
        long pos = 0;
        for(int i = 0;i < 5; ++i) {
            rf.writeDouble(ds[i]);
            if(i == 3) { pos = rf.getFilePointer(); }
        }
        rf.close();
        
        display();

        rf = new RandomAccessFile("rf.dat", "rw");
        rf.seek(pos);
        rf.writeDouble(1.111111);
        rf.close();
        display();
        System.out.println("\npos:" + pos);
    }

    public static void clear() throws Exception {
        RandomAccessFile rf = new RandomAccessFile("rf.dat", "rw");
        rf.setLength(0);
        rf.close();
    }
    
    public static void display() throws Exception {
        System.out.println("~~~~~~~~~~");
        RandomAccessFile rf = new RandomAccessFile("rf.dat", "r");
        long length = rf.length();
        while(rf.getFilePointer() != length) {
            System.out.println(rf.readDouble());
        }
        rf.close();
    }

}


/* Output
~~~~~~~~~~
10.0
1.0
0.1
0.01
0.001
~~~~~~~~~~
10.0
1.0
0.1
0.01
1.111111

pos:32
*/
