/**
 * @author banbao
 * @comment �޸���ʾ������
 */ 

// ������񹤳��ӿ�
interface Service {
    void method1();
    void method2();
}

// ���幤���ӿ�
interface ServiceFactory {
    Service getService();
}

// ���񹤳��ӿ�ʵ�� 1
class Implementation1 implements Service {
    Implementation1() {} // Package access
    public void method1() {System.out.println("Implementation1 method1");}
    public void method2() {System.out.println("Implementation1 method2");}
}

// �����ӿ�ʵ�� 1
class Implementation1Factory implements ServiceFactory {
    public Service getService() {
        return new Implementation1();
    }
}

// ���񹤳��ӿ�ʵ�� 2
class Implementation2 implements Service {
    Implementation2() {} // Package access
    public void method1() {System.out.println("Implementation2 method1");}
    public void method2() {System.out.println("Implementation2 method2");}
}

// �����ӿ�ʵ�� 2
class Implementation2Factory implements ServiceFactory {
    public Service getService() {
        return new Implementation2();
    }
}

public class Factories {
    // һ���������ܽӿ���Ϊ����, ����ʵ���ṩ��ͬ�ķ���
    // ��˱����ʵ��,���޸�������������ṩ����ķ�����
    //     1. ���ӷ��񹤳����ṩ�ķ���(��)
    //     2. �����޸��������
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