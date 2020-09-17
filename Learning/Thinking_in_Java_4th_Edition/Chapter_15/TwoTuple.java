/**
 * @author banbao
 * 修改自示例代码
 */

public class TwoTuple<T1, T2> {
    public final T1 first; // final 不允许修改, 没必要设置为 private
    public final T2 second;
    public TwoTuple(T1 first, T2 second) {
        this.first = first; 
        this.second = second; 
    }
    @Override
    public String toString() {
        return "first:" + this.first + " ,second:" + this.second;
    }
    public static void main(String...args) {
        TwoTuple<String, Integer> si = new TwoTuple<>("test string", 10086);
        System.out.println(si);
        System.out.format("first:%s, second:%d\n", si.first, si.second);
    }
}
/* Output
first:test string ,second:10086
first:test string, second:10086
*/
