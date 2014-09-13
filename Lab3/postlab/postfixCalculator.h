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
