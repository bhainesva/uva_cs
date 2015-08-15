#!/bin/python2
import sys

class Edge():
    def __init__(self, u, v, cap):
        self.source = u
        self.sink = v  
        self.capacity = cap
 
class Graph():
    def __init__(self):
        self.adjList = {}
        self.flow = {}
 
    def addNode(self, node):
        self.adjList[node] = []
 
    def getEdges(self, v):
        return self.adjList[v]
 
    def addEdge(self, u, v, cap):
        edge = Edge(u,v,cap)
        redge = Edge(v,u,0)
        edge.redge = redge
        redge.redge = edge
        self.adjList[u].append(edge)
        self.adjList[v].append(redge)
        self.flow[edge] = 0
        self.flow[redge] = 0
 
    def augmentedPath(self, source, sink, path):
        if source == sink:
            return path
        for edge in self.getEdges(source):
            remainingCap = edge.capacity - self.flow[edge]
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
            residualCapacity = [edge.capacity - self.flow[edge] for edge in path]
            c_fp = min(residualCapacity)
            # Augment path
            for edge in path:
                self.flow[edge] += c_fp
                self.flow[edge.redge] -= c_fp
            # Get new path
            path = self.augmentedPath(source, sink, [])
        # Return flow into sink, guaranteed to be max flow
        return sum(-self.flow[edge] for edge in self.getEdges(sink))

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
                g.addNode("class"+clss)
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
