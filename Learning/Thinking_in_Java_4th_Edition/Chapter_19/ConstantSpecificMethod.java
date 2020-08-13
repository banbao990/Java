/**
 * @author banbao
 * @comment �޸���ʾ������
 */

import java.util.*;
import java.text.*;

public enum ConstantSpecificMethod {
    DATE_TIME {
        @Override
        public String getInfo() {
            return
                DateFormat.getDateInstance().format(new Date());
        }
    },
    JAVA_HOME {
        @Override
        public String getInfo() {
            return System.getenv("JAVA_HOME");
        }
    },
    VERSION {
        @Override
        public String getInfo() {
            return System.getProperty("java.version");
        }
    },
    NOTHING;
    public String getInfo() { return "default"; }
    public static void main(String[] args) {
        for(ConstantSpecificMethod csm : values()) {
            System.out.println(csm + " : " + csm.getInfo());
        }
    }
    // public void f1(ConstantSpecificMethod.NOTHING instance) {}
    // ����: �Ҳ�������
    // public void f1(ConstantSpecificMethod.NOTHING instance) {}
    //                                      ^
    // ����:   �� NOTHING
    // λ��: �� ConstantSpecificMethod
}
/* Output
DATE_TIME : 2020-8-12
JAVA_HOME : D:\installed-application\Java\JDK
VERSION : 1.8.0_251
NOTHING : default
*/
