#!/bin/python2
import sys

class Edge():
    def __init__(self, u, v, cap=0):
        self.source = u
        self.sink = v  
        self.capacity = cap
        self.flow = 0
 
class Graph():
    def __init__(self):
        self.adjList = {}
 
    def addNode(self, node):
        self.adjList[node] = []
 
    def E(self, v):
        return self.adjList[v]
 
    def addEdge(self, u, v, cap):
        edge = Edge(u,v,cap)
        backflow = Edge(v,u)
        edge.backflow = backflow
        backflow.backflow = edge
        self.adjList[u].append(edge)
        self.adjList[v].append(backflow)
        edge.flow = 0
        backflow.flow = 0
 
    def augmentedPath(self, source, sink, path):
        if source == sink:
            return path
        for edge in self.E(source):
            remainingCap = edge.capacity - edge.flow
            # Can we push flow through this edge?
            if remainingCap > 0 and edge not in path:
                extPath = self.augmentedPath( edge.sink, sink, path + [edge]) 
                if extPath != []:
                    return extPath
        # There is no path, try different edge
        return []
 
    def maxFlow(self, source, sink):
        path = self.augmentedPath(source, sink, [])
        # While there is an augmented path
        while path != []:
            residualCapacity = [edge.capacity - edge.flow for edge in path]
            c_fp = min(residualCapacity)
            # Augment path
            for edge in path:
                edge.flow += c_fp
                edge.backflow.flow -= c_fp
            # Get new path
            path = self.augmentedPath(source, sink, [])
        # Return flow into sink, guaranteed to be max flow
        return sum(-edge.flow for edge in self.E(sink))

if __name__ == "__main__":
    f = open(sys.argv[1], 'r')
    r,n,c = 1,1,1
    r,c,n = f.readline().split()
    r,c,n = int(r), int(c), int(n)
    while not (r == n == c == 0):
        students = set([])
        classes = set([])
        g = Graph()
        g.addNode("s")
        g.addNode("t")

        for i in range(r):
            student, clss = f.readline().split()
            if not "student"+student in students:
                g.addNode("student" + student)
                students.add("student" + student)
                g.addEdge("s", "student"+student, n)
            if not "class"+clss in classes:
                g.addNode("class"+clss)
                classes.add("class"+clss)
            g.addEdge("student"+student, "class"+clss, 1)
        for i in range(c):
            clss, cap = f.readline().split()
            cap = int(cap)
            g.addEdge("class"+clss, "t", cap)
        if g.maxFlow("s", "t")  == n*len(students):
            print "YES"
        else:
            print "NO"
        f.readline()
        r,c,n = f.readline().split()
        r,c,n = int(r), int(c), int(n)
