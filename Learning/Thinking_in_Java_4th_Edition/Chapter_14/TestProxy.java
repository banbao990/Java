/**
 * @author banbao
 * @comment 修改自示例代码
 */

interface Connectable {
    public void connect();
    public void close();
}

class RealObject implements Connectable {
    public void connect() {
        System.out.println("RealObject connect...");
    }
    public void close() {
        System.out.println("RealObject close...");
    }
}

class SimpleProxy implements Connectable {
    private Connectable ro;
    public SimpleProxy(Connectable ro) {
        this.ro = ro;
    }
    public void connect() {
        System.out.println("SimpleProxy");
        ro.connect();
    }
    public void close() {
        System.out.println("SimpleProxy");
        ro.close();
    }
}

public class TestProxy {
    public static void run(Connectable cd) {
        cd.connect();
        cd.close();
    }
    
    public static void main(String...args) {
        run(new SimpleProxy(new RealObject()));
        run(new RealObject());
    }
}
/* Output
SimpleProxy
RealObject connect...
SimpleProxy
RealObject close...
RealObject connect...
RealObject close...
*/
