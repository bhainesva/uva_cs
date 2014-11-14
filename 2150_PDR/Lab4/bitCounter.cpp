/*
* Name: Ben Haines
* ID: bmh5wx
* Filename: bitCounter.cpp
* Date: 09/19/14
*/

#include <iostream>
#include <string>
#include <cstdlib>

using namespace std;

string outputBinary(unsigned int x) {
    string out = "";
    while (x != 0){
            if(x%2 != 0){
                out = "1" + out;
            }
            else{
                out = "0" + out;
            }
            x = (x)/2;
    }
    return out;
}

int bitCount(string num) {
    if (num.length() == 1){
        if(num == "1"){
            return 1;
        }
        return 0;
    }
    if(num[num.length()-1] == '1'){
       return 1 + bitCount(num.substr(0, num.length()-1));
    }
    else{
        return 0 + bitCount(num.substr(0, num.length()-1));
    }
}
    
int main(int argc, char *argv[]) {
    if(argc <= 1){
        cout << "You need a command line argument." << endl;
        return 0;
    }
    int in = atoi(argv[1]);
    cout << bitCount(outputBinary(in)) << endl;
    return 0;
}
