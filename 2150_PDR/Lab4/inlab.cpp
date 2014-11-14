#include <iostream>
#include <string>
#include <climits>
#include <cfloat>

using namespace std;

void sizeOfTest() {
    cout << "int: " << sizeof(int) << endl;
    cout << "unsigned int: " << sizeof(unsigned int) << endl;
    cout << "float: " << sizeof(float) << endl;
    cout << "double: " << sizeof(double) << endl;
    cout << "char: " << sizeof(char) << endl;
    cout << "bool: " << sizeof(bool) << endl;
    cout << "int*: " << sizeof(int*) << endl;
    cout << "char*: " << sizeof(char*) << endl;
    cout << "double*: " << sizeof(double*) << endl;
}

int main() {
    sizeOfTest();
    float x = FLT_MAX;
    cout << x << endl;
    double y = DBL_MAX;
    cout << y << endl;
    char character = SCHAR_MAX;
    cout << static_cast<unsigned>(character) << endl;

    int i = 0;
    unsigned int ui = 0;
    float f = 0.0;
    double d = 0.0;
    char c = '0';
    bool b = false;
    int* ip = NULL;
    char* cp = NULL;
    double* dp = NULL;
    cout << i << ui << f << d << c << b << ip << cp << dp << endl;
    cout << "break" << endl;

    int i1 = 1;
    unsigned int ui1 = 1;
    float f1 = 1.0;
    double d1 = 1.0;
    char c1 = '1';
    bool b1 = true;
    cout << i1 << ui1 << f1 << d1 << c1 << b1 << endl;
    cout << "break" << endl;
    return 0;
}
