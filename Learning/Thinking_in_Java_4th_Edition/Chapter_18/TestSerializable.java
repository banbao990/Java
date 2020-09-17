/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;
import java.io.*;

class TestSerializable1 implements Serializable {
    public static int cnt = 0;
    private int ID;
    public TestSerializable1() {
        this.ID = ++ cnt;
        System.out.println(
            "default constructor for TestSerializable1");
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[~TestSerializable1~]\n");
        sb.append("    ID:");
        sb.append(this.ID);
        sb.append("\n    cnt:");
        sb.append(cnt);
        return sb.toString();
    }
}

class TestSerializable2 implements Serializable {
    private TestSerializable1 t1;
    private int specialNum;
    public TestSerializable2() {
        this.specialNum =  new Random().nextInt(500);
        System.out.println(
            "default constructor for TestSerializable2");
        t1 = new TestSerializable1();
    }
    
    public TestSerializable2(int specialNum) {
        this.specialNum = specialNum;
        System.out.println(
            "arg(int) constructor for TestSerializable2");
        t1 = new TestSerializable1();
    }
    
    @Override
    public String toString() {
        return "specialNum:" + this.specialNum + "\n" + t1;
    }
}

public class TestSerializable implements Serializable {
    private TestSerializable2 t2;
    private static void error() {
        System.err.println("args:1->store,2->restore");
        System.exit(1);
    }
    
    public static void main(String...args) throws Exception {
        // test static cnt
        System.out.println(
            "TestSerializable1.cnt(static):" 
            + TestSerializable1.cnt);
        
        TestSerializable ts = new TestSerializable();
        if(args.length != 1) { error(); }
        if("1".equals(args[0])) { ts.store(); } 
        else if("2".equals(args[0])) { ts.restore(); }
        else{ error(); }
        
        // test static cnt
        System.out.println(
            "TestSerializable1.cnt(static):" 
            + TestSerializable1.cnt);
    }
    
    private void store() throws Exception {
        System.err.println("Store...");
        t2 = new TestSerializable2();
        System.out.println(t2);
        // serializable
        ObjectOutputStream out = 
            new ObjectOutputStream(
                new FileOutputStream("TestSerializable.out"));
        out.writeObject("Store an Object!");
        out.writeObject(t2);
        out.close(); // auto flush
    }
    
    private void restore() throws Exception {
        System.err.println("Restore...");
        ObjectInputStream in = 
            new ObjectInputStream(
                new FileInputStream("TestSerializable.out"));
        String s = (String)in.readObject();
        t2 = (TestSerializable2)in.readObject();
        System.out.println(s);
        System.out.println(t2);
    }
}


/* Output

/// java TestSerializable 1 ///
TestSerializable1.cnt(static):0
Store...
default constructor for TestSerializable2
default constructor for TestSerializable1
specialNum:144
[~TestSerializable1~]
    ID:1
    cnt:1
TestSerializable1.cnt(static):1

/// java TestSerializable 1 ///
TestSerializable1.cnt(static):0
Restore...
Store an Object!
specialNum:144
[~TestSerializable1~]
    ID:1
    cnt:0
TestSerializable1.cnt(static):0
*/
