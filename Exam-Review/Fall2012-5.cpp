#include <iostream>
using namespace std;

int main() {
    int *p = new int();
    cout << p << endl;
    delete p;
    *p = 4;
    cout << p << endl;

    return 0;
}
