/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: hashTable.cpp
 * Date: 10/17/14
 * Description: Hash table class implementation
 */
#include <string>
#include "hashTable.h"
#include <set>
#include <vector>
#include "hashFunctions.h"
#include <typeinfo>

using namespace std;

//Constructor
HashTable::HashTable(unsigned int size) {
    space = getNextPrime(size);
    map = new set<string>*[getNextPrime(size)];
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
//djb2 from http://www.cse.yorku.ca/~oz/hash.html
int HashTable::hash(string key) {
    unsigned char *str = (unsigned char*)key.c_str();
    unsigned long hash = 5381;
    int c;

    while ((c = *str++))
        hash = ((hash << 5) + hash) + c; /* hash * 33 + c */

    return hash % space;
}

//Checks if a given string is in the hash table
bool HashTable::get(string keyVal) {
    int index = hash(keyVal);
    if (map[index] == NULL) {
        return false;
    }

    return map[index]->find(keyVal)!= map[index]->end();
}

//Insers a string into the hash table
void HashTable::insert(string value) {
    int index = hash(value);
    if (map[index] == NULL) {
        set<string>* mySet = new set<string>();
        mySet->insert(value);
        map[index] = mySet;
    }
    else {
        map[index]->insert(value);
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
