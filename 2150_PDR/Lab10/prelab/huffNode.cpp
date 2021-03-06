/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: HuffNode.cpp
 * Date: 11/24/14
 * Description: HuffNode implementation 
 */
#include <iostream>
#include "huffNode.h"
#include <string>

HuffNode::HuffNode(){
    freq = 0;
    letter = 0;
    left = NULL;
    right = NULL;
}

HuffNode::~HuffNode(){
    if (this->left != NULL) {
        delete this->left;
    }
    if (this->right != NULL) {
        delete this->right;
    }
}

HuffNode::HuffNode(char chr, int frq){
    freq = frq;
    letter = chr;
    left = NULL;
    right = NULL;
}

void HuffNode::print() {
    string ret = "";
    HuffNode * node = this;
    while (node->left != NULL) {
        node = node->left;
    }
}
    
