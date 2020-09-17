/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class TestCollections {
    public static void main(String...args) {
        List<TestCollections> list = 
            new ArrayList<TestCollections>(
                Collections.nCopies(2, new TestCollections())
            );
        System.out.println(list);
        Collections.fill(list, new TestCollections());
        System.out.println(list);
    }
}


/* Output
[TestCollections@15db9742, TestCollections@15db9742]
[TestCollections@6d06d69c, TestCollections@6d06d69c]
*/
