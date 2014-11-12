//	Modified: 8/30/2006: mc2zk
//  CS216 Lab 1 - Test Harness
//   by Michael Crane
//
//  Integrate this test harness with your List implementation to
//  thoroughly test your design.  I have only used functions that were
//  explicitly defined in the specification, with one exception:
//
//  I have assumed a global print function whose prototype is
//      void printList(List l, bool forward);
//
//  If the 'forward' parameter is true,
//      the list is printed in forward order
//  if the 'forward' parameter is false,
//      the list is printed in reverse order


#include <iostream>
#include <string>
#include <ctype.h>
#include <stdlib.h>
using namespace std;

//Make sure your own files for the List and ListItr
//classes are included here.  These are the names I used.
#include "List.h"
#include "ListItr.h"

int main() {
    int      command;
    string   response;
    List     *list = NULL;
    ListItr  *itr = NULL; 
    if (list != NULL) delete list;
    list = new List;

    // accept elements
    cout << "\t\tEnter elements one by one as integers.\n";
    cout << "\t\tAny non-numeric character, e.g. #, ";
    cout << "will terminate input\n";

    cout << "\tEnter first element: ";
    cin >> response;

    while (isdigit(response[0])) {
        string element = response;
        list->insertAtTail(element);

        cout << "\tEnter next element: ";
        cin >> response;
    }

    cout << endl << "The elements in forward order: " << endl;
    printList(*list, true);
    
    return 0;
}
