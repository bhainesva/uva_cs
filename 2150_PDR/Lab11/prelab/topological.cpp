/*
 * Name: Ben Haines
 * ID: bmh5wx
 * Date: 12/02/14
 * Filename: topological.cpp
 */

#include <iostream>
#include <unordered_map>
#include <fstream>
#include <stdlib.h>
#include <vector>
#include <string>
using namespace std;

/** @brief Node class that makes up the graph.
 *
 * Nodes make up the graph. They each contain a string value, a vector that contains pointers to adjacent nodes, and a boolean value representing whether the node has been visited.
 * @author Ben Haines
 * @version 0.1
 * @date 12/02/14
 */
class Node {
    public:
        vector<Node*> adj;
        string value;
        bool mark;
        /** @brief Default constructor.
         *
         * Constructor that takes no arguments.
         * @author Ben Haines
         * @version 0.1
         * @date 12/02/14
         */
        Node() {
            value = "";
            mark = false;
        }

        /** @brief Single argument constructor
         *
         * Constructor that takes a string value as an argument and created a node with that value.
         * @param val Value of node.
         * @author Ben Haines
         * @version 0.1
         * @date 12/02/14
         */
        Node(string val) {
            value = val;
            mark = false;
        }

        /** @brief Adds an edge to the node.
         *
         * Method adds an additional node pointer to the node's adjacency vector.
         * @param w New node to mark as adjacent to current node.
         * @author Ben Haines
         * @version 0.1
         * @date 12/02/14
         */
        void addEdge(Node* w) {
            adj.push_back(w);
        }

        /** @brief Marks node as visited.
         *
         * Marks the node as visited.
         * @author Ben Haines
         * @version 0.1
         * @date 12/02/14
         */
        void setMark() {
            mark = true;
        }
};

/** @brief Visits node and adjacent nodes.
 *
 * This function visits the node passed as an argument and then recursively visits all of its adjacent nodes.
 * @param inNode Node to visit.
 * @param store Vector that holds the topological order of nodes.
 * @author Ben Haines
 * @version 0.1
 * @date 12/02/14
 */
void visit(Node *inNode, vector<Node*> &store) {
    if (!inNode->mark) {
        for (int i =0; i< inNode->adj.size(); i++) {
                visit(inNode->adj[i], store);
        }
        inNode->setMark();
        store.push_back(inNode);
    }
}
        

            

/** @brief Main method. Performs topological sort.
 *
 * This function takes a filename as its single command line argument. The file contains a list of edges. The output is the classes prrinted in a topologically sorted order.
 * @author Ben Haines
 * @version 0.1
 * @date 12/02/14
 */
int main (int argc, char **argv) {
    unordered_map<string, int> stringMap;
    vector<Node*> graph = vector<Node*>();

    if ( argc != 2 ) {
        cout << "Must supply the input file name as the one and only parameter" << endl;
        exit(1);
    }
    ifstream file(argv[1], ifstream::binary);
    if ( !file.is_open() ) {
        cout << "Unable to open file '" << argv[1] << "'." << endl;
        exit(2);
    }

    string s1, s2;
    int count = 0;
    file >> s1;
    file >> s2;
    while (s1 != s2) {
        if (stringMap.find(s1) == stringMap.end()) {
            stringMap.insert({s1, count});
            count++;
            Node * alice = new Node(s1);
            graph.push_back(alice);
        }
        if (stringMap.find(s2) == stringMap.end()) {
            stringMap.insert({s2, count});
            count++;
            Node * bob = new Node(s2);
            graph.push_back(bob);
        }
    
        Node* carl;
        Node* derick;
        for (int j = 0;j < graph.size(); j++) {
            if (graph[j]->value == s1) {
                carl = graph[j];
            }
            if (graph[j]->value == s2) {
                derick = graph[j];
            }
        }
        carl->addEdge(derick);

        file >> s1;
        file >> s2;
    }

    vector<Node*> sorted;
    for (int i=0;i<graph.size();i++) {
        if (!graph[i]->mark) {
            visit(graph[i], sorted);
        }
    }

    file.close();
    for (int k = sorted.size()-1; k>= 0; k--) {
        cout << sorted[k]->value << endl;
    }
}


