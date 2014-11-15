/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Date: 10/31/14
 * Filename: threexinput.cpp
 */
#include <iostream>
#include "timer.h"

using namespace std;

extern "C" int collatz (int);

int  main () {
    timer t;
    t.start();
    int  n, steps, times;
    cout << "Please enter the number to check";
    cin >> n;

    cout << "Please enter the number of times to run";
    cin >> times;

    //sets up and starts timer
    // sum up the vector and print out results
    for (int i = 0; i < times; i++) {
        steps = collatz(n);
    }

    t.stop();

    cout << "Each run took an average of: " << t.getTime()/steps << endl;
    cout << n << " takes " << steps << " steps to reach 1" << endl;

    return 0;
}

