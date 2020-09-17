// 示例代码

import java.util.logging.*;
import java.io.*;

class LoggingException extends Exception {
    // 创建一个 String 参数相关联的 logger
    private static Logger logger =
        Logger.getLogger("LoggingException");
    // 构造函数
    public LoggingException() {
        // 为了将栈轨迹输出到字符串中
        StringWriter trace = new StringWriter();
        printStackTrace(new PrintWriter(trace));
        // severe 严重级别的异常
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

七月 13, 2020 11:22:36 上午 LoggingException <init>
严重: LoggingException
    at LoggingExceptions.main(LoggingExceptions.java:19)

Caught LoggingException
七月 13, 2020 11:22:36 上午 LoggingException <init>
严重: LoggingException
    at LoggingExceptions.main(LoggingExceptions.java:24)

Caught LoggingException

*/
