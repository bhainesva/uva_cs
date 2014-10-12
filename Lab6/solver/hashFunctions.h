#ifndef HASH_FUNCTIONS
#define HASH_FUNCTIONS

#include "hashTable.h"
#define MAXROWS 500
#define MAXCOLS 500
extern char table[MAXROWS][MAXCOLS];

using namespace std;

bool checkprime(unsigned int p);
int getNextPrime(unsigned int n);
int hash(string key);
HashTable* readInDict(string filename);
bool readInTable (string filename, int &rows, int &cols);
char* getWordInTable (int startRow, int startCol, int dir, int len,
                      int numRows, int numCols);

#endif
