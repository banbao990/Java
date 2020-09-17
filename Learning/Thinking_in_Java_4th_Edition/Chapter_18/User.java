/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.io.Serializable;
public class User implements Serializable {
    public final String username;
    private transient String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public String toString() {
        return "username:" + this.username
                + ", password:" + this.password;
    }
}
