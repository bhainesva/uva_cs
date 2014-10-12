#include <iostream>
#include "hashFunctions.h"
#include <stdlib.h>
using namespace std;

int main(int argc, char *argv[]) {
    //to hold the number of rows and cols in the input file
    int rows, cols;
    // attempt to read in the file
    bool result = readInTable (argv[2], rows, cols);
    // if there is an error, report it
   
    if ( !result ) {
        cout << "Error reading in file!" << endl;
        exit(1); // requires the <stdlib.h> #include header!
    }

    result = readInDict(argv[1]);
    if ( !result ) {
        cout << "Error reading in file!" << endl;
        exit(1); // requires the <stdlib.h> #include header!
    }
    return 1;
}
