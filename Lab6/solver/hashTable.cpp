#include <string>
#include "hashTable.h"
#include "List.h"
#include <vector>
#include "hashFunctions.h"
#include <typeinfo>

using namespace std;

HashTable::HashTable(unsigned int size) {
    space = size;
    map = new List*[getNextPrime(size)];
}

HashTable::~HashTable() {
    makeEmpty();
    delete map;
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

void HashTable::makeEmpty() {
   for (int i=0;i<this->space;i++) {
       if (map[i] != NULL) {
            delete map[i];
        }
    }
}
