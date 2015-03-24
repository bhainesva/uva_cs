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

def findNearest(X, Y):
    if len(X) <= 3:
        return bruteForce(X)

    XL = X[:len(X)/2]
    XR = X[len(X)/2:]

    YL = []
    YR = []

    for point in Y:
        if point in XL:
            YL.append(point)
        else:
            YR.append(point)

    dL = findNearest(XL, YL)
    dR = findNearest(XR, YR)

    d = min(dL, dR)

    l = XL[-1][0]
    Yprime = [point for point in Y if (l-d < point[0] < l+d)]

    for i in range(len(Yprime)):
        p = Yprime[i]
        for j in range(min(len(Yprime)-1, i+7), i, -1):
            if pointDistance(p, Yprime[j]) < d:
                d = pointDistance(p, Yprime[j])
    return d

def solveCase(f, numPoints):
    X = []

    for i in range(numPoints):
        point = f.readline()
        x, y = point.split()
        X.append((float(x), float(y)))
    Y = copy.deepcopy(X)
    X = sorted(X, key=lambda p: p[0])
    Y = sorted(Y, key=lambda p: p[1])

    result =  findNearest(X, Y)
    if result <= 10000:
        print "%0.4f" % (result)
    else:
        print "infinity"


if __name__ == "__main__":
    f = open(sys.argv[1], 'r')
    numPoints = int(f.readline().strip())
    while numPoints != 0:
        solveCase(f, numPoints)
        numPoints = int(f.readline().strip())
    f.close()
