#!/bin/python2
from __future__ import division
import numpy as np
import math
import random
import os
f = open('tmp.txt', 'w')
def bruteForce(sad, happy, sadx, happyx):
        return max(sad[sadx], happy[happyx])
            
def findMedian(sad, happy, sadStart, sadEnd, happyStart, happyEnd):
    if (sadEnd - sadStart == 0):
        return bruteForce(sad, happy, sadEnd, happyEnd)

    if (sadEnd - sadStart)%2 == 0:
        sadMed = sad[int((sadEnd + sadStart)/2)]
        happyMed = happy[int((happyEnd + happyStart)/2)]
    
        if sadMed > happyMed:
            sadEnd = int((sadEnd + sadStart)/2)
            happyStart = int((happyEnd + happyStart)/2)

        else:
            sadStart = int((sadEnd + sadStart)/2)
            happyEnd = int((happyEnd + happyStart)/2)

    else:
        sadMed = sad[int((sadEnd + sadStart)/2)+1]
        happyMed = happy[int((happyEnd + happyStart)/2)+1]

        if sadMed > happyMed:
            sadEnd = int((sadEnd + sadStart)/2)
            happyStart = int((happyEnd + happyStart)/2)

        else:
            sadStart = int((sadEnd + sadStart)/2)+1
            happyEnd = int((happyEnd + happyStart)/2)

    print sadStart, sadEnd
    print happyStart, happyEnd
    return findMedian(sad, happy, sadStart, sadEnd, happyStart, happyEnd)


lst = list(set([random.randint(0, 2000) for i in range(10)]))
sad = lst[:int(len(lst)/2)]
happy = lst[int(len(lst)/2):]

sad = sorted(sad)
happy = sorted(happy)
print sad
print happy


sadStart = happyStart = 0
sadEnd = happyEnd = len(sad)
sad = [1, 2, 3, 11, 12]
happy = [6, 7, 8, 9, 10]


total = sorted(sad+happy)
gah = sad + happy
for x in gah:
    f.write(str(x) + " ")
print findMedian(sad, happy, 0, len(sad)-1, 0, len(happy)-1)
print total[int(len(total)/2)]
print total
