/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.lang.reflect.Proxy;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

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

class DynamicProxy implements InvocationHandler {
    private Connectable ro;
    public DynamicProxy(Connectable ro) {
        this.ro = ro;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
        System.out.println("**** proxy: " + proxy.getClass() +
            ", method: " + method + ", args: " + args);
        if(args != null) {
            for(Object arg : args) {
                System.out.println("  " + arg);
            }
        }
        return method.invoke(ro, args);
    }
}

public class TestDynamicProxy {
    public static void run(Connectable cd) {
        cd.connect();
        cd.close();
    }
    
    public static void main(String...args) {
        RealObject ro = new RealObject();
        run(ro);
        // proxy
        Connectable proxy = (Connectable) Proxy.newProxyInstance(
            Connectable.class.getClassLoader(),
            new Class[]{ Connectable.class },
            new DynamicProxy(ro)
        );
        run(proxy);
    }
}
/* Output
RealObject connect...
RealObject close...
**** proxy: class $Proxy0, method: public abstract void Connectable.connect(), args: null
RealObject connect...
**** proxy: class $Proxy0, method: public abstract void Connectable.close(), args: null
RealObject close...
*/