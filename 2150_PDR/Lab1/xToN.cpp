//Name: Ben Haines
//ID: bmh5wx
//File Name: xToN.cpp
//Date: 08/31/14
#include <iostream>
#include <limits>
using namespace std;

long long xton(int base, int n) {
    if (!n) {
	return 1;
    }
    if (n == 1){
	return base;
    }

   return (long long)base * xton(base, n-1);
}

int main( ) {
    int x, y, z; 
    cout << "This program computes x^n for n>0, x and n must be ints" << endl;
    cout << "Enter x: ";
    cin >> x;
    if (cin.fail()){
	cout << "That wasn't an int, using 1 instead." << endl;
	cin.clear();
	cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
	x = 1;
    }
    cout << "Enter n: ";
    cin >> y;
    if (cin.fail()){
        cout << "That wasn't an int, using 1 instead." << endl;
	cin.clear();
	cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        y = 1;
    } 
    if (y < 0) {
      cout << "n must be greater than 0" << endl;
      return 0;
    }
    z = xton(x, y);
    cout << x << " ^ " << y << " = " << z << endl;
    return 0;
}
