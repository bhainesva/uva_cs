/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: testPostfixCalc.cpp
 * Date: 09/17/14
 * Description: PostfixCalculator testing 
 */
#include <cstdlib>
#include <string>
#include "postfixCalculator.h"
#include <iostream>

using namespace std;

int main() {
    PostfixCalculator p;
    while (cin.good()) {
        string s;
        cin >> s;
        if (isdigit(s[s.length()-1])) {
            p.pushNum(atoi(s.c_str()));
        }
        else if(s[0] == '+') {
            p.add();
        }
        else if(s[0] == '*') {
            p.multiply();
        }
        else if(s[0] == '/') {
            p.divide();
        }
        else if(s[0] == '-') {
            p.subtract();
        }
        else if(s[0] == '~') {
            p.negate();
        }
        else{
            //LOL DO NOTHING
        }
    }
    cout << p.getTopValue() << endl;
    return 0;
}
