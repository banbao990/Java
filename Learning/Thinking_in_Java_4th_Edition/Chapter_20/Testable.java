// 示例代码
public class Testable {
    public void execute() {
        System.out.println("Executing..");
    }
    @Test 
    void testExecute() { execute(); }
    public static void main(String...args) {
        new Testable().testExecute();
    }
}
