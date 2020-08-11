/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.io.*;

public class Blip1 implements Externalizable {
    public int cc = 0;
    public Blip1() {
        System.out.println("Blip1 Constructor");
    }
    public void writeExternal(ObjectOutput out)
        throws IOException {
        System.out.println("Blip1.writeExternal");
    }
    public void readExternal(ObjectInput in)
        throws IOException, ClassNotFoundException {
        System.out.println("Blip1.readExternal");
    }
    public static void main(String[] args)
    throws IOException, ClassNotFoundException {
        System.out.println("Constructing objects:");
        Blip1 b1 = new Blip1();
        System.out.println("b1.cc:" + b1.cc);
        b1.cc = 10086;
        System.out.println("b1.cc:" + b1.cc);
        ObjectOutputStream o = new ObjectOutputStream(
            new FileOutputStream("Blips.out"));
        System.out.println("Saving objects:");
        o.writeObject(b1);
        o.close();
        // Now get them back:
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream("Blip1.out"));
        System.out.println("Recovering b1:");
        b1 = (Blip1)in.readObject();
        System.out.println("b1.cc:" + b1.cc);
    }
} 
/* Output:
Constructing objects:
Blip1 Constructor
b1.cc:0
b1.cc:10086
Saving objects:
Blip1.writeExternal
Recovering b1:
Blip1 Constructor
Blip1.readExternal
b1.cc:0
*/