/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: postfixCalculator.h
 * Date: 09/17/14
 * Description: PostfixCalculator class definition
 */
#ifndef POSTFIX_CALC_H
#define POSTFIX_CALC_H

#include <string>
#include <iostream>
#include "Stack.h"

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
    Stack s;
};

#endif
