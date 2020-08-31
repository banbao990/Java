/**
 * @author banbao
 * @comment �޸���ʾ������
 */

public class JustRun implements Runnable {
    static int cnt = 0;
    private int id = ++cnt;
    @Override
    public void run() {
        // ֻ��ִ��һ�����
        Thread.yield(); // Ϊ�����̵߳ĵ��ȸ�����
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
