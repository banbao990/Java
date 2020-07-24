/**
 * @author banbao
 */

class PriClass {
    // ˽�й�����
    private PriClass(){}
}

class NoDefaultClass {
    // û��Ĭ�Ϲ�����
    NoDefaultClass(int noUse) {}
}

public class TestNewInstanceEx {
    public static void main(String...args) {
        func(PriClass.class);
        func(NoDefaultClass.class);
    }
    
    public static <T> void func(Class<T> tClass) {
        try{
            tClass.newInstance();
        } catch (InstantiationException e) {
            System.err.println("InstantiationException");
        } catch (IllegalAccessException e) {
            System.err.println("IllegalAccessException");
        } finally {
            System.err.println(tClass.getName() + "\n~~~~~~");
        }
    }
}

/*
IllegalAccessException
PriClass
~~~~~~
InstantiationException
NoDefaultClass
~~~~~~
*/