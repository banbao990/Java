/**
 * @author banbao
 */

class PriClass {
    // 私有构造器
    private PriClass(){}
}

class NoDefaultClass {
    // 没有默认构造器
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