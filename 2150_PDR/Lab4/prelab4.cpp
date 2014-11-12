/*
* Name: Ben Haines
* ID: bmh5wx
* Filename: prelab4.cpp
* Date: 09/19/14
*/
#include <iostream>
#include <string>
#include <climits>

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

void outputBinary(unsigned int x) {
    string out = "";
    while (x != 0){
            if(x%2 != 0){
                out = "1" + out;
            }
            else{
                out = "0" + out;
            }
            x = (x)/2;
    }
    while (out.length() < 32) {
        out = "0" + out;
    }
    for (int i = 0; i < 32; i+=4) {
        cout << out.substr(i, 4);
        cout << " ";
    }
    cout << endl;
}

void overflow() {
    unsigned int x;
    x = UINT_MAX;
    cout << x << endl;
    x++;
    cout << x << endl;
    cout << "The max unsigned int is 32 bits of 1. When you try to add 1 to it each position turns to a zero and carries a 1 to the next bit. When it reaches the most significant bit it turns it into a 0 and then carries a one. The place that it dumps the one isn't in memory that's allocated to the int. So the int can only see the long string of 0s." << endl;
}

int main() {
    unsigned int x;
    cout << "Enter an number: ";
    cin >> x;
    sizeOfTest();
    outputBinary(x);
    overflow();
    
    return 0;
}
