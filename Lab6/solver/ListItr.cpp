/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: ListItr.cpp 
 * Date: 09/09/14
 * Description: ListItr implementations 
 */
#include <iostream>
#include "ListItr.h"

ListItr::ListItr(){
    current = NULL;
}

ListItr::ListItr(ListNode* theNode){
    current = theNode;
}

bool ListItr::isPastEnd() const {
    return (this->current->next == NULL); 
}

bool ListItr::isPastBeginning() const {
    return (this->current->previous == NULL);
}

void ListItr::moveForward(){
    if (!(this->isPastEnd())){
        this->current = this->current->next;
    } 
}

void ListItr::moveBackward(){
    if (!this->isPastBeginning()){
        current = current->previous;
    }
}

string ListItr::retrieve() const {
    return current->value;
}
