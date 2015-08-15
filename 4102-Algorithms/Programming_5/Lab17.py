#!/bin/python2
import pdb
import math

def read_words(fname):
    infile=open(fname)
    temp=infile.read()
    infile.close()

    array = []
    a = 0
    for x in temp:
        array.append(x)
    return array
    

def generate_neighbors(array):
    lookup = {}
    box = []
    for a in range((int)(math.sqrt(len(array)))):
        box.append({})

    for i in range(1, len(array)+1):
        #element = array[i]
        row = i/9
        col = i%9
        lookup[i] = []
        for a in range(row*9, (row+1)*9):
            lookup[i].append(a)
        for a in range(col, len(array), 9):
            lookup[i].append(a)


        box = []
        box.append([1,2,3,10,11,12,19,20,21])
        box.append([4,5,6,13,14,15,22,23,24])
        box.append([7,8,9,16,17,18,25,26,27])
        box.append([28,29,30,37,38,39,46,47,48])
        box.append([31,32,33,40,41,42,49,50,51])
        box.append([34,35,36,43,44,45,52,53,54])
        box.append([55,56,57,64,65,66,73,74,75])
        box.append([58,59,60,67,68,69,76,77,78])
        box.append([61,62,63,70,71,72,79,80,81])
        #box = [[1,2,3,10,11,12,19,20,21][4,5,6,13,14,15,22,23,24][7,8,9,16,17,18,25,26,27][28,29,30,37,38,39,46,47,48][31,32,33,40,41,42,49,50,51][34,35,36,43,44,45,52,53,54][55,56,57,64,65,66,73,74,75][58,59,60,67,68,69,76,77,78][61,62,63,70,71,72,79,80,81]]

        for b in box:
            if i in b:
                for a in b:
                    if a not in lookup[i]:
                        lookup[i].append(a)
            break

        

        
    return lookup

def guess_colors(lookup, depth, color_table, possibility_list, useda,curr_past):
    #pdb.set_trace()
    used = {}
    for a in useda.keys():
        used[a] = True

    if depth > 0:
        possibility_list[curr_past] = []
        for y in lookup[curr_past]:
            if curr_past in color_table and color_table[curr_past] in possibility_list[y]:
                possibility_list[y].remove(color_table[curr_past])
        
    if not check_possibilities(possibility_list,used):
        #del used[curr]
        return False, color_table

    if depth == len(lookup.keys()):
        return True, color_table

    #find mrv
    mrv = 100
    max_neighbors = 0
    curr = 0
    for x in lookup.keys():
        if x in used:
            continue
        if len(possibility_list[x]) < mrv:
            curr = x
            mrv = len(possibility_list[x])
            max_neighbors = len(lookup[x])
        elif len(possibility_list[x]) > mrv:
            continue
        else:
            if len(lookup[x]) > max_neighbors:
                curr = x
                max_neighbors = len(lookup[x])

    #pdb.set_trace()            
    if curr == 0:
        #del used[curr]
        return False, color_table





    used[curr] = True
    for x in possibility_list[curr]:
        color_table[curr] = x
        a, b = guess_colors(lookup, depth+1, color_table, possibility_list, used, curr)
        if a == True:
            return a,b
    #del used[curr]
    return False, color_table
        
def check(lookup, color_table):
    for x in lookup.keys():
        color = color_table[x]
        for neighbor in lookup[x]:
            if color_table[neighbor] == color:
                return False
    return True

def check_possibilities(possibility_list, used):
    for a in possibility_list.keys():
        if len(possibility_list[a]) == 0 and a not in used:
            print a
            return False
    return True
    #checks each round to see if there is any region without a possible color

def fill_pos_list(lookup):
    pos_list = {}
    for a in lookup.keys():
        pos_list[a] = [1,2,3,4,5,6,7,8,9]
    return pos_list

def make_color_table(lookup):
    color_table = {}
    for x in lookup.keys():
        color_table[x] = -1
    return color_table

def find_used(array):
    used = {}
    for e in range(len(array)):
        if array[e] != '.':
            used[e+1] = True
    return used
def main():
    array = read_words('sudoku.txt')
    used = find_used(array)
    lookup = generate_neighbors(array)
    color_table = make_color_table(lookup)
    possibility_list = fill_pos_list(lookup)
    a, color_table = guess_colors(lookup, 0, {}, possibility_list, used, 0)
    print color_table
    print a
    
    

#pdb.set_trace()
main()
