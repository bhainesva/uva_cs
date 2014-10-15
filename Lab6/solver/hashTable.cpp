#include <string>
#include "hashTable.h"
#include "List.h"
#include <vector>
#include "hashFunctions.h"
#include <typeinfo>

using namespace std;

HashTable::HashTable(int size) {
    size = getNextPrime(size);
    map = new List*[getNextPrime(size)];
}

//HashTable::~HashTable() {
//    cout << "THis is being called" << endl;
//    for (int i=0; i<getSize(); i++) {
//        if (map[i] != NULL) {
//            delete map[i];
//        }
//    }
//    delete map;
//}

int HashTable::getSize() {
    return size;
}

bool HashTable::get(string keyVal) {
    int index = hash(keyVal);
    if (map[index] == NULL) {
        return false;
    }

    return map[index]->find(keyVal);
}

void HashTable::insert(string value) {
    int index = hash(value);
    if (map[index] == NULL) {
        List* lis = new List();
        lis->insertAtTail(value);
        map[index] = lis;
    }
    else {
        map[index]->insertAtTail(value);
    }
}

