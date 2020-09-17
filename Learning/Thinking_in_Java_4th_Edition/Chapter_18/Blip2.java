/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.io.*;

public class Blip2 implements Externalizable {
    public int cc = 0;
    public Blip2() {
        System.out.println("Blip2 Constructor");
    }
    public void writeExternal(ObjectOutput out)
        throws IOException {
        System.out.println("Blip2.writeExternal");
        out.writeInt(cc);
    }
    public void readExternal(ObjectInput in)
        throws IOException, ClassNotFoundException {
        System.out.println("Blip2.readExternal");
        cc = in.readInt();
    }
    public static void main(String[] args)
    throws IOException, ClassNotFoundException {
        System.out.println("Constructing objects:");
        Blip2 b1 = new Blip2();
        System.out.println("b1.cc:" + b1.cc);
        b1.cc = 10086;
        System.out.println("b1.cc:" + b1.cc);
        ObjectOutputStream o = new ObjectOutputStream(
            new FileOutputStream("Blip2.out"));
        System.out.println("Saving objects:");
        o.writeObject(b1);
        o.close();
        // Now get them back:
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream("Blips.out"));
        System.out.println("Recovering b1:");
        b1 = (Blip2)in.readObject();
        System.out.println("b1.cc:" + b1.cc);
    }
} 
/* Output:
Constructing objects:
Blip2 Constructor
b1.cc:0
b1.cc:10086
Saving objects:
Blip2.writeExternal
Recovering b1:
Blip2 Constructor
Blip2.readExternal
b1.cc:10086
*/
