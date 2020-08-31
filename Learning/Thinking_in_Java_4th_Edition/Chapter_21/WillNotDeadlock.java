/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
public class WillNotDeadlock implements Runnable {
    public synchronized void f() { g(); }
    public synchronized void g() {}
    @Override
    public void run(){
        f();
    }
    public static void main(String...args) {
        new Thread(new WillNotDeadlock()).start();
    }
}
/* Output
*/