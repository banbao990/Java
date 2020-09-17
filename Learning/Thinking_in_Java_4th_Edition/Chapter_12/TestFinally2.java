/**
 * @author banbao
 */
import java.io.*;
public class TestFinally2{
    public static void main(String...args){
        boolean[] test = {true, false};
        /////////// return //////////
        System.out.println("~~~~~test return~~~~~");
        test(11);
        /////////// break ///////////
        System.out.println("~~~~~test break~~~~~");
        test(5);
    }

    static void test(int t) { 
        try{
            if(t >= 10) return;
            for(int i = 0; i < 10; ++i){
                System.out.println(i);
                if(i == t) break;
            }
        } finally{
            System.out.println("Clean up!");
        }
    }
}

/*
~~~~~test return~~~~~
Clean up!
~~~~~test break~~~~~
0
1
2
3
4
5
Clean up!
*/
