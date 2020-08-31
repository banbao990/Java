/**
 * @author banbao
 * @comment 修改自示例代码
 */

public class SyncOtherObj {
    private Object obj = new Object();
    public synchronized void f() {
        for(int i = 0;i < 10; ++i) {
            System.out.println("f()");
        }        
    }
    public void g() {
        synchronized(this) {
            for(int i = 0;i < 10; ++i) {
                System.out.println("g()");
            }        
        }
    }
    public void h() {
        synchronized(obj) {
            for(int i = 0;i < 10; ++i) {
                System.out.println("h()");
            }        
        }
    }
    public static void main(String[] args) {
        final SyncOtherObj soo = new SyncOtherObj();
        new Thread() {
            public void run() {
                soo.f();
            }
        }.start();
        soo.g();
        // soo.h();
    }
}