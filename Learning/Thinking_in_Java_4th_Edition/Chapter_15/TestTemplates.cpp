/**
 * @author banbao
 * @comment �޸���ʾ������
 */

# include <iostream>
using namespace std;

template<class T> class TestTemplates{
    T obj;
public:
    TestTemplates(T _obj) : obj(_obj){}
    void test() { obj.f(); }
};

class HasF {
public: 
    void f() { cout << "HasF:f()" << endl;}
};

class NotHasF {};

int main() {
    // HasF
    HasF hf;
    TestTemplates<HasF> hasF(hf);
    hasF.test();
    // NotHasF
    // error C2039: ��f��: ���ǡ�NotHasF���ĳ�Ա
    // NotHasF nhf;
    // TestTemplates<NotHasF> notHasF(nhf);
    // notHasF.test();
    return 0;
}
/* Output
HasF:f()
*/