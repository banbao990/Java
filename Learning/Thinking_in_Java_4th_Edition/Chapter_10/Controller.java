// 示例代码
// 控制框架

import java.util.*;

public class Controller{
    // 保存事件的列表
    private List<Event> eventList = new ArrayList<Event>();
    public void addEvent(Event e){
        this.eventList.add(e);
    }
    // 阻塞,检查已经准备好的事件,逐个执行
    public void run(){
        while(this.eventList.size() > 0){
            // 进行一个拷贝, 为了防止在迭代的时候由于 remove 导致的问题
            // 同样还有事件的干扰
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