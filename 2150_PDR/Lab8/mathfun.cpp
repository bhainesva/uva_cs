/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Date: 10/31/14
 * Filename: mathfun.cpp
 */

#include <iostream>
#include <time.h>
#include <cstdlib>

using namespace std;

extern "C" int product (int, int);
extern "C" int power (int, int);

int  main () {
    int  a, b, prod, pow;
    cout << "Please enter the first number: ";
    cin >> a;

    cout << "Please enter the second number: ";
    cin >> b;

    // sum up the vector and print out results
    prod = product(a, b);
    cout << "The product of a and b is: " << prod << endl;
    pow = power(a, b);
    cout << "a^b is: " << pow << endl;
    return 0;
}

