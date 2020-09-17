/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.*;
import java.io.*;

class SNA implements Serializable {
    public SNB snb;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n    ");
        sb.append(super.toString());
        sb.append("\n    ");
        sb.append(snb!=null ? snb.toString() : "null");
        sb.append("\n]");
        return sb.toString();
    }
}
class SNB implements Serializable {
    @Override
    public String toString() {
        return "[ " + super.toString() + " ]";
    }
}
class SNC implements Serializable {
    public SNB snb;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n    ");
        sb.append(super.toString());
        sb.append("\n    ");
        sb.append(snb!=null ? snb.toString() : "null");
        sb.append("\n]");
        return sb.toString();
    }
}

public class SerializableNet {
    public static void main(String...args)
        throws Exception {
        final String seg = "~~~~~~~~~~~~~~~~\n";
        SNA a = new SNA();
        SNB b = new SNB();
        SNC c = new SNC();
        System.out.print(seg+a+"\n"+c+"\n"+seg); // P1
        a.snb = b;
        c.snb = b;
        System.out.print(seg+a+"\n"+c+"\n"+seg); // P2
        TransientTest.store(new Object[]{a,c}, "AC.out");
        // bad restore
        HolderObject[] temp = new HolderObject[]{
                new HolderObject(a), new HolderObject(c)};
        TransientTest.restore(temp, "AC.out");
        a = (SNA)temp[0].o;
        c = (SNC)temp[1].o;
        System.out.print(seg+a+"\n"+c+"\n"+seg); // P3
        TransientTest.store(new Object[]{a}, "A.out");
        TransientTest.store(new Object[]{c}, "C.out");
        // bad restore
        temp[0].o = a; temp[1].o = c;
        TransientTest.restore(temp[0], "A.out");
        TransientTest.restore(temp[1], "C.out");
        a = (SNA)temp[0].o;
        c = (SNC)temp[1].o;
        System.out.print(seg+a+"\n"+c+"\n"+seg); // P4
        
        // save A,B
        a.snb = b;
        System.out.print(seg+a+"\n"+b+"\n"+seg); // P5
        TransientTest.store(new Object[]{a,b}, "AB.out");
        // bad restore
        temp[0].o = a; temp[1].o = b;
        TransientTest.restore(temp, "AB.out");
        a = (SNA)temp[0].o;
        b = (SNB)temp[1].o;
        System.out.print(seg+a+"\n"+b+"\n"+seg); // P6
    }
}


/* Output
~~~~~~~~~~~~~~~~
[
    SNA@15db9742
    null
]
[
    SNC@6d06d69c
    null
]
~~~~~~~~~~~~~~~~
~~~~~~~~~~~~~~~~
[
    SNA@15db9742
    [ SNB@7852e922 ]
]
[
    SNC@6d06d69c
    [ SNB@7852e922 ]
]
~~~~~~~~~~~~~~~~
Store...
[
    SNA@15db9742
    [ SNB@7852e922 ]
]
[
    SNC@6d06d69c
    [ SNB@7852e922 ]
]
Restore...
[
    SNA@7ba4f24f
    [ SNB@3b9a45b3 ]
]
[
    SNC@7699a589
    [ SNB@3b9a45b3 ]
]
~~~~~~~~~~~~~~~~
[
    SNA@7ba4f24f
    [ SNB@3b9a45b3 ]
]
[
    SNC@7699a589
    [ SNB@3b9a45b3 ]
]
~~~~~~~~~~~~~~~~
Store...
[
    SNA@7ba4f24f
    [ SNB@3b9a45b3 ]
]
Store...
[
    SNC@7699a589
    [ SNB@3b9a45b3 ]
]
Restore...
[
    SNA@58372a00
    [ SNB@4dd8dc3 ]
]
Restore...
[
    SNC@6d03e736
    [ SNB@568db2f2 ]
]
~~~~~~~~~~~~~~~~
[
    SNA@58372a00
    [ SNB@4dd8dc3 ]
]
[
    SNC@6d03e736
    [ SNB@568db2f2 ]
]
~~~~~~~~~~~~~~~~
~~~~~~~~~~~~~~~~
[
    SNA@58372a00
    [ SNB@7852e922 ]
]
[ SNB@7852e922 ]
~~~~~~~~~~~~~~~~
Store...
[
    SNA@58372a00
    [ SNB@7852e922 ]
]
[ SNB@7852e922 ]
Restore...
[
    SNA@378bf509
    [ SNB@5fd0d5ae ]
]
[ SNB@5fd0d5ae ]
~~~~~~~~~~~~~~~~
[
    SNA@378bf509
    [ SNB@5fd0d5ae ]
]
[ SNB@5fd0d5ae ]
~~~~~~~~~~~~~~~~
*/
