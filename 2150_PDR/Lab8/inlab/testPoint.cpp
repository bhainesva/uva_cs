int foo(int * x) {
    return *x+3;
}

int main() {
    int * c = new int;
    *c = 3;
    foo(c);

    return 0;
}

