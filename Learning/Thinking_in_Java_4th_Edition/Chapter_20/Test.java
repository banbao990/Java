/**
 * @author banbao
 * @comment 修改自示例代码 
 * "@Test"
 */

import java.lang.annotation.*;
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {}