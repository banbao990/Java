/**
 * @author banbao
 * @comment �޸���ʾ������
 */

# include <iostream>
using namespace std;
template<class T> class Foo {
    T x;  // Create a field of type T(��)
    T* y; // Pointer to T(ָ��)
public:
    // Initialize the pointer:
    // ֱ��ʹ�ò�������
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