#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include "heap.h"
#include "huffNode.h"
#include <string>

using namespace std;

void prefix(HuffNode * node, string base, string arr[]) {
    arr[(int)node->letter] = base;
    if (node->right != NULL) {
        prefix(node->left, base + "0", arr);
    }
    if (node->right != NULL) {
        prefix(node->right, base + "1", arr);
    }
}





int main (int argc, char **argv) {
    ///Number of chars in the input file
    int fileLen = 0;
    // verify the correct number of parameters
    if ( argc != 2 ) {
        cout << "Must supply the input file name as the one and only parameter" << endl;
        exit(1);
    }
    // attempt to open the supplied file.  FILE is a type desgined to
    // hold file pointers.  The first parameter to fopen() is the
    // filename.  The second parameter is the mode -- "r" means it
    // will read from the file.
    FILE *fp = fopen(argv[1], "r");
    // if the file wasn't found, output and error message and exit
    if ( fp == NULL ) {
        cout << "File '" << argv[1] << "' does not exist!" << endl;
        exit(2);
    }
    // read in each character, one by one.  Note that the fgetc() will
    // read in a single character from a file, and returns EOF when it
    // reaches the end of a file.
    int freqArr[256] = {0}; 
    heap minHeap = heap();
    char g;
    while ( (g = fgetc(fp)) != EOF ) {
        if ((int)g >= 32 && (int)g <= 127) {   
            fileLen+=1;
            freqArr[g] += 1;
        }
    }

    for (int i=0;i<256;i++) {
        if (freqArr[i] != 0) {
            HuffNode * node = new HuffNode((char)i, freqArr[i]);
            minHeap.insert(node);
        }
    }

    HuffNode * one;
    HuffNode * two;
    while (minHeap.size() > 1) {
        one = minHeap.deleteMin();
        two = minHeap.deleteMin();

        HuffNode * newNode = new HuffNode((char)(0), one->freq + two->freq);
        newNode->left = one;
        newNode->right = two;
        minHeap.insert(newNode);
    }

    string prefixArr[256] = {};
    for (int i=0; i<256; i++) {
        prefixArr[i] = "";
    }

    prefix(minHeap.findMin(), "", prefixArr);


    int distinctChars = 0;
    for (int i =32; i< 256; i++) {
        if (prefixArr[i] != "") {
            distinctChars += 1;
            cout << (char)i << " " << prefixArr[i] << endl;
        }
    }

    int totalArr[256] = {0}; 
    for (int i=40; i<256;i++) {
        totalArr[i] = freqArr[i] * prefixArr[i].length();
    }
    int totalBits = 0;
    for (int i=0;i<256;i++) {
        totalBits += totalArr[i];
    }

    cout << "----------------------------------------" << endl;

    rewind(fp);
    while ( (g = fgetc(fp)) != EOF )
        if ((int)g >= 32 && (int)g <= 122) {   
            cout << prefixArr[(int)g];
        }
    // close the file
    fclose(fp);
    cout << endl;
    cout << "----------------------------------------" << endl;
    cout << "There are a total of " << fileLen << " symbols that are encoded." << endl;
    cout << "There are a total of " << distinctChars << " distinct symbols used." << endl;
    cout << "There were a total of " << fileLen * 8 << " bits in the original file." << endl;
    cout << "There were " << totalBits << " bits in the compressed file." << endl;
    cout << "This gives a compression ration of " << (double)(fileLen * 8)/totalBits << "."<< endl;
    cout << "The cost of the Huffman tree is " << (double)totalBits/fileLen << " bits per character." << endl;

}


