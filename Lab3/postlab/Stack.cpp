#include "Stack.h"
#include <iostream>
#include "postfixCalculator.h"

using namespace std;

Stack::Stack() {
    List stackTho; 
}

Stack::~Stack() {
   //Do Nothing; 
}

void Stack::pop() {
    ListItr toPop = stackTho.last();
    stackTho.del(toPop);
}

void Stack::push(int x) {
    stackTho.insertAtTail(x);
}

int Stack::top() const {
    ListItr toTop = stackTho.last();
    return toTop.retrieve();
}

bool Stack::empty() const {
    return stackTho.isEmpty();
}   
