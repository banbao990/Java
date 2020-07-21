/**
 * @author banbao
 * @comment Scanner
 */

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.StringTokenizer;

public class TestScanner{
    static StringBuilder sb = new StringBuilder();
    public static void main(String...args){
        // test 1
        testInt("3", "12345");     // "12 45"
        testInt("3", "12xx3xx12"); // ""
        // test 2:regex
        testInt("\\s*,\\s*", "1, 2 ,3  ,4 ,5 ,6");  // "1 2 3 4 5 6"
        // regex
        testRegex("a", "123", "a123a123z123"); // "123"
        // StringTokenizer
        testStringTokenizer();
        System.out.println(sb); // "StringTokennizer is deprecated!"
    }
    public static void testStringTokenizer() {
        String input = "StringTokennizer is deprecated!";
        StringTokenizer stoke = new StringTokenizer(input);
        boolean first = true;   
        sb.append("\"");
        while(stoke.hasMoreElements()) {
            if(first) first = false;
            else sb.append(" ");
            sb.append(stoke.nextToken()); 
        }
        sb.append("\"");
        sb.append("\n"); 
    }
    
    // Regex
    public static void testRegex(String delimiter, String regex, String str) {
        Scanner sc = new Scanner(str);
        sc.useDelimiter(delimiter);
        boolean first = true;
        sb.append("\"");
        while(sc.hasNext(regex)) {
            if(first) first = false;
            else sb.append(" ");
            sc.next(regex);
            MatchResult match = sc.match();
            sb.append(match.group()); 
        }
        sb.append("\"");
        sb.append("\n"); 
    }
    
    // Int
    public static void testInt(String regex, String str) {
        Scanner sc = new Scanner(str);
        sc.useDelimiter(regex);
        boolean first = true;
        sb.append("\"");
        while(sc.hasNextInt()) {
            if(first) first = false;
            else sb.append(" ");
            sb.append(sc.nextInt()); 
        }
        sb.append("\"");
        sb.append("\n"); 
    }
}

/* Output
"12 45"
""
"1 2 3 4 5 6"
"123"
*/