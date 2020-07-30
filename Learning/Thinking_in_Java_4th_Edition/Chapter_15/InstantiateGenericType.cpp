/**
 * @author banbao
 * @comment 修改自示例代码
 */

# include <iostream>
using namespace std;
template<class T> class Foo {
    T x;  // Create a field of type T(域)
    T* y; // Pointer to T(指针)
public:
    // Initialize the pointer:
    // 直接使用参数类型
    Foo() { y = new T(); }
};

class Bar {};

int main() {
    Foo<Bar> fb;
    Foo<int> fi; // ... and it works with primitives
    cout << *(new int(10)) << endl;
}
/* Output
10
*/