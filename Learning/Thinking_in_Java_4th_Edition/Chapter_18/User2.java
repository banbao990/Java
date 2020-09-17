/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.io.*;
public class User2 implements Serializable {
    public final String username;
    private transient String password;
    public User2(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public String toString() {
        return "username:" + this.username
                + ", password:" + this.password;
    }
    
    private void writeObject(ObjectOutputStream stream)
    throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(password);
    }

    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException{
        stream.defaultReadObject();
        password = (String)stream.readObject();
    }
    
    public static void main(String...args) 
        throws Exception {
        User2 user = new User2("banbao", "banbao990");
        TransientTest.store(user);
        user = new User2("error", "error");
        HolderObject temp = new HolderObject(user);
        TransientTest.restore(temp);
        user = (User2)temp.o;
        System.out.println(user);
    }
}
/*
Output:
Store...
username:banbao, password:banbao990
Restore...
username:banbao, password:banbao990
username:banbao, password:banbao990
*/
