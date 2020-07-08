// ʾ������
// ���ƿ��

import java.util.*;

public class Controller{
    // �����¼����б�
    private List<Event> eventList = new ArrayList<Event>();
    public void addEvent(Event e){
        this.eventList.add(e);
    }
    // ����,����Ѿ�׼���õ��¼�,���ִ��
    public void run(){
        while(this.eventList.size() > 0){
            // ����һ������, Ϊ�˷�ֹ�ڵ�����ʱ������ remove ���µ�����
            // ͬ�������¼��ĸ���
            for(Event e : new ArrayList<Event>(eventList)){
                if(e.ready()){
                    System.out.println(e);
                    e.action();
                    eventList.remove(e);
                }
            }
        }
    }
}