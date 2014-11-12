/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Filename: wordPuzzle.cpp
 * Date: 10/17/14
 * Description: Word search puzzle solver
 */
// Running Time Example: Using words.txt and 250x250.grid.txt, running time was 367.404595 seconds
// The program has a big theta running time of BigTheta(r*c*w) where r is the number of rows in the puzzle, c is the number of columns, and w is the number of words in the wordlist. For each square in the puzzle (r*c) the program has to do a constant number of checks if words are in the hashtable. This operation is linear in the worst case for my implementation of the hash table.
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
    //So I can use indexes to print letter directions in output
    string directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
    // to hold the number of rows and cols in the input file
    int rows, cols;
    // attempt to read in the file
    bool result = readInTable (argv[2], rows, cols);
    // if there is an error, report it
    if ( !result ) {
        cout << "Error reading in file!" << endl;
        exit(1); // requires the <stdlib.h> #include header!
    }

    //Read in the wordlist
    HashTable *wordTable = readInDict(argv[1]);

    //stores total number of words found
    int wordCount = 0;

    //Using this to avoid finding words multiple times
    vector<string> foundWords;

    //sets up and starts timer
    timer t;
    t.start();
    
    //Loop over every square
    for (int r=0; r<rows;r++) {
        for (int c=0; c<cols; c++) {
            //In each direction
            for (int dir=0;dir<8;dir++) {
                //For every possible word length
                for (int len=3;len<22 && (len <= cols || len<= rows);len++) {
                    //Get the word, check if it's in the dictionary, and if we've already found it
                    string word = getWordInTable(r, c, dir, len, rows, cols);
                    if (word.length() > 2) {
                        if (wordTable->get(word)) {
                            if(std::find(foundWords.begin(), foundWords.end(), word + (char)(((int)'0')+dir) + (char)(((int)'0')+r) + (char)(((int)'0')+c)) != foundWords.end()) {
                            } else {
                                cout << directions[dir] << "(" << r << ", " << c << "): " << word << endl;
                                foundWords.push_back(word + (char)(((int)'0')+dir) + (char)(((int)'0')+r) + (char)(((int)'0')+c));
                                wordCount++;
                            }        
                        }
                    }
                }
            }
        }
    }
    //stop timer
    t.stop();

    //display output
    cout << wordCount << " words found." << endl;
    cout << "Found all words in " << t << " seconds" << endl;

    //clean up
    delete wordTable;

    return 0;
}


