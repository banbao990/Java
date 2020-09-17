/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
class TDOB1 implements Runnable {
    private static volatile int cnt = 0;
    private final int upper = 100;
    private boolean first = true;
    @Override
    public void run() {
        // synchronized(this) {
        while(cnt < upper) {
            Thread.yield();// 这里执行权被抢占
            ++cnt;
        }
        System.out.println(cnt);
        // }
    }
}

public class TestDependingOnBound {
    public static void main(String...args) {
        TDOB1 td = new TDOB1(); 
        // 注意这里需要是同一个对象
        // 否则不同对象会获得不同锁
        for(int i = 0;i < 2; ++i) {
            new Thread(td).start();
        }
    }
}


/* Output1
100
100
*/

/* Output2
101
101
*/
