#include <fstream>
#include <string>
#include <vector>
#include <stdlib.h>
#include <iostream>
#include "hashTable.h"
#include <algorithm>
#include "hashFunctions.h"
#include "timer.h"

using namespace std;

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
    int wordCount = 0;
    vector<string> foundWords;

    timer t;
    t.start();
    for (int r=0; r<rows;r++) {
        for (int c=0; c<cols; c++) {
            for (int dir=0;dir<8;dir++) {
                for (int len=3;len<22 && (len <= cols || len<= rows);len++) {
                    string word = getWordInTable(r, c, dir, len, rows, cols);
                    if (word.length() > 2) {
                        if (wordTable->get(word)) {
                            if(std::find(foundWords.begin(), foundWords.end(), word + (char)(((int)'0')+dir) + (char)(((int)'0')+r) + (char)(((int)'0')+c)) != foundWords.end()) {
                            } else {
                                cout << dir << "(" << r << ", " << c << "): " << word << endl;
                                foundWords.push_back(word + (char)(((int)'0')+dir) + (char)(((int)'0')+r) + (char)(((int)'0')+c));
                                wordCount++;
                            }        
                        }
                    }
                }
            }
        }
    }
    t.stop();
    cout << wordCount << " words found." << endl;
    cout << "Found all words in " << t << " seconds." << endl;
    cout << (int)(t.getTime() * 1000) << endl;
    delete wordTable;

    return 0;
}


