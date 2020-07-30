/**
 * @author banbao
 * @comment 修改自示例代码
 */

public class ComparablePet implements Comparable<ComparablePet>{
    @Override
    public int compareTo(ComparablePet cp) {
        // ...
        return 0;
    }
}


//  错误: 无法使用以下不同的参数继承Comparable: <Cat> 和 <ComparablePet>
// class Cat extends ComparablePet implements Comparable<Cat> {
    // @Override
    // public int compareTo(Cat cat) {
        // // ...
        // return 0;
    // }
// }

class Cat extends ComparablePet {
    @Override
    public int compareTo(ComparablePet cp) {
        // if(cp instanceof Cat) {...}
        // ...
        return 0;
    }
}

