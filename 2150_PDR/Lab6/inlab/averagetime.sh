#!/bin/bash
# Name: Ben Haines
# ID: bmh5wx
# Date: 10/21/14
# Filename: averagetime.sh

#Run the solver 5 times and store the running times
# $1 is the wordlist, $2 is the puzzle to solve
R1=`./a.out $1 $2 | tail -1`
R2=`./a.out $1 $2 | tail -1`
R3=`./a.out $1 $2 | tail -1`
R4=`./a.out $1 $2 | tail -1`
R5=`./a.out $1 $2 | tail -1`

#Expression to calculate the average time
average=($R1+$R2+$R3+$R4+$R5)/5

#Display the average
echo $((average))

#Exit
exit 0
