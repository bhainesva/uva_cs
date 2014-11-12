#!/bin/bash
# Name: Ben Haines
# ID: bmh5wx
# Date: 10/21/14
# Filename: averagetime.sh

# Get input
iters=5
echo "enter the exponent for counter.cpp:"
read exponent
declare -a times

if [[ $exponent = "quit" ]] ; then
    exit 0
else
    for ((i=1; i<=iters; i++)); do
      echo "Running iteration $i ..."
      times[i]=`./a.out $exponent | tail -1`
      echo "time taken: ${times[i]} ms"
    done
fi

#Expression to calculate the average time
sum=0
for ((i=1; i<=iters; i++)); do
    sum=$(($sum + ${times[i]}))
done

#Display the total time
echo "$iters iterations took $sum ms"

#Display the average
echo "Average time was $(($sum/$iters)) ms"

#Exit
exit 0
