// Insert your header information here
/*
* Name: Ben Haines
* ID: bmh5wx
* File: TreeCalc.cpp
* Date: 09/26/14
*/
// TreeCalc.cpp:  CS 2150 Tree Calculator method implementations

#include "TreeCalc.h"
#include <iostream>
#include <cstdlib>

using namespace std;

//Constructor
TreeCalc::TreeCalc() {
    stack<TreeNode*> mystack;
}

//Destructor- frees memory
TreeCalc::~TreeCalc() {
    while(!mystack.empty()) {
        cleanTree(mystack.top());
        mystack.pop();
    }
}

//Deletes tree/frees memory
void TreeCalc::cleanTree(TreeNode* ptr) {
    if(ptr->left != NULL) {
        cleanTree(ptr->left);
        ptr->left = NULL;
    }
    if(ptr->right != NULL) {
        cleanTree(ptr->right);
        ptr->right = NULL;
    }
    if(ptr->left == NULL && ptr->right == NULL) {
        delete ptr;
    }
}

//Gets data from user
void TreeCalc::readInput() {
    string response;
    cout << "Enter elements one by one in postfix notation" << endl
         << "Any non-numeric or non-operator character,"
         << " e.g. #, will terminate input" << endl;
    cout << "Enter first element: ";
    cin >> response;
    //while input is legal
    while (isdigit(response[0]) || response[0]=='/' || response[0]=='*'
            || response[0]=='-' || response[0]=='+' ) {
        insert(response);
        cout << "Enter next element: ";
        cin >> response;
    }
}

//Puts value in tree stack
void TreeCalc::insert(const string& val) {
    // insert a value into the tree
    TreeNode * newNode = new TreeNode(val);
    if(isdigit(val[val.length()-1])) {
        mystack.push(newNode);
    }
    else {
        newNode->right = mystack.top();
        mystack.pop();
        newNode->left = mystack.top();
        mystack.pop();
        mystack.push(newNode);
    }
}

//Prints data in prefix form
void TreeCalc::printPrefix(TreeNode* ptr) const {
    // print the tree in prefix format
    cout << ptr->value << " ";
    if (ptr->left != NULL) {
        printPrefix(ptr->left);
    }
    if (ptr->right != NULL) {
        printPrefix(ptr->right);
    }
}

//Prints data in infix form
void TreeCalc::printInfix(TreeNode* ptr) const {
    // print tree in infix format with appropriate parentheses
    if (ptr->left != NULL) {
        if (!isdigit(ptr->left->value[ptr->left->value.length()-1])) {
            cout << "(";
            printInfix(ptr->left);
            if (ptr->right == NULL) {
                cout << ")";
            }
        }
        else {
            cout << "(";
            printInfix(ptr->left);
        }
    }
    if (isdigit(ptr->value[ptr->value.length()-1])) {
        cout << ptr->value;
    }
    else {
        cout << " " << ptr->value << " ";
    }
    if (ptr->right != NULL) {
        if (!isdigit(ptr->right->value[ptr->right->value.length()-1])){
            if (ptr->left == NULL) {
                cout << "(";
            }
            printInfix(ptr->right);
            cout << ")";
        }
        else{
            printInfix(ptr->right);
            cout << ")";
        }
    }
}

//Prints data in postfix form
void TreeCalc::printPostfix(TreeNode* ptr) const {
    // print the tree in postfix form
    if (ptr->left != NULL) {
        printPostfix(ptr->left);
    }
    if (ptr->right != NULL) {
        printPostfix(ptr->right);
    }
    cout << ptr->value << " ";
}

// Prints tree in all 3 (pre,in,post) forms

void TreeCalc::printOutput() const {
    if (mystack.size()!=0 && mystack.top()!=NULL) {
        cout << "Expression tree in postfix expression: ";
        // call your implementation of printPostfix()
        this->printPostfix(mystack.top());
        cout << endl;
        cout << "Expression tree in infix expression: ";
        // call your implementation of printInfix()
        this->printInfix(mystack.top());
        cout << endl;
        cout << "Expression tree in prefix expression: ";
        // call your implementation of printPrefix()
        this->printPrefix(mystack.top());
        cout << endl;
    } else
        cout<< "Size is 0." << endl;
}

//Evaluates tree, returns value
// private calculate() method
int TreeCalc::calculate(TreeNode* ptr) const {
    // Traverse the tree and calculates the result
    if (ptr->left == NULL && ptr->right == NULL) {
        return atoi(ptr->value.c_str());
    }
    if (ptr->value == "+") {
        return calculate(ptr->left) + calculate(ptr->right);
    }
    else if (ptr->value == "/") {
        return calculate(ptr->left) / calculate(ptr->right);
    }
    else if (ptr->value == "*") {
        return calculate(ptr->left) * calculate(ptr->right);
    }
    else if (ptr->value == "-") {
        if (ptr->right != NULL) {
            return calculate(ptr->left) - calculate(ptr->right);
        }
        else {
            return -calculate(ptr->left);
        }
    }

    return 77;
}

//Calls calculate, sets the stack back to a blank stack
// public calculate() method. Hides private data from user
int TreeCalc::calculate() {
    int i = calculate(mystack.top());
    // call private calculate method here
    return i;
}
