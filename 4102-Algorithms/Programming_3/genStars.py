#!/bin/python2
import math
import random

#50 test cases
for i in range(50):
    n = random.randint(2, 10000)
    with open('bigtest.txt', 'a') as f:
        f.write(str(n))
        f.write('\n')
        for i in range(n):
            x = random.uniform(-50000, 50000)
            y = random.uniform(-50000, 50000)
            f.write(str(x) + " " + str(y) + "\n")

with open('bigtest.txt', 'a') as f:
    f.write("0")
