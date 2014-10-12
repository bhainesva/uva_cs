#include <fstream>
#include <string>
#include <vector>
#include <stdlib.h>
#include <iostream>
#include "hashTable.h"
#include "hashFunctions.h"

using namespace std;

// We create a 2-D array of some big size, and assume that the table
// read in will be less than this size (a valid assumption for lab 6)
#define MAXROWS 500
#define MAXCOLS 500

// Forward declarations
bool readInTable (string filename, int &rows, int &cols);
char* getWordInTable (int startRow, int startCol, int dir, int len,
                      int numRows, int numCols);

int main(int argc, char *argv[]) {
    // to hold the number of rows and cols in the input file
    int rows, cols;
    // attempt to read in the file
    bool result = readInTable (argv[2], rows, cols);
    // if there is an error, report it
    if ( !result ) {
        cout << "Error reading in file!" << endl;
        exit(1); // requires the <stdlib.h> #include header!
    }

    HashTable *wordTable = readInDict(argv[1]);

    for (int x=0; x < 3; x++) {
        for (int y = 0; y < 3; y++) {
            
}


