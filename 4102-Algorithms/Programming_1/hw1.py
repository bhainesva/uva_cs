#!/bin/python2
import sys
import locale
locale.setlocale( locale.LC_ALL, '' )

coinDict = {25 : "Q",
            10 : "D",
            5  : "N",
            1  : "P"}

f = open(sys.argv[1], 'r')
for money in f:
    money = money.strip()
    if money == '-1.00':
        f.close()
        break
    change = int(money.split('.')[1])
    outStr = locale.currency(float(money), grouping=True)
    for coin in coinDict.keys():
        while change >= coin:
            change -= coin
            outStr += " " + coinDict[coin]
    outStr = outStr.replace('\n', '')
    print outStr


