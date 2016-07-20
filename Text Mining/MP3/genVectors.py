import numpy as np
import csv

f = open('projectionVectorsl12.txt','w')
for i in range(12):
    x = np.random.uniform(-1.0, 1.0, 1780)
    for j in x:
        f.write(str(j)) 
        f.write(',') 
    f.write('\n') 
f.close() 
