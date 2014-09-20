#include <iostream>

using namespace std;

int main() {
    bool b = false;
    char c = 'a';
    int i = 1;
    double d = 2;
    int * ip = new int(2);

    cout << i << endl;
    cout << "start out" << endl;
    cout << "bool: " << b << "char: " << c << "int: " << i << " double: " << d << "intpointer: " << ip << endl;
    cout << "end" << endl;

    /*
    * ---------Primitive Arrays in C++ --------- 
    */
    int IntArray[10];
    char CharArray[10];
    int IntArray2D[6][5];
    char CharArray2D[6][5];

    for (int i = 0; i < 10; i++){
        IntArray[i] = i;
        CharArray[i] = 80 + i;
        if (i < 6) {
            for (int j = 0; j < 5; j++) {
                IntArray2D[i][j] = j*i+j;
                CharArray2D[i][j] = 80 + i + j;
            }
        }
    }

    for (int i=0; i < 10; i++) {
        cout << IntArray[i] << " ";
    }
    cout << endl;
    for (int i=0; i < 10; i++) {
        cout << CharArray[i];
    }
    cout << endl;
    cout << "2D Ints: " << endl;
    for (int m=0; m < 6; m++) {
        for (int j=0; j<5; j++) {
            cout << IntArray2D[m][j];
        }
        cout << endl;
    }
    
    cout << endl;
    cout << "2D Chars" << endl;
    for (int m=0; m < 6; m++) {
        for (int j=0; j<5; j++) {
            cout << CharArray2D[m][j];
        }
        cout << endl;
    }

    cout << "end" << endl;
    return 0;
}
