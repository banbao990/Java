/**
 * @author banbao
 * @comment �޸���ʾ������
 */

public class ComparablePet implements Comparable<ComparablePet>{
    @Override
    public int compareTo(ComparablePet cp) {
        // ...
        return 0;
    }
}


//  ����: �޷�ʹ�����²�ͬ�Ĳ����̳�Comparable: <Cat> �� <ComparablePet>
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

