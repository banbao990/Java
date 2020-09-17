/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.util.concurrent.*;
public class UseJustRunExecutor {
    public static void main(String...args) {
        // ExecutorService exec = Executors.newSingleThreadExecutor();
        // ExecutorService exec = Executors.newFixedThreadPool();
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0;i < 5; ++i) {
            exec.execute(new JustRun());
        }
        exec.shutdown(); // 拒绝之后台添加新的任务到线程池中
        // exec.execute(new JustRun()); 
        // java.util.concurrent.RejectedExecutionException
    }
}


/* Output: newSingleThreadExecutor
1@run()
2@run()
3@run()
4@run()
5@run()
*/

// newFixedThreadPool, newCachedThreadPool
/* Output:(one possible output)
1@run()
5@run()
4@run()
2@run()
3@run()
*/
