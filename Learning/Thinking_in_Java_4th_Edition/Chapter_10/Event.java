// ʾ������
// �¼��ӿ�

public abstract class Event{
    private long eventTime;
    protected final long delayTime;
    public Event(long delayTime){
        this.delayTime = delayTime;
        start();
    }
    // �͹���������,������ʱ������֮������������ʱ��
    // �ظ�һ��ͬ�����¼�
    public void start(){
        eventTime = System.currentTimeMillis() + delayTime;
    }
    // ��ʱ���Կ�ʼ���и��¼�( action ����)
    public boolean ready(){
        return System.currentTimeMillis() >= eventTime;
    }
    // ���ڼ̳�ʵ�ֵ� action ����
    public abstract void action();
}