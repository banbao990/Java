/**
 * @author banbao
 */

public class TestStandardException{
    public static void main(String...args){
        try{
            test1();
        } catch (Exception e){
            System.out.println(e);
        }
        ////////////////////
        try{
            test2();
        } catch (Exception e){
            System.out.println(e);
        }
        /////////////
        test3();
    }

    static void test1() throws Exception{
        throw new Exception();
    }

    static void test2() throws Exception{
        throw new Exception("test2");
    }

    static void test3(){
        try{
            throw new Exception();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

/*
java.lang.Exception
    at TestStandardException.test3(TestStandardException.java:32)
    at TestStandardException.main(TestStandardException.java:19)
*/
