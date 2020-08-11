/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;
import java.io.*;

public class TestOSExecute {
    public static void main(String...args) {
        if(args.length == 0) {
            System.err.println("Lack of command line!");
            System.exit(1);
        }
        OSExecute.command(args);
    }
}


/* Output
*/