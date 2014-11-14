#include <iostream>
#include "List.h"

using namespace std;

int main(){
    List * list = new List();
    list->insertAtTail("23");
    list->insertAtTail("23");
    list->insertAtTail("23");
    list->insertAtTail("23");
    list->insertAtTail("23");
    list->insertAtTail("23");
    list->insertAtTail("23");
    printList(*list, true);

    delete list;
    return 0;
}
