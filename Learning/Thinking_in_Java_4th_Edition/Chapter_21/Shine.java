/**
 * @author banbao
 * @comment �޸���ʾ������
 */

public class Shine implements Runnable {
    static int cnt = 0;
    static final int N = 10000;
    @Override
    public void run() {
        ++cnt; // �̲߳���ȫ��
        if(cnt == N) {
            System.out.format("reach \'N\'(%d)\n", N);
        }
    }
    public static void main(String...args) throws Exception {
        for(int i = 0;i < N; ++i) {
            new Thread(new Shine()).start();
        }
    }
}

/* Output1
*/

/* Output2
reach 'N'(10000)
*/
