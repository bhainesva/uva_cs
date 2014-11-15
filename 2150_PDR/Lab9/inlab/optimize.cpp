#include <iostream>
 
using namespace std;

int constantArithmetic() {
    int x = 0;
    for (int i = 0; i < 100; i++) {
        x += i;
    }
    return x;
}

int redundancy(int x) {
    int y = 2 * x;
    int z = 2 * x;
    cout << y;
    cout << z;
}

int simplification(int x) {
    int y = x + x;
    int z = 2 * x;
    int ret = y - z;
    return ret;
}

int inlining(int x) {
    return x + 1;
}

int main(void) {
    int arg;
    cin >> arg;
    int x = constantArithmetic();
    int y = redundancy(arg);
    int z = simplification(arg);
    int a = inlining(arg);
    cout << a;
    return 0;
}

