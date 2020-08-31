/**
 * @author banbao
 * @comment 修改自示例代码
 */

public class JustRun implements Runnable {
    static int cnt = 0;
    private int id = ++cnt;
    @Override
    public void run() {
        // 只是执行一个输出
        Thread.yield(); // 为了让线程的调度更明显
        System.out.format("%d@run()\n", id);
    }
    public static void main(String...args) {
        for(int i = 0;i < 5; ++i) {
            new Thread(new JustRun()).start();
        }
    }
}

/* Output(one possible output)
1@run()
5@run()
3@run()
4@run()
2@run()
*/
