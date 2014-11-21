/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: ListNode.cpp
 * Date: 09/09/14
 * Description: ListNode implementation 
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

HuffNode::HuffNode(char chr, int frq){
    freq = frq;
    letter = chr;
    left = NULL;
    right = NULL;
}

