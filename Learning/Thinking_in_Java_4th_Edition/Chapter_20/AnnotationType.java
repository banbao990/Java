/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.lang.annotation.*;
@Target(ElementType.METHOD) // 一个或者多个,默认是全部元素
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationType {
    public static final String nullStr = ""; // OK
    public int id1() default -1;
    public String id2() default nullStr;
    // public String id2() default nullStr; // 等价于 ""
    public Class id3() default Null.class;
    public CanUseEnum id4() default CanUseEnum.NULL;
    // public CanUseClass id5(); // 错误: 注释类型元素 {0} 的类型无效
    public CanUseAnnotation id6() default @CanUseAnnotation();
    public int[] id7() default {-1};
    // public Integer id8(); // 错误: 注释类型元素 {0} 的类型无效
}
class Null {}
enum NullEnum {}
enum CanUseEnum { NULL, }
class CanUseClass {}
@interface CanUseAnnotation {}
@interface NullAnnotation {}