#include <iostream>
using namespace std;

int main() {
    int a = 7;
    int *b;
    int *c = &a;
    int &d = a;
    int &e = *c;
    int **f = &c;
    int g[4];
    int h[] = { 1, 2, 3, 4 };

    cout << "a: " << a << endl;
    cout << "b: " << b << endl;
    cout << "*b: " << *b << endl;
    cout << "c: " << c << endl;
    cout << "*c: " << *c << endl;
    cout << "d: " << d << endl;
    cout << "e: " << e << endl;
    cout << "f: " << f << endl;
    cout << "*f: " << *f << endl;
    cout << "**f: " << **f << endl;
    cout << "g: " << g << endl;
    cout << "g[0]: " << g[0] << endl;
    cout << "h: " << h << endl;
    cout << "h[0]: " << h[0] << endl;
}
