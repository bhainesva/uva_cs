#include <iostream>
#include <string>
#include <cstdlib>

using namespace std;

string bitCounter(int num, int index, int size) {
    string out = "";
    while(num != 0) {
        if(num % 2 == 0) {
            out = "1" + out;
        }
        else {
            out = "0" + out;
        }
        num = (num/2);
    }
    while (out.length() < size) {
        out = "0" + out;
    }
    
    cout << "Binary: " << out << endl;
    return out.substr(out.length() - index, 1);
}

int main(int argc, char *argv[]) {
    int nu = atoi(argv[1]);
    int ind = atoi(argv[2]);
    int si = atoi(argv[3]);

    cout << bitCounter(nu, ind, si) << endl;

    return 0;
}

