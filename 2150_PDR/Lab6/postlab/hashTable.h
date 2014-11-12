/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: hashTable.cpp
 * Date: 10/17/14
 * Description: Definition of hashTable class
 */
#ifndef HASH_TABLE
#define HASH_TABLE
#include <vector>
#include <string>
#include <set>

class List;

using namespace std;

class HashTable {
private:
    set<string>** map;
    unsigned int space;
public:
    HashTable(unsigned int size); //constructor
    ~HashTable(); //deconstructor
    int hash(string key);
    void makeEmpty(); // Remove all elements of map
    bool get(string keyVal); //retrive value by key
    void insert(string value); //inserts new node into the table
};
#endif
