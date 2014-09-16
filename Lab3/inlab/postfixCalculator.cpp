/*
* Name: Ben Haines
* ID: bmh5wx
* Filename: postfixCalculator.cpp
* Date: 09/16/14
* Description: Class implementation for PostfixCalculator
*/

#include <stack>
#include <iostream>
#include "postfixCalculator.h"

using namespace std;

PostfixCalculator::PostfixCalculator() {
    stack<int> s;
}

PostfixCalculator::~PostfixCalculator() {
   //Do Nothing; 
}

bool PostfixCalculator::isEmpty() const {
    return s.empty();
}

void PostfixCalculator::pushNum(int num) {
    s.push(num);
}

void PostfixCalculator::add() {
    int x = s.top();
    s.pop();
    int y = s.top();
    s.pop();
    this->pushNum(x+y);
}

void PostfixCalculator::subtract() {
    int x = s.top();
    s.pop();
    int y = s.top();
    s.pop();
    this->pushNum(x-y);
}   

void PostfixCalculator::multiply() {
    int x = s.top();
    s.pop();
    int y = s.top();
    s.pop();
    this->pushNum(y*x);
}

void PostfixCalculator::divide() {
    int x = s.top();
    s.pop();
    int y = s.top();
    s.pop();
    this->pushNum(y/x);
}

void PostfixCalculator::negate() {
    int x = s.top();
    s.pop();
    this->pushNum(-x);
}

int PostfixCalculator::getTopValue() {
    return s.top();
}
