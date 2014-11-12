struct simple {
  int x;
  int y;
};

class Test {
    int x;
    bool b;
    char c;
    simple s;

  public:
    void setX(int a);
    int y;
};

void Test::setX(int a) {
    this->x = a;
}

int main() {
    Test myTest = Test();
    myTest.setX(3);
    myTest.y = 2;
    return 0;
}



