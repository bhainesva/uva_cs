/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: Stack.h
 * Date: 09/17/14
 * Description: Stack class definition
 */
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
