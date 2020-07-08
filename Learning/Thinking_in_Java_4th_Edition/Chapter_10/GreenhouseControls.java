// ʾ������
// ͨ���ڲ���ʵ�ָ����¼�

public class GreenhouseControls extends Controller {

    // �ƹ�״̬
    private boolean light = false;
    // �����¼�
    public class LightOn extends Event {
        public LightOn(long delayTime) { super(delayTime); }
        public void action() {
            // ����
            // ����Ӳ�����Ʋ���
            light = true;
        }
        public String toString() { return "Light is on"; }
    }
    // �ص��¼�
    public class LightOff extends Event {
        public LightOff(long delayTime) { super(delayTime); }
        public void action() {
            // �ص�
            // ����Ӳ�����Ʋ���
            light = false;
        }
        public String toString() { return "Light is off"; }
    }
    
    // ˮ��״̬
    private boolean water = false;
    // ��ˮ�¼�
    public class WaterOn extends Event {
        public WaterOn(long delayTime) { super(delayTime); }
        public void action() {
            // ��ˮ
            // ����Ӳ�����Ʋ���
            water = true;
        }
        public String toString() {
            return "Greenhouse water is on";
        }
    }
    // ��ˮ�¼�
    public class WaterOff extends Event {
        public WaterOff(long delayTime) { super(delayTime); }
        public void action() {
            // ��ˮ
            // ����Ӳ�����Ʋ���
            water = false;
        }
        public String toString() {
            return "Greenhouse water is off";
        }
    }
    
    // ����״̬
    private String thermostat = "Day";
    // ����״̬����Ϊҹ��
    public class ThermostatNight extends Event {
        public ThermostatNight(long delayTime) {
            super(delayTime);
        }
        public void action() {
            // ����Ϊҹ��
            // ����Ӳ�����Ʋ���
            thermostat = "Night";
        }
        public String toString() {
            return "Thermostat on night setting";
        }
    }
    // ����״̬����Ϊ����
    public class ThermostatDay extends Event {
        public ThermostatDay(long delayTime) {
            super(delayTime);
        }
        public void action() {
            // ����Ϊ����
            // ����Ӳ�����Ʋ���
            thermostat = "Day";
        }
        public String toString() {
            return "Thermostat on day setting";
        }
    }
    
    // �������
    public class Bell extends Event {
        public Bell(long delayTime) { super(delayTime); }
        public void action() {
            // ����һ����������
            // Ч���൱��ÿ��һ���׶�ִ����������
            // ����Ӳ�����Ʋ���
            addEvent(new Bell(delayTime));
        }
        public String toString() { return "Bing!"; }
    }
    
    // ��������
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
                // ����Ϊ��������������¼���������
                e.start();
                addEvent(e);
            }
            // �����¼�����
            start();
            addEvent(this);
        }
        public String toString() {
            return "Restarting system";
        }
    }
    
    // ��ֹ����,ֹͣ�����¼���ִ��
    public static class Terminate extends Event {
        public Terminate(long delayTime) { super(delayTime); }
        public void action() { System.exit(0); }
        public String toString() { return "Terminating";    }
    }
}
