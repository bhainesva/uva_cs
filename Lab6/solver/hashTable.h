/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Date: 10/12/14
 * Filename: hashTable.h
 * Description: hashTable class definition 
 */
#ifndef HASH_TABLE
#define HASH_TABLE
#include <vector>
#include <string>
#include "List.h"

class List;

using namespace std;

class HashTable {
private:
    List** map;
public:
    HashTable(int size); //constructor
    //~HashTable(); //deconstructor
    bool get(string keyVal); //retrive value by key
    void insert(string value); //inserts new node into the table
};
#endif
