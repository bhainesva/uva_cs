/*
* Name: Ben Haines
* ID: bmh5wx
* Filename: postfixCalculator.h
* Date: 09/16/14
* Description: Class declaration for PostfixCalculator
*/

#ifndef POSTFIX_CALC_H
#define POSTFIX_CALC_H

#include <string>
#include <stack>
#include <iostream>

using namespace std;

class PostfixCalculator {
public:
    PostfixCalculator();				//Constructor
    ~PostfixCalculator();			//Destructor
    bool isEmpty() const;
    void pushNum(int num);
    void add();
    void subtract();
    void multiply();
    void divide();
    void negate();
    int getTopValue();

private:
    stack<int> s;
};

#endif
