#!/bin/python2
from __future__ import division
import numpy as np
import math
import random

def bruteForce(sad, happy, sadStart, sadEnd, happyStart, happyEnd):
    if happy[happyStart] > sad[sadEnd-1]:
        return sad[sadEnd-1]
    elif sad[sadStart] > happy[happyEnd-1]:
        return happy[happyEnd-1]
    else:
        return min(happy[happyEnd-1], sad[sadEnd-1])
            
def findMedian(sad, happy, sadStart, sadEnd, happyStart, happyEnd):
    if (sadEnd - sadStart <= 1):
        return bruteForce(sad, happy, sadStart, sadEnd+1, happyStart, happyEnd+1)

    print sad
    print happy

    sadMed = sad[int((sadEnd + sadStart)/2)]
    happyMed = happy[int((happyEnd + happyStart)/2)]
    
    if sadMed > happyMed:
        sadEnd = int((sadEnd + sadStart)/2)
        happyStart = int((happyEnd + happyStart)/2)

    else:
        sadStart = int((sadEnd + sadStart)/2)
        happyEnd = int((happyEnd + happyStart)/2)

    print sadStart, sadEnd
    print happy[happyStart:happyEnd]
    return findMedian(sad, happy, sadStart, sadEnd, happyStart, happyEnd)


lst = list(set([random.randint(0, 2000) for i in range(11)]))
sad = lst[:int(len(lst)/2)]
happy = lst[int(len(lst)/2):]

sad = sorted(sad)
happy = sorted(happy)


sadStart = happyStart = 0
sadEnd = happyEnd = len(sad)

print findMedian(sad, happy, 0, len(sad)-1, 0, len(happy)-1)
total = sorted(sad+happy)
print total
print total[int(len(total)/2)]
