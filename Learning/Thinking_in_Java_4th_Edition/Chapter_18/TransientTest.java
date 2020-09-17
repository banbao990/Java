/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;
import java.io.*;

public class TransientTest {
    public User user;
    public static void main(String...args) 
    throws Exception {
        TransientTest tt = new TransientTest();
        tt.user = new User("banbao", "banbao990");
        store(tt.user);
        tt.user = new User("error", "error");
        HolderObject temp = new HolderObject(tt.user);
        restore(temp);
        tt.user = (User)temp.o;
        System.out.println(tt.user);
    }
    
    public static void store(Object o) throws Exception {
        store(new Object[]{o}, "TransientTest.out");
    }
    
    // Overload
    public static void store(Object o, String fileName)
    throws Exception {
        store(new Object[]{o}, fileName);
    }
    
    // Overload
    public static void store(Object[] o, String fileName)
        throws Exception {
        System.err.println("Store...");
        // serialization
        ObjectOutputStream out = 
            new ObjectOutputStream(
                new FileOutputStream(fileName));
        for(Object obj : o) {
            System.out.println(obj);
            out.writeObject(obj);
        }
        out.close(); // auto flush
    }
    
    public static void restore(HolderObject o) throws Exception {
        restore(new HolderObject[]{o}, "TransientTest.out");
    }
    
    // Overload
    public static void restore(HolderObject o, String fileName) 
        throws Exception {
        restore(new HolderObject[]{o}, fileName);
    }
    
    // Overload
    public static void restore(HolderObject[] o, String fileName)
        throws Exception {
        System.err.println("Restore...");
        // deserialization
        ObjectInputStream in = 
            new ObjectInputStream(
                new FileInputStream(fileName));
        for(int i = 0;i < o.length; ++i) {
            o[i].o = in.readObject();
            System.out.println(o[i].o);
        }
    }
}


/* Output
Store...
username:banbao, password:banbao990
Restore...
username:banbao, password:null
username:banbao, password:null
*/
