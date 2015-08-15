
import pdb

def read_words(fname):
    infile=open(fname)
    temp=infile.read()
    infile.close()
    return temp.split('\n')[:-1]


def generate_neighbors(array):
    lookup = {}
    for element in array:
        temp = element.split(' ')[:]
        if temp[0] in lookup:
            lookup[temp[0]].append(temp[1])
        else:
            lookup[temp[0]] = [temp[1]]

        if temp[1] in lookup:
            lookup[temp[1]].append(temp[0])
        else:
            lookup[temp[1]] = [temp[0]]

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
        pos_list[a] = [0,1,2]
    return pos_list

def make_color_table(lookup):
    color_table = {}
    for x in lookup.keys():
        color_table[x] = -1
    return color_table
def main():
    array = read_words('usa.txt')
    lookup = generate_neighbors(array)
    color_table = make_color_table(lookup)
    possibility_list = fill_pos_list(lookup)
    a, color_table = guess_colors(lookup, 0, {}, possibility_list, {}, 0)
    print color_table
    print a
    
    

#pdb.set_trace()
main()
