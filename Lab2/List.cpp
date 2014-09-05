/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: List.cpp
 * Date: 09/09/14
 * Description: Implementation of List methods.
 */

#include <iostream>
#include "List.h"
#include "ListNode.h"
#include "ListItr.h"
using namespace std;

//Default Constructor
List::List() {
    head=new ListNode;
    tail=new ListNode;
    head->next=tail;
    tail->previous=head;
    count=0;
}

//Copy Constructor
List::List(const List& source) {
    head=new ListNode;
    tail=new ListNode;
    head->next=tail;
    tail->previous=head;
    count=0;
    ListItr iter(source.head->next);
    while (!iter.isPastEnd()) {       // deep copy of the list
        insertAtTail(iter.retrieve());
        iter.moveForward();
    }
}

//Destructor
List::~List(){
    cout << "List Destructor: Please implement." << endl;
}

//Equals Operator
List& List::operator=(const List& source){
    if (this == &source)
        return *this;
    else {
        makeEmpty();
        ListItr iter(source.head->next);
        while (!iter.isPastEnd()) {
            insertAtTail(iter.retrieve());
            iter.moveForward();
        }
    }
    return *this;
}

//True if empty, else false
bool isEmpty(){
    return false;
}

//Makes the list empty
void makeEmpty(){
    cout << "Make empty." << endl;
}

//Returns an iterator pointing to the first listNode.
ListItr first(){
    ListItr iterator;
    return iterator;
}

//Returns an interator pointing to the last listNode
ListItr last(){
    ListItr iterator;
    return iterator;
}

//Inserts x after current iterator position
void insertAfter(int x, ListItr position){
    cout << "Insert After" << endl;
}

//Inserts x after current iterator position p
void insertBefore(int x, ListItr position){
    cout << "Insert Before" << endl;
}
//Insert x at tail of list
void insertAtTail(int x){
    cout << "Insert at Tail" << endl;
}

//Removes the first occurrence of x
void remove(int x){
    cout << "Remove" << endl;
}

//Returns an iterator that points to the first occurrence of x, else return a blank iterator
ListItr find(int x){
    ListItr iterator;
    return iterator;
}
