#!/bin/bash
# Name: Ben Haines
# ID: bmh5wx
# Date: 10/21/14
# Filename: averagetime.sh

# Get input
iters=6
echo "enter the exponent for counter.cpp:"
read exponent
declare -a times

if [[ $exponent = "quit" ]] ; then
    exit 0
else
    for i in {1..5} ; do
      echo "Running iteration $i ..."
      times[i]=`./a.out $exponent | tail -1`
      echo "time taken: $times[$1] ms"
    done
fi

#Expression to calculate the average time
average=($times[1] + $times[2] + $times[3] + $times[4] + $times[5])/5

#Display the average
echo "$iters iterations took $((average)) ms"

#Exit
exit 0
