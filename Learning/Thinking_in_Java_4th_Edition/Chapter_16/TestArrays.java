/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.Arrays;
import java.util.Comparator;

public class TestArrays {
    public static void seg(String str) {
        System.out.println("~~~~~" + str + "~~~~~");
    }
    public static void main(String...args) {
        int[] a = new int[5];
        
        // fill
        seg("fill()");
        Arrays.fill(a, 10086);
        System.out.println(Arrays.toString(a));
        Arrays.fill(a, 1, 2, 88);
        System.out.println(Arrays.toString(a));
        
        // System.arraycopy()
        seg("System.arraycopy()");
        int[] b = new int[10];
        for(int i = 0;i < b.length; ++i) { b[i] = i + 1; }
        System.out.println(Arrays.toString(b));
        System.arraycopy(b, 2, b, 0, 4);
        System.out.println(Arrays.toString(b));
        for(int i = 0;i < b.length; ++i) { b[i] = i + 1; }
        System.arraycopy(b, 0, b, 2, 4);
        System.out.println(Arrays.toString(b));
        
        // Arrays.sort()
        Integer[] c = new Integer[10];
        seg("Arrays.sort()");
        for(int i = 0;i < c.length; ++i) { c[i] = i + 1; };
        System.out.println(Arrays.toString(c));
        // 默认升序(Integer实现)
        Arrays.sort(c); 
        System.out.println(Arrays.toString(c));
        // 匿名类
        Arrays.sort(c, new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        System.out.println(Arrays.toString(c));
        // lambda 表达式(偶数>奇数,其余按照顺序)
        Arrays.sort(c, (o1,o2)-> {
            if(((o1&1)^(o2&1)) != 0) {
                return (o1&1)==0 ? 1 : -1;
            } else {
                return o1 - o2;
            }
        });
        System.out.println(Arrays.toString(c));

        // binarySearch();
        seg("binarySearch()");
        for(int i = 0;i < c.length; ++i) { c[i] = i + 1; };
        c[5] = 10;
        Arrays.sort(c);
        System.out.println(Arrays.toString(c));
        System.out.println(Arrays.binarySearch(c, 5));
        System.out.println(Arrays.binarySearch(c, 6));
    }
}


/* Output
~~~~~fill()~~~~~
[10086, 10086, 10086, 10086, 10086]
[10086, 88, 10086, 10086, 10086]
~~~~~System.arraycopy()~~~~~
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
[3, 4, 5, 6, 5, 6, 7, 8, 9, 10]
[1, 2, 1, 2, 3, 4, 7, 8, 9, 10]
~~~~~Arrays.sort()~~~~~
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
[10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
[1, 3, 5, 7, 9, 2, 4, 6, 8, 10]
~~~~~binarySearch()~~~~~
[1, 2, 3, 4, 5, 7, 8, 9, 10, 10]
4
-6
*/