/*
 * Filename: ListNode.h
 * Description: ListNode class definition
 */

#ifndef HUFFNODE_H
#define HUFFNODE_H

#include <iostream>

// next line needed because NULL is part of std namespace
using namespace std;

class HuffNode {
public:
    HuffNode();                 // Constructor
    HuffNode(char chr, int frq);                 // Constructor
    void print();
    int freq;
    char letter;
    HuffNode *left, *right;
};

#endif

