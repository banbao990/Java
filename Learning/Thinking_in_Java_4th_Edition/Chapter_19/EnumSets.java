/**
 * @author banbao
 * @comment �޸���ʾ������
 */

import java.util.*;
import animals.*;
import static animals.Animal.*;
import static animals.Animal2.*;

// Animal : HORSE, COW, BIRD, DEER, GOOSE, 
// Animal2 : DUCK, 
public class EnumSets {
    private static EnumSet<Animal> animals;
    public static void print() {
        System.out.println(animals);
    }
    public static void main(String[] args) {
        animals = EnumSet.noneOf(Animal.class); // Empty set
        print();
        animals.add(HORSE);
        print();
        animals.addAll(EnumSet.of(COW, BIRD));
        print();
        animals = EnumSet.allOf(Animal.class); // FULL
        animals.removeAll(EnumSet.of(DEER, GOOSE));
        print();
        animals.removeAll(EnumSet.range(HORSE, COW)); // ���Ҷ�ȡ��
        print();
        animals = EnumSet.complementOf(animals); // ȡ����
        print();
        // animals.add(DUCK); // CE
    }
}
/* Output:
[]
[HORSE]
[HORSE, COW, BIRD]
[HORSE, COW, BIRD]
[BIRD]
[HORSE, COW, DEER, GOOSE]
*/
