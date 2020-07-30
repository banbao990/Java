/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

interface Processor<T,E extends Exception> {
    void process(List<T> toRun) throws E;
}

class StringFailure extends Exception {
    public StringFailure(int n) {
        super("Left " + n + " to deal with!");
    }
}
class IntegerFailure extends Exception {
    public IntegerFailure(int n) {
        super("Left " + n + " to deal with!");
    }
}

class StringProcessor implements Processor<String, StringFailure> {
    private int left;
    public StringProcessor(int left){
        this.left = left;
    }
    @Override
    public void process(List<String> toRun) throws StringFailure {
        if(left == 0) { throw new StringFailure(toRun.size()); }
        if(toRun.size() <= left) {
            left -= toRun.size();
            for(String str : toRun) {
                // process
                System.out.println("process " + str);
            }
            toRun.clear();
        } else {
            for(int i = 0; i < left; ++i) {
                // process
                System.out.println("process " + toRun.get(0));
                toRun.remove(0);
            }
            left = 0;
            throw new StringFailure(toRun.size());
        }
    }
}
class IntegerProcessor implements Processor<Integer, IntegerFailure> {
    private int left;
    public IntegerProcessor(int left){
        this.left = left;
    }
    @Override
    public void process(List<Integer> toRun) throws IntegerFailure {
        if(left == 0) { throw new IntegerFailure(toRun.size()); }
        if(toRun.size() <= left) {
            left -= toRun.size();
            for(int str : toRun) {
                // process
                System.out.println("process " + str);
            }
            toRun.clear();
        } else {
            for(int i = 0; i < left; ++i) {
                // process
                System.out.println("process " + toRun.get(0));
                toRun.remove(0);
            }
            left = 0;
            throw new IntegerFailure(toRun.size());
        }
    }
}
public class ThrowGenericException {
    public static void main(String...args)
    throws Exception{
        new IntegerProcessor(5).process(
            new ArrayList<Integer>(Arrays.asList(
                1,2,3,4,5
            ))
        );
        new StringProcessor(5).process(
            new ArrayList<String>(Arrays.asList(
                "A","B","C","D","E","F"
            ))
        );
    }
}


/* Output
process 1
process 2
process 3
process 4
process 5
process A
process B
process C
process D
process E
Exception in thread "main" StringFailure: Left 1 to deal with!
    at StringProcessor.process(ThrowGenericException.java:47)
    at ThrowGenericException.main(ThrowGenericException.java:85)
*/