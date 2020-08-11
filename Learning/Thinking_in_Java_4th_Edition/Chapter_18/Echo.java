/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.io.*;

public class Echo {
    public static void main(String...args) 
        throws Exception {
        BufferedReader br = new BufferedReader(
            new InputStreamReader(System.in)
        );
        String s;
        // Ctrl-Z / Ctrl-C
        while((s = br.readLine()) != null) {
            System.out.println(s);
        }
    }
}


/* Output
*/