/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: ListItr.cpp 
 * Date: 09/09/14
 * Description: ListItr implementations 
 */

#include "ListNode.h"
#include "List.h"
#include "ListItr.h"

//Constructor
ListItr::ListItr(){
    current = new ListNode();
}


// One parameter constructor
ListItr::ListItr(ListNode* theNode){};

//Returns true if past end position in list, else false
bool ListItr::isPastEnd() const{
    return false;
}


bool ListItr::isPastBeginning() const {//Returns true if past first position in list, else false
    return false;
}


void ListItr::moveForward(){	//Advances current to next position in list (unless already past end of list)
    cout << "Moving forward" << endl;
}

//Moves current back to previous position in list (unless already past beginning of list)
void ListItr::moveBackward(){
    cout << "Moving backwards" << endl;
}

//Returns item in current position
int ListItr::retrieve() const{
    return 1;
}
