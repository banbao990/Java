// 示例代码
// 事件接口

public abstract class Event{
    private long eventTime;
    protected final long delayTime;
    public Event(long delayTime){
        this.delayTime = delayTime;
        start();
    }
    // 和构造器独立,可以在时间运行之后重新启动计时器
    // 重复一个同样的事件
    public void start(){
        eventTime = System.currentTimeMillis() + delayTime;
    }
    // 何时可以开始运行该事件( action 方法)
    public boolean ready(){
        return System.currentTimeMillis() >= eventTime;
    }
    // 用于继承实现的 action 方法
    public abstract void action();
}