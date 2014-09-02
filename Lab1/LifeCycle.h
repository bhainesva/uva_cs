//Name: Ben Haines
//ID: bmh5wx
//File Name: xToN.cpp
//Date: 08/31/14include <iostream>
#ifndef LIFECYCLE_H
#define LIFECYCLE_H
using namespace std;
// ---------------------------------------------------  class definition
class MyObject {
public:
    static int numObjs;
    MyObject(const char *n = "--default--");      // default constructor
    MyObject(const MyObject& rhs);                // copy constructor
    ~MyObject();                                  // destructor
    string getName() const {
        return name;
    }
    void setName(const string newName) {
        name = newName;
    }
    friend ostream& operator<<(ostream& output, const MyObject& obj);
private:
    string name;
    int id;
};

// ----------------------------------------------------------  print out
ostream& operator<<(ostream& output, const MyObject& obj); 

//---------------------------------------------------------- cmpMyObj
int cmpMyObj(const MyObject o1, const MyObject o2); 

//---------------------------------------------------------- getMaxMyObj
MyObject getMaxMyObj(const MyObject o1, const MyObject o2); 

//---------------------------------------------------------- swapMyObj
void swapMyObj(MyObject& o1, MyObject& o2);
//---------------------------------------------------------- prototypes
MyObject getMaxMyObj(const MyObject o1, const MyObject o2);
int cmpMyObj(const MyObject o1, const MyObject o2);
void swapMyObj(MyObject& o1, MyObject& o2);
#endif

