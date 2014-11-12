int foo(int &x) {
    return x+3;
}

int main() {
    int y = 4;
    foo(y);

    return 0;
}


