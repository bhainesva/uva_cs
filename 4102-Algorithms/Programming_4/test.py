#!/bin/python2
import sys
import math

def keyfunc(x, heightmap, solutions):
    if x != 0:
        return x
    return solveRC(heightmap, len(heightmap), len(heightmap[0]), solutions)
    

def solveRC(heightmap, r, c, solutions):
    if (r < 0 or r > len(heightmap) - 1 or c < 0 or c > len(heightmap[0]) - 1): 
        return 0

    if (solutions[r][c] != 0):
        return solutions[r][c]

    dirs = [(-1, 0), (1, 0), (0, 1), (0, -1)]
    paths = [0]
    
    for dr in dirs:
        if(0 <= r+dr[0] < len(heightmap) and 0 <= c+dr[1] < len(heightmap[0]) and heightmap[r+dr[0]][c+dr[1]] < heightmap[r][c]):
           paths.append(solveRC(heightmap,r+dr[0],c+dr[1],solutions))

    solutions[r][c] = max(paths) + 1
    return solutions[r][c]
    
def maxPath(heightmap, rows, cols, solutions):
        maxPath = max(
        for r in xrange(rows):
            for c in xrange(cols):
                if solutions[r][c]==0:
                    maxPath = max(maxPath, solveRC(heightmap, r, c, solutions))
                else:
                    maxPath = max(maxPath, solutions[r][c])

        return maxPath

def solve(line, f):
    name, r, c = line.split()
    r = int(r)
    c = int(c)
    solutions = [[0 for x in xrange(c)] for j in xrange(r)]
    matrix = []
    for i in range(r):
        matrix.append([int(x) for x in f.readline().split()])
    result = maxPath(matrix, r, c, solutions)
    print name + ": " + str(result)

if __name__ == "__main__":
    f = open(sys.argv[1], 'r')
    cases = int(f.readline())

    for i in range(cases):
        line = f.readline()
        solve(line, f)
