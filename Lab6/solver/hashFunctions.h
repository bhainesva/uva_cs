#ifndef HASH_FUNCTIONS
#define HASH_FUNCTIONS

#include "hashTable.h"

using namespace std;

bool checkprime(unsigned int p);
int getNextPrime(unsigned int n);
int hash(string key);
HashTable* readInDict(string filename);
bool readInTable (string filename, int &rows, int &cols);

#endif
