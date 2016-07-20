from __future__ import division
import matplotlib.pyplot as plt
import pandas as pd
import csv


# open the file in universal line ending mode 
with open('fxRanks0.01.csv', 'rU') as infile:
    df = pd.read_csv(infile)
    precision1 = df.P
    recall1 = df.R

with open('fxRanks0.1.csv', 'rU') as infile:
    df = pd.read_csv(infile)
    precision2 = df.P
    recall2 = df.R

with open('fxRanks5.0.csv', 'rU') as infile:
    df = pd.read_csv(infile)
    precision3 = df.P
    recall3 = df.R

with open('fxRanks20.0.csv', 'rU') as infile:
    df = pd.read_csv(infile)
    precision4 = df.P
    recall4 = df.R

with open('fxRanks100.0.csv', 'rU') as infile:
    df = pd.read_csv(infile)
    precision5 = df.P
    recall5 = df.R

with open('fxRanks500.0.csv', 'rU') as infile:
    df = pd.read_csv(infile)
    precision6 = df.P
    recall6 = df.R

plt.plot(recall1, precision1, label="0.01")
plt.plot(recall2, precision2, label="0.1")
plt.plot(recall3, precision3, label="5.0")
plt.plot(recall4, precision4, label="20.0")
plt.plot(recall5, precision5, label="100.0")
plt.plot(recall6, precision6, label="500.0")

plt.legend(bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.)
plt.ylabel("Precision")
plt.xlabel("Recall")
plt.savefig('curves.png', bbox_inches='tight')

