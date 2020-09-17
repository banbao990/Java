// 示例代码
// 一个控制

public class GreenhouseController {
    public static void main(String[] args) {
        GreenhouseControls gc = new GreenhouseControls();
        gc.addEvent(gc.new Bell(900));
        Event[] eventList = {
            gc.new ThermostatNight(0),
            gc.new LightOn(200),
            gc.new LightOff(400),
            gc.new WaterOn(600),
            gc.new WaterOff(800),
            gc.new ThermostatDay(1400)
        };
        gc.addEvent(gc.new Restart(2000, eventList));
        // 停止的时间由命令行参数给出
        // 默认为 5000
        long terminalTime = 5000;
        if(args.length == 1)
            terminalTime = new Long(args[0]);
        gc.addEvent(new GreenhouseControls.Terminate(terminalTime));
        gc.run();
    }
} 
/* 
 * output:(System.nanotime)
 * 
 * Bing!
 * Thermostat on night setting
 * Light is on
 * Light is off
 * Greenhouse water is on
 * Greenhouse water is off
 * Thermostat on day setting
 * Restarting system
 * Terminating
 * 
 */
 
/*
 * output:(System.currentTimeMillis)
 * 
 * Thermostat on night setting
 * Light is on
 * Light is off
 * Greenhouse water is on
 * Greenhouse water is off
 * Bing!
 * Thermostat on day setting
 * Bing!
 * Restarting system
 * Thermostat on night setting
 * Light is on
 * Light is off
 * Greenhouse water is on
 * Bing!
 * Greenhouse water is off
 * Thermostat on day setting
 * Bing!
 * Restarting system
 * Thermostat on night setting
 * Light is on
 * Light is off
 * Bing!
 * Greenhouse water is on
 * Greenhouse water is off
 * Terminating
 */
