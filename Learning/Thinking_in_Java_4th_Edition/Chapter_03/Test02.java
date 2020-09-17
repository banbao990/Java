/**
 * @author banbao
 */

public class Test02{
    public static void main(String[] args){
        boolean a = trueFunc() ? trueFunc() : falseFunc();
    }
    
    public static boolean falseFunc(){
        System.out.println("FALSE");
        return false;
    }
    
    public static boolean trueFunc(){
        System.out.println("TRUE");
        return true;
    }
}
