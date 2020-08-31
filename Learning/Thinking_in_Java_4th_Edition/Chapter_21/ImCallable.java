/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
public class ImCallable implements Callable<String> {
    static int cnt = 0;
    private int id = ++cnt;
    @Override
    public String call() {
        Thread.yield(); // 为了让线程的调度更明显
        String ret = String.format("%d@run()", id);
        System.out.println(ret);
        return ret;
    }
    public static void main(String...args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<String>> result = new ArrayList<>();
        for(int i = 0;i < 5; ++i) {
            result.add(exec.submit(new ImCallable()));
        }
        System.out.println("~~~~~~~~~~");
        for(Future<String> fs : result) {
            try {
                /**
                 * get() 阻塞
                 *
                 * Waits if necessary for the computation to complete, and then
                 * retrieves its result.
                 */
                System.out.println(fs.get());
            } catch(InterruptedException e) {
                System.out.println(e);
                return;
            } catch(ExecutionException e) {
                System.out.println(e);
            } finally {
                exec.shutdown();
            }
        }
    }
}

/* Output(one possible output)
~~~~~~~~~~
3@run()
2@run()
1@run()
5@run()
4@run()
1@run()
2@run()
3@run()
4@run()
5@run()
*/
