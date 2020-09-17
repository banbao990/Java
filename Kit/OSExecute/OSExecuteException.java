/**
 * @author banbao
 * 这里的异常是为了区别于其他异常
 */

package OSExecute;
public class OSExecuteException extends RuntimeException {
    public OSExecuteException(String why) { super(why); }
}
