/**
 * @author banbao
 */

public class Test01{
    public static void main(String[] args){
        System.out.println("test1:&&");
        boolean t1 = (falseFunc() && trueFunc());
        System.out.println("test2:||");
        boolean t2 = (trueFunc() || falseFunc());
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

/*
 * output :
 *
 * test1:&&
 * FALSE
 * test2:||
 * TRUE
 */
