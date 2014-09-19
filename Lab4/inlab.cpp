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
    char c = SCHAR_MAX;
    cout << static_cast<unsigned>(c) << endl;
    return 0;
}
