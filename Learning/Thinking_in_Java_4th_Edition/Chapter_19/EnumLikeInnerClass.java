/**
 * @author banbao
 * @comment 修改自示例代码
 */
import java.util.EnumSet;
public class EnumLikeInnerClass {
    public enum Func {
        FLY { 
            public void func() {
                System.out.println("I can fly!");
            }
        },
        RUN { 
            public void func() {
                System.out.println("I can run!");
            }
        },
        SWIM { 
            public void func() {
                System.out.println("I can swim!");
            }
        }
        ;
        abstract void func();
    }
    public static void main(String...args) {
        EnumLikeInnerClass elic = new EnumLikeInnerClass();
        EnumSet<Func> es = EnumSet.of(Func.FLY);
        es.add(Func.SWIM);
        es.add(Func.SWIM); // ignore
        es.add(Func.RUN); // 添加顺序无关
        System.out.println(es);
    }
}


/* Output
[FLY, RUN, SWIM]
*/