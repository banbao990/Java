/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.util.concurrent.TimeUnit;
public class JustRunSleepVersion implements Runnable {
    static int cnt = 0;
    private int id = ++cnt;
    @Override
    public void run() {
        try {
            // sleep
            // Thread.sleep(100); // old-style
            TimeUnit.MILLISECONDS.sleep(100); // Java SE 5-6 style
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.format("%d@run()\n", id);
    }
    public static void main(String...args) {
        for(int i = 0;i < 5; ++i) {
            new Thread(new JustRunSleepVersion()).start();
        }
    }
}

/* Output(one possible output)
3@run()
5@run()
1@run()
2@run()
4@run()
*/
