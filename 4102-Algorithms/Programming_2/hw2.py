#!/bin/python2
from __future__ import division
import sys
import math

def solveCase(total, takeWith, shipOne, shipHalf):
    cost = 0
    while math.floor(total/2) > takeWith and math.ceil(total/2) * shipOne > shipHalf:
        cost += shipHalf
        total = math.floor(total/2)
    total -= takeWith
    cost += total * shipOne
    return cost

def readIn(inputs, f):
    (total, takeWith, numCompanies) = map(int, map(str.strip, inputs.split()))
    companies = []
    for i in range(numCompanies):
        line = f.readline()
        name, (shipOne, shipHalf) = line.split()[0], map(int, line.split()[1:])

        companies.append((name, solveCase(total, takeWith, shipOne, shipHalf)))
    results = sorted(sorted(companies), key=lambda x : x[1])
    for i in results:
        print i[0], int(i[1])

        

if __name__ == "__main__":
    f = open(sys.argv[1], 'r')
    cases = int(f.readline())

    for i in range(cases):
        print "Case ",  i+1
        line = f.readline()
        readIn(line, f)
