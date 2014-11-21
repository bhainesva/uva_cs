// Code written by Aaron Bloomfield, 2014
// Released under a CC BY-SA license
// This code is part of the https://github.com/aaronbloomfield/pdr repository

#ifndef heap_H
#define heap_H

#include <vector>
#include "huffNode.h"

using namespace std;

class heap {
public:
    heap();
    ~heap();

    void insert(HuffNode * x);
    HuffNode* findMin();
    HuffNode* deleteMin();
    unsigned int size();
    void makeEmpty();
    bool isEmpty();
    void print();

private:
    vector<HuffNode*> myHeap;
    unsigned int heap_size;

    void percolateUp(int hole);
    void percolateDown(int hole);
};

#endif

