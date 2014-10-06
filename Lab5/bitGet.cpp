#include <iostream>
#include <string>

using namespace std;

string bitGet(int x) {
    string out = "";
    while (x!=0) {
        if(x%2 == 0) {
            out = "1"+out;
        }
        else {
            out = "0"+out;
        }
        x = x/2;
    }
    while(out.length() < 32) {
        out = "0" + out;
    }
    cout << out << endl;

    return out.substr(13, 1);
}

int main() {
    cout << bitGet(2867323233) << endl;
    cout << bitGet(2) << endl;

    return 0;
}
