#!/bin/bash
# Name: Ben Haines
# ID: bmh5wx
# Date: 10/14/14
# Filename: averagetime.sh


#The first argument is the wordlist
wordlist="$1"

#The second argument is the puzzle to be solved
puzzle="$2"

#Run the solver 5 times and store the runnning times
R1=`./a.out $1 $2| tail -1`
R2=`./a.out $1 $2| tail -1`
R3=`./a.out $1 $2| tail -1`
R4=`./a.out $1 $2| tail -1`
R5=`./a.out $1 $2| tail -1`

#Compute the average running time
average=($R1 + $R2 + $R3 + $R4 + $R5)/5

#Echo the average running time
echo $(($average))

#Exit
exit 0
