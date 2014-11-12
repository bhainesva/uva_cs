struct simple {
  int x;
  int y;
} test;

int foo(simple z) {
    return z.x;
}

int main() {
    test.x = 3;
    test.y = 4;
    foo(test);
    return 0;
}


