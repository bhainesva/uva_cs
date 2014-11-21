// This program is the skeleton code for the lab 10 in-lab.  It uses
// C++ streams for the file input, and just prints out the data when
// read in from the file.

#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <string>
#include <map>
#include "huffNode.h"
using namespace std;

void buildTree(HuffNode* root, string base, map<string, char> &preMap) {
    if (preMap.find(base) != preMap.end()) {
        root->letter = preMap.find(base)->second;
    }

    else {
        HuffNode* left = new HuffNode();
        HuffNode* right = new HuffNode();
        root->left = left;
        root->right = right;
        buildTree(left, base + "0", preMap);
        buildTree(right, base + "1", preMap);
    }
}
// main(): we want to use parameters
int main (int argc, char **argv) {
    map<string, char> prefixMap;
    // verify the correct number of parameters
    if ( argc != 2 ) {
        cout << "Must supply the input file name as the only parameter" << endl;
        exit(1);
    }
    // attempt to open the supplied file; must be opened in binary
    // mode, as otherwise whitespace is discarded
    ifstream file(argv[1], ifstream::binary);
    // report any problems opening the file and then exit
    if ( !file.is_open() ) {
        cout << "Unable to open file '" << argv[1] << "'." << endl;
        exit(2);
    }
    // read in the first section of the file: the prefix codes
    char buffer[256];
    while ( true ) {
        // read in first character on line
        char first = file.get();
        // catch DOS and UNIX newlines
        if ( (first == '\n') || (first == '\r') )
            continue;
        // read in second character on line
        char second = file.get();
        // did we encounter the separator?
        if ( (first == '-') && (second == '-') ) {
            // read in the rest of the line
            file.getline(buffer, 255, '\n');
            break;
        }
        // read in the prefix code
        file.getline(buffer, 255, '\n');
        prefixMap.insert(pair<string, char>(buffer, first));
    }
    HuffNode * root = new HuffNode();
    HuffNode * loop = root;
    string base = "";
    buildTree(root, base, prefixMap);
    // read in the second section of the file: the encoded message
    char bit;
    while ( (bit = file.get()) != '-' ) {
        if ( (bit != '0') && (bit != '1') )
            continue;
        // do something with the bit read in
        if (bit=='1') {
            loop = loop->right;
        }
        else {
            loop = loop->left;
        }
        if (loop->left == NULL && loop->right == NULL) {
            cout << loop->letter;
            loop = root;
        }
    }
    cout << endl;
    // close the file before exiting
    file.close();
}
