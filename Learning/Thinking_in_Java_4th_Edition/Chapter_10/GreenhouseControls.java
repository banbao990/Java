// 示例代码
// 通过内部类实现各种事件

public class GreenhouseControls extends Controller {

    // 灯光状态
    private boolean light = false;
    // 开灯事件
    public class LightOn extends Event {
        public LightOn(long delayTime) { super(delayTime); }
        public void action() {
            // 开灯
            // 加入硬件控制操作
            light = true;
        }
        public String toString() { return "Light is on"; }
    }
    // 关灯事件
    public class LightOff extends Event {
        public LightOff(long delayTime) { super(delayTime); }
        public void action() {
            // 关灯
            // 加入硬件控制操作
            light = false;
        }
        public String toString() { return "Light is off"; }
    }
    
    // 水的状态
    private boolean water = false;
    // 开水事件
    public class WaterOn extends Event {
        public WaterOn(long delayTime) { super(delayTime); }
        public void action() {
            // 开水
            // 加入硬件控制操作
            water = true;
        }
        public String toString() {
            return "Greenhouse water is on";
        }
    }
    // 关水事件
    public class WaterOff extends Event {
        public WaterOff(long delayTime) { super(delayTime); }
        public void action() {
            // 关水
            // 加入硬件控制操作
            water = false;
        }
        public String toString() {
            return "Greenhouse water is off";
        }
    }
    
    // 恒温状态
    private String thermostat = "Day";
    // 恒温状态调整为夜晚
    public class ThermostatNight extends Event {
        public ThermostatNight(long delayTime) {
            super(delayTime);
        }
        public void action() {
            // 调整为夜晚
            // 加入硬件控制操作
            thermostat = "Night";
        }
        public String toString() {
            return "Thermostat on night setting";
        }
    }
    // 恒温状态调整为白天
    public class ThermostatDay extends Event {
        public ThermostatDay(long delayTime) {
            super(delayTime);
        }
        public void action() {
            // 调整为白天
            // 加入硬件控制操作
            thermostat = "Day";
        }
        public String toString() {
            return "Thermostat on day setting";
        }
    }
    
    // 响铃控制
    public class Bell extends Event {
        public Bell(long delayTime) { super(delayTime); }
        public void action() {
            // 增加一个响铃命令
            // 效果相当于每隔一定阶段执行响铃命令
            // 加入硬件控制操作
            addEvent(new Bell(delayTime));
        }
        public String toString() { return "Bing!"; }
    }
    
    // 重启命令
    public class Restart extends Event {
        private Event[] eventList;
        public Restart(long delayTime, Event[] eventList) {
            super(delayTime);
            this.eventList = eventList;
            for(Event e : eventList)
                addEvent(e);
        }
        public void action() {
            for(Event e : eventList) {
                // 将作为参数传入的所有事件进行重置
                e.start();
                addEvent(e);
            }
            // 将该事件重置
            start();
            addEvent(this);
        }
        public String toString() {
            return "Restarting system";
        }
    }
    
    // 终止命令,停止所有事件的执行
    public static class Terminate extends Event {
        public Terminate(long delayTime) { super(delayTime); }
        public void action() { System.exit(0); }
        public String toString() { return "Terminating";    }
    }
}
