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
    unsigned int space;
public:
    HashTable(unsigned int size); //constructor
    ~HashTable(); //deconstructor
    void makeEmpty(); // Remove all elements of map
    bool get(string keyVal); //retrive value by key
    void insert(string value); //inserts new node into the table
};
#endif
