#!/bin/python2
import math
import itertools
import copy
import sys

def pointDistance(p1, p2):
    (x1, y1) = p1
    (x2, y2) = p2
    return math.sqrt((x1 - x2)**2 + (y1 - y2)**2)

def bruteForce(P):
    minDistance = -1
    for pair in itertools.combinations(P, 2):
        if pointDistance(pair[0], pair[1]) < minDistance or minDistance == -1:
            minDistance = pointDistance(pair[0], pair[1])

    return minDistance

def solveCase(f, numPoints):
    X = []

    for i in range(numPoints):
        point = f.readline()
        x, y = point.split()
        X.append((float(x), float(y)))

    result =  bruteForce(X)
    if result <= 10000:
        print "%0.4f" % (result)
    else:
        print "infinity"

if __name__ == "__main__":
    count = 1
    f = open(sys.argv[1], 'r')
    with open('bruteout.txt', 'w') as the_file:
        numPoints = int(f.readline().strip())
        while numPoints != 0:
            solveCase(f, numPoints)
            count += 1
            numPoints = int(f.readline().strip())

