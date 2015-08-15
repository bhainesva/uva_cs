#!/bin/python2
import sys

adjList = {}

class Edge():
    def __init__(self, v, cap=0):
        self.tail = v  
        self.capacity = cap
        self.flow = 0

def initEdge(u, v, cap):
    edge = Edge(v,cap)
    backflow = Edge(u)
    edge.backflow = backflow
    backflow.backflow = edge
    adjList[u].append(edge)
    adjList[v].append(backflow)
    edge.flow = 0
    backflow.flow = 0

def augmentedPath(source, sink, path):
    if source == sink:
        return path
    for edge in adjList[source]:
        # Can we push flow through this edge?
        if edge.capacity - edge.flow > 0 and edge not in path:
            extPath = augmentedPath(edge.tail, sink, path + [edge]) 
            if extPath != []:
                return extPath
    # There is no path, try different edge
    return []

def maxFlow(source, sink):
    path = augmentedPath(source, sink, [])
    # While there is an augmented path
    while path != []:
        c_fp = min([edge.capacity - edge.flow for edge in path])
        # Augment path
        for edge in path:
            edge.flow += c_fp
            edge.backflow.flow -= c_fp
        # Get new path
        path = augmentedPath(source, sink, [])
    # Return flow into sink, guaranteed to be max flow
    return sum(-edge.flow for edge in adjList[sink])

if __name__ == "__main__":
    f = open(sys.argv[1], 'r')
    r,n,c = 1,1,1
    r,c,n = f.readline().split()
    r,c,n = int(r), int(c), int(n)
    while not (r == n == c == 0):
        students = set([])
        classes = set([])
        adjList["s"] = []
        adjList["t"] =[]

        for i in range(r):
            student, clss = f.readline().split()
            if not "student"+student in students:
                adjList["student" + student] = []
                students.add("student" + student)
                initEdge("s", "student"+student, n)
            if not "class"+clss in classes:
                adjList["class"+clss] = []
                classes.add("class"+clss)
            initEdge("student"+student, "class"+clss, 1)
        for i in range(c):
            clss, cap = f.readline().split()
            cap = int(cap)
            initEdge("class"+clss, "t", cap)
        if maxFlow("s", "t")  == n*len(students):
            print "YES"
        else:
            print "NO"
        f.readline()
        r,c,n = f.readline().split()
        r,c,n = int(r), int(c), int(n)

