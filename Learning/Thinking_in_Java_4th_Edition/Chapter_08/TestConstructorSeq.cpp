/**
 * @author banbao
 */

#include <stdio.h>

class Base{
    public:
    Base(){
        sayHi();
    }
    
    void sayHi(){
        printf("Base:Hi\n");
    }
};

class Derived: Base{
    public:
    int kind = 1;
    Derived(){}
    void sayHi(){
        printf("Derived:Hi, my kind is %d\n", kind);
    }
};

int main() {
    new Derived();
    return 0;
}
/* output:
 * Base:Hi
*/