#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <set>
#include <vector>
#include <queue>
#include <stack>
#include <string>
#include <algorithm>
#include <unordered_map>

using namespace std;

class Graph {
    private:
        int V;
        vector<int>* adjacent[100];

    public:
        Graph(int vertices) {
            V = vertices;
            for (int i =0;i<100; i++) {
                adjacent[i] = new vector<int>;
            }
        }

        void insert(int v, int w) {
            adjacent[v]->push_back(w);
        }

        int getV() {
            return V;
        }

        vector<int>* adj(int v) {
            return adjacent[v];
        }
};

class DepthFS {
    private:
        vector<int>* pre;
        vector<int>* post;
        queue<int>* postorder;
        queue<int>* preorder;
        vector<bool>* marked;
        int preCounter;
        int postCounter;

    public:
        DepthFS(Graph G) {
            vector<bool> *marked = new vector<bool>(100);
            for (int i=0;i<100;i++) {
                marked->at(i) = false;
            }

            for (int v = 0; v < G.getV(); v++) {
                if (!marked->at(v)) {
                    dfs(G, v);
                }
            }

            preCounter = 0;
            postCounter = 0;
        
        }

        void dfs(Graph G, int v) {
            marked->at(v) = true;
            pre->at(v) = preCounter++;
            preorder->push(v);
            for (int w=0;w< G.adj(v)->size();w++) {
                int tmp = G.adj(v)->at(w);
                if (!(marked->at(tmp))) {
                    dfs(G, w);
                }
            }
            postorder->push(v);
            post->at(v) = postCounter++;
        }

        vector<int>* reversePost() {
            vector<int> *reverse = new vector<int>();
            while(!postorder->empty()) {
                reverse->push_back(postorder->back());
                postorder->pop();
            }
            return reverse;
        }
};

class TSort {
    private:
        vector<int>* order;

    public:
        TSort(Graph G) {
            DepthFS *dfs = new DepthFS(G);
            order = dfs->reversePost();
        }
};

int main (int argc, char **argv) {
    unordered_map<string, int> stringMap;
    // verify the correct number of parameters
    if ( argc != 2 ) {
        cout << "Must supply the input file name as the one and only parameter" << endl;
        exit(1);
    }
    // attempt to open the supplied file
    ifstream file(argv[1], ifstream::binary);
    // report any problems opening the file and then exit
    if ( !file.is_open() ) {
        cout << "Unable to open file '" << argv[1] << "'." << endl;
        exit(2);
    }
    Graph classGraph = Graph(100);
    // read in two strings
    string s1, s2;
    file >> s1;
    file >> s2;
    int count = 0;
    while (s1 != s2) {
        if (stringMap.find(s1) == stringMap.end()) {
            stringMap.insert({s1, count});
            count++;
        }
        if (stringMap.find(s2) == stringMap.end()) {
            stringMap.insert({s2, count});
            count++;
        }
        classGraph.insert(stringMap.find(s1)->second, stringMap.find(s2)->second);
        file >> s1;
        file >> s2;
    }
    TSort myTsort = TSort(classGraph);

    // string comparison done with ==, but not shown here
    // close the file before exiting
    file.close();

}


