/**
 * @author banbao
 * @comment 修改自示例代码
 */

enum EnumMethod {
    R("red"), G("green"), B("blue");
    public final String description;
    // Constructor must be package or private access:
    private EnumMethod(String description) {
        this.description = description;
    }
    public static void main(String...args) {
        for(EnumMethod em : EnumMethod.values()) {
            System.out.println(
                em.toString() + ":"
                + em.description + "\n"
                + "--------------"
            );
        }
    }
}


/* Output
R:red
--------------
G:green
--------------
B:blue
--------------
*/
