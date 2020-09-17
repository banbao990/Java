/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
public class PriorityInConstructor implements Runnable {
    public PriorityInConstructor(int priority) {
        // 在构造函数中设置优先级
        Thread.currentThread().setPriority(priority);
    }
    @Override
    public void run() {}
    public static void main(String...args) {
        System.out.println(Thread.currentThread().getPriority());
        for(int i = 0;i < 5; ++i) {
            new Thread(new PriorityInConstructor(1)).start();
        }
        System.out.println(Thread.currentThread().getPriority());
    }
}
/* Output
5
1
*/
