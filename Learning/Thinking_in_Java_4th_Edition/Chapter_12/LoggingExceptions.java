// ʾ������

import java.util.logging.*;
import java.io.*;

class LoggingException extends Exception {
    // ����һ�� String ����������� logger
    private static Logger logger =
        Logger.getLogger("LoggingException");
    // ���캯��
    public LoggingException() {
        // Ϊ�˽�ջ�켣������ַ�����
        StringWriter trace = new StringWriter();
        printStackTrace(new PrintWriter(trace));
        // severe ���ؼ�����쳣
        logger.severe(trace.toString());
    }
}

public class LoggingExceptions {
    public static void main(String[] args) {
        try {
            throw new LoggingException();
        } catch(LoggingException e) {
            System.err.println("Caught " + e);
        }
        try {
            throw new LoggingException();
        } catch(LoggingException e) {
            System.err.println("Caught " + e);
        }
    }
}
/*

���� 13, 2020 11:22:36 ���� LoggingException <init>
����: LoggingException
    at LoggingExceptions.main(LoggingExceptions.java:19)

Caught LoggingException
���� 13, 2020 11:22:36 ���� LoggingException <init>
����: LoggingException
    at LoggingExceptions.main(LoggingExceptions.java:24)

Caught LoggingException

*/
