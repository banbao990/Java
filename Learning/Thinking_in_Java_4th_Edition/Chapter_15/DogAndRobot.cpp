/**
 * @author banbao
 * @comment �޸���ʾ������(Ǳ�����ͻ���)
 */
#include <iostream>
using namespace std;

class Dog {
public:
    void speak() {
        cout << "Dog:speak()" << endl;
    }
    void sit() {}
};

class Robot{
public: 
    void speak() {
        cout << "Robot:speak()" << endl;
    }
    void work() {}
};

template<class T> void perform(T anything) {
    anything.speak();
}

int main() {
    Dog d; 
    Robot r;
    perform(d);
    perform(r);
    return 0;
}
/* Output
Dog:speak()
Robot:speak()
*/