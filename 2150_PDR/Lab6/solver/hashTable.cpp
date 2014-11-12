/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: hashTable.cpp
 * Date: 10/17/14
 * Description: Hash table class implementation
 */
#include <string>
#include "hashTable.h"
#include "List.h"
#include <vector>
#include "hashFunctions.h"
#include <typeinfo>

using namespace std;

//Constructor
HashTable::HashTable(unsigned int size) {
    space = getNextPrime(size);
    map = new List*[getNextPrime(size)];
    for (int i=0; i < space;i++) {
        map[i] = NULL;
    }
}

//Deconstructor
HashTable::~HashTable() {
    makeEmpty();
    delete map;
}

//Hash function
int HashTable::hash(string key) {
    return key.length() % space;
}

//Checks if a given string is in the hash table
bool HashTable::get(string keyVal) {
    int index = hash(keyVal);
    if (map[index] == NULL) {
        return false;
    }

    return map[index]->find(keyVal);
}

//Insers a string into the hash table
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

//Deletes everything from the hash table
void HashTable::makeEmpty() {
   for (int i=0;i<this->space;i++) {
       if (map[i] != NULL) {
            delete map[i];
        }
    }
}
