/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: HuffNode.h
 * Date: 11/24/14
 * Description: HuffNode class definition
 */

#ifndef HUFFNODE_H
#define HUFFNODE_H

#include <iostream>

// next line needed because NULL is part of std namespace
using namespace std;

class HuffNode {
public:
    HuffNode();                 // Constructor
    ~HuffNode();
    HuffNode(char chr, int frq);                 // Constructor
    void print();
    int freq;
    char letter;
    HuffNode *left, *right;
};

#endif

