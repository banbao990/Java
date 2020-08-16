/**
 * @author banbao
 * @comment �޸���ʾ������
 */

import java.lang.annotation.*;
@Target(ElementType.METHOD) // һ�����߶��,Ĭ����ȫ��Ԫ��
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationType {
    public static final String nullStr = ""; // OK
    public int id1() default -1;
    public String id2() default nullStr;
    // public String id2() default nullStr; // �ȼ��� ""
    public Class id3() default Null.class;
    public CanUseEnum id4() default CanUseEnum.NULL;
    // public CanUseClass id5(); // ����: ע������Ԫ�� {0} ��������Ч
    public CanUseAnnotation id6() default @CanUseAnnotation();
    public int[] id7() default {-1};
    // public Integer id8(); // ����: ע������Ԫ�� {0} ��������Ч
}
class Null {}
enum NullEnum {}
enum CanUseEnum { NULL, }
class CanUseClass {}
@interface CanUseAnnotation {}
@interface NullAnnotation {}