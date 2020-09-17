/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.concurrent.*;
import java.util.*;
class EDProducerHolder {}
class EDConsumerHolder {}
class EDProducer implements Runnable {
    private Exchanger<Object> ex;
    private Object holder = new EDProducerHolder();
    public EDProducer(Exchanger<Object> ex) {
        this.ex = ex;
    }
    @Override
    public void run() {
        try {
            holder = ex.exchange(holder);
        } catch(InterruptedException e) {
          // OK to terminate this way.
        }
        System.out.println(
            this.getClass().getName() 
            + " holds " 
            + holder.getClass().getName()
        );
    }
}

class EDConsumer implements Runnable {
    private Exchanger<Object> ex;
    private Object holder = new EDConsumerHolder();
    public EDConsumer(Exchanger<Object> ex) {
        this.ex = ex;
    }
    @Override
    public void run() {
        try {
            holder = ex.exchange(holder);
        } catch(InterruptedException e) {
          // OK to terminate this way.
        }
        System.out.println(
            this.getClass().getName() 
            + " holds " 
            + holder.getClass().getName()
        );
    }
}

public class ExchangerDemo {
    public static void main(String...args) {
        Exchanger<Object> ex = new Exchanger<>();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new EDProducer(ex));
        exec.execute(new EDConsumer(ex));
        exec.shutdown();
    }
}


/* Output
EDConsumer holds EDProducerHolder
EDProducer holds EDConsumerHolder
*/
