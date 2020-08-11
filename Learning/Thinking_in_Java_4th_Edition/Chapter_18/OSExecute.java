/**
 * @author banbao
 * @comment �޸���ʾ������
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
            // ��һ������
            Process process =
                new ProcessBuilder(command.split(" ")).start();
            // ��ȡ����
            BufferedReader results = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            String s;
            while((s = results.readLine())!= null)
                System.out.println(s);
            // ��ȡ������Ϣ
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
        // ����������в����˴���, ����
        if(err) {
            throw new OSExecuteException("Errors executing " + command);
        }
    }
}


/* Output
*/