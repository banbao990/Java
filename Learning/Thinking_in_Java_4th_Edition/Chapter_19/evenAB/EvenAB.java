/**
 * @author banbao
 * @comment 接受偶数个a, 偶数个b的序列
 */

package evenAB;
import static evenAB.AB.*;
import static evenAB.State.*;
import java.util.EnumSet;
import java.util.Scanner;
public class EvenAB {
    private static Scanner sc = new Scanner(System.in);
    private static final String tip = 
        "This program accept a sequence consists of 'a' and 'b'\n"
        + "One or more chars each line\n"
        + "Remeber that if there is an illegal char the line, "
        + "the whole will be dropped!\n"
        + "'0' stands for the exit char!\n";
    private static boolean exitFlag;
    private static EnumSet<State> acceptedState =
        EnumSet.of(AEBE);
    private static State state;
    private static void print(String seq) {
        System.out.println(seq);
    }
    private static String check(String str) {
        str = str.replaceAll(" ", "");
        for(int i = 0;i < str.length(); ++i) {
            char now = str.charAt(i);
            if(now == 'a' || now == 'b') continue;
            if(now == '0' && i == (str.length() - 1)) {
                exitFlag = true;
                continue;
            }
            return null;
        }
        if(exitFlag) { str = str.substring(0, str.length() - 1); }
        return str;
    }
    public static AB get(char a) {
        if(a == 'a') { return A; }
        else if(a == 'b') { return B; }
        else throw new RuntimeException(new NotABException());
    }
    public static void DFA(final String str) {
        for(int i = 0;i < str.length(); ++i) {
            state = state.next(get(str.charAt(i)));
        }
    }
    public static void main(String...args) {
        print(tip);
        print("Input your sequence:");
        StringBuilder sb = new StringBuilder();
        exitFlag = false;
        state = AEBE;
        while(!exitFlag) {
            String str = sc.nextLine();
            str = check(str);
            if(str == null) { print("Illegal Input!"); }
            else { 
                DFA(str);
                sb.append(str);
            }
            print("next?");
        }
        print("Your sequence:" + sb.toString());
        if(acceptedState.contains(state)) { print("Accepeted!"); }
        else { print("Refused!"); }
    }
}


/* Output
*/