/**
 * @author banbao
 * @comment 修改自示例代码
 */ 

// 定义服务工厂接口
interface Service {
    void method1();
    void method2();
}

// 定义工厂接口
interface ServiceFactory {
    Service getService();
}

// 服务工厂接口实现 1
class Implementation1 implements Service {
    Implementation1() {} // Package access
    public void method1() {System.out.println("Implementation1 method1");}
    public void method2() {System.out.println("Implementation1 method2");}
}

// 工厂接口实现 1
class Implementation1Factory implements ServiceFactory {
    public Service getService() {
        return new Implementation1();
    }
}

// 服务工厂接口实现 2
class Implementation2 implements Service {
    Implementation2() {} // Package access
    public void method1() {System.out.println("Implementation2 method1");}
    public void method2() {System.out.println("Implementation2 method2");}
}

// 工厂接口实现 2
class Implementation2Factory implements ServiceFactory {
    public Service getService() {
        return new Implementation2();
    }
}

public class Factories {
    // 一个函数接受接口作为参数, 可以实现提供不同的服务
    // 如此便可以实现,不修改这个函数就能提供更多的服务功能
    //     1. 增加服务工厂和提供的服务(类)
    //     2. 而不修改这个函数
    public static void serviceConsumer(ServiceFactory fact) {
        Service s = fact.getService();
        s.method1();
        s.method2();
    }
    public static void main(String[] args) {
        serviceConsumer(new Implementation1Factory());
        // Implementations are completely interchangeable:
        serviceConsumer(new Implementation2Factory());
    }
} 
/* 
 * output:
 * 
 * Implementation1 method1
 * Implementation1 method2
 * Implementation2 method1
 * Implementation2 method2
 * 
 */