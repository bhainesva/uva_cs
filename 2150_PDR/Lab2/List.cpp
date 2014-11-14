/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: List.cpp
 * Date: 09/09/14
 * Description: Implementation of List methods.
 */
#include <iostream>
#include "List.h"
//Constructor
List::List() {
    head = new ListNode;
    tail = new ListNode;
    head->next=tail;
    head->previous=NULL;
    tail->previous=head;
    tail->next=NULL;
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
    while (!iter.isPastEnd()) {       
        insertAtTail(iter.retrieve());
        iter.moveForward();
    }
}

//Destructor
List::~List(){
    this->makeEmpty();
    delete this->head;
    delete this->tail;
}

//Equals Operator
List& List::operator=(const List& source) { 
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

//True if empty, else false.
bool List::isEmpty() const {
    return !this->size();
}

//Removes all items except blank head and tail
void List::makeEmpty(){
    ListItr points = this->first(); 
    while(!points.isPastEnd()){
        points.moveForward();
        delete points.current->previous;
    }
    this->head->next = this->tail;
    this->tail->previous = this->head;
    this->count = 0;
}

//Returns iterator pointing to first list node
ListItr List::first(){
    ListItr * firstNodeItr = new ListItr(this->head->next);
    return *firstNodeItr;
}

//Returns iterator pointing to last list node
ListItr List::last(){
    ListItr * lastItr = new ListItr(this->tail->previous);
    return *lastItr;
}

void List::insertAfter(int x, ListItr position){
    this->count++;
    ListNode * newNode = new ListNode();
    newNode->value = x;
    newNode->next = position.current->next;
    newNode->previous = position.current;
    position.current->next = newNode;
    newNode->next->previous = newNode;
}

void List::insertBefore(int x, ListItr position){
    this->count++;
    ListNode * newNode = new ListNode();
    newNode->value = x;
    newNode->next = position.current;
    newNode->previous = position.current->previous;
    newNode->previous->next = newNode;
    position.current->previous = newNode;
}

void List::insertAtTail(int x){
    ListNode * newNode = new ListNode();
    newNode->value = x;
    newNode->next = this->tail;
    newNode->previous = this->tail->previous;
    newNode->previous->next = newNode;
    tail->previous = newNode;
    this->count++;
}

void List::remove(int x){
    this->count--;
    ListItr points = find(x);
    if(points.current != NULL){
        points.current->next->previous = points.current->previous;
        points.current->previous->next = points.current->next;
        delete points.current;
    }
}

ListItr List::find(int x){
    ListItr * search = new ListItr(this->first());
    while(search->current->value != x){
        search->moveForward();
        if(search->isPastEnd()){
            ListItr emptyItr;
            emptyItr.current = this->tail;
            return emptyItr;
        }
    }
    return *search;
}

int List::size() const {
    return this->count;
}

//Global printList function
void printList(List& source, bool direction){
    if(direction){
        ListItr points = source.first(); 
        while(!points.isPastEnd()){
            cout << points.retrieve() << " ";
            points.moveForward();
        }
    }
    else {
        ListItr points = source.last();
        while(!points.isPastBeginning()){
            cout << points.retrieve() << " ";
            points.moveBackward();
        }
    }
}

