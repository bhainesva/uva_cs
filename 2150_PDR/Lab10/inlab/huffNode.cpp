/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: HuffNode.cpp
 * Date: 11/26/14
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

HuffNode::HuffNode(char chr, int frq){
    freq = frq;
    letter = chr;
    left = NULL;
    right = NULL;
}

