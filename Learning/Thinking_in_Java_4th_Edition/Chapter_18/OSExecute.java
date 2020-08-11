/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OSExecute {
    public static void command(String[] command) {
        if(command.length == 0) {
             System.out.println("No command line!");
             System.exit(1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(command[0]);
        for(int i = 1; i < command.length; ++i) {
            sb.append(" ");
            sb.append(command[i]);
        }
        command(sb.toString());
    }
    
    public static void command(String command) {
        boolean err = false;
        try {
            // 开一个进程
            Process process =
                new ProcessBuilder(command.split(" ")).start();
            // 获取输入
            BufferedReader results = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            String s;
            while((s = results.readLine())!= null)
                System.out.println(s);
            // 获取错误信息
            BufferedReader errors = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));
            while((s = errors.readLine())!= null) {
                System.err.println(s);
                err = true;
            }
        } catch(Exception e) {
            // Windows 2000
            if(!command.startsWith("CMD /C")) {
                command("CMD /C " + command);
            }
            else {
                throw new RuntimeException(e);
            }
        }
        // 如果程序运行产生了错误, 报错
        if(err) {
            throw new OSExecuteException("Errors executing " + command);
        }
    }
}


/* Output
*/