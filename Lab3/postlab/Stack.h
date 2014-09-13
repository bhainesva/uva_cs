#ifndef STACK
#define STACK

#include <string>
#include <iostream>
#include "List.h"

class Stack {
public:
    Stack();				//Constructor
    ~Stack();			//Destructor
    void push(int x);
    void pop();
    int top() const;
    bool empty() const;

private:
   List stackTho; 
};

#endif
