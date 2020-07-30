/**
 * @author banbao
 */

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

interface Generator<T> { T next(); }

public class OceanNet {
    // eat
    public static void eat(BigFish bf, SmallFish sf) {
        System.out.println(bf + " eats " + sf);
    }
    
    public static <T> Collection<T> fill(
        Collection<T> co, Generator<T> gen ,int num) {
        for(int i = 0;i < num; ++i) {
            co.add(gen.next());
        }
        return co;
    }
    
    // main
    public static void main(String...args) {
        Random rd = new Random(47);
        List<SmallFish> smallFish = new ArrayList<>();
        List<BigFish> bigFish = new ArrayList<>();
        fill(smallFish, SmallFish.generator, 15);
        fill(bigFish, BigFish.generator, 5);
        for(SmallFish sf : smallFish) {
            eat(bigFish.get(rd.nextInt(bigFish.size())), sf);
        }
    }
}

// Fish
class Fish {
    protected final long id;
    protected Fish(long id) {
        this.id = id;
    }
}

// BigFish
class BigFish extends Fish {
    private static long count = 0;
    private BigFish(){
        super(++ BigFish.count);
    }
    // 匿名类泛型
    // 定义为 static, 因为只需要一个就足够了
    public static Generator<BigFish> generator = 
        new Generator<BigFish>() {
            public BigFish next() {
                return new BigFish();
            }
        };
    @Override
    public String toString(){ return "BigFish " + id; }
}

// SmallFish
class SmallFish extends Fish {
    private static long count = 0;
    SmallFish(){
        super(++ SmallFish.count);
    }
    public static Generator<SmallFish> generator = 
        // 这里必须要声明<SmallFish>,否则报错
        new Generator<SmallFish>() { 
            public SmallFish next() {
                return new SmallFish();
            }
        };
    @Override
    public String toString(){ return "SmallFish " + id; }
}
/* Output
BigFish 4 eats SmallFish 1
BigFish 1 eats SmallFish 2
BigFish 4 eats SmallFish 3
BigFish 2 eats SmallFish 4
BigFish 2 eats SmallFish 5
BigFish 5 eats SmallFish 6
BigFish 4 eats SmallFish 7
BigFish 1 eats SmallFish 8
BigFish 3 eats SmallFish 9
BigFish 3 eats SmallFish 10
BigFish 4 eats SmallFish 11
BigFish 4 eats SmallFish 12
BigFish 2 eats SmallFish 13
BigFish 5 eats SmallFish 14
BigFish 5 eats SmallFish 15
*/