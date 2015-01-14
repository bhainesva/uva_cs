#!/bin/python2
coinDict = {25 : "Q",
            10 : "D",
            5  : "N",
            1  : "P"}

while(True):
    money = raw_input()
    if money == '-1.00':
        break
    change = int(money.split('.')[1])
    outStr = "$" + money
    for coin in coinDict.keys():
        while change >= coin:
            change -= coin
            outStr += " " + coinDict[coin]
    print outStr


