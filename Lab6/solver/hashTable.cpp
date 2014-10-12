/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: hashTable.cpp
 * Date: 10/12/14
 * Description: HashTable class implementation
 */
#include <string>
#include "hashTable.h"
#include "List.h"
#include <vector>
#include "hashFunctions.h"
#include <typeinfo>

using namespace std;

HashTable::HashTable(int size) {
    map = new List*[getNextPrime(size)];
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

