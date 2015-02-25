#!/bin/python2
Hl = Hr = 0

def sort(lst):
    if (len(lst) == 1):
        return lst

    left = lst[:len(lst)/2]
    right = lst[len(lst)/2:]
    final = merge(sort(left), sort(right))
    
    return final

def merge(left, right):
    global Hl
    global Hr
    result = []
    count = 0
    while (not (len(left) == 0 and len(right) == 0)):
        if (len(left) == 0):
            result += right
            return result

        if (len(right) == 0):
            result += left
            return result

        if (left[0] < right[0]):
            Hl += len(right)
            result.append(left[0])
            left = left[1:]

        elif (left[0] == right[0]):
            tmp = left[0]
            left = left[1:]
            while len(right) != 0 and tmp == right[0]:
                result.append(right[0])
                right = right[1:]
            Hl += len(right)
            result.append(tmp)

        else:
            Hr += len(left)
            result.append(right[0])
            right = right[1:]

    return result


print sort([5, 4, 4, 4, 3, 2, 1])
print Hl
print Hr

