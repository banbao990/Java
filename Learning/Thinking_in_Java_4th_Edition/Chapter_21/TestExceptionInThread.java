/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
public class TestExceptionInThread {
    public static void main(String...args) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    throw new RuntimeException("ERROR");
                }
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}

/* Output
*/