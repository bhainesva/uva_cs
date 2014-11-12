int foo(int x[]) {
    return x[0];
}

int foo2(int x[]) {
    return x[1];
}

int foo3(int x[]) {
    return x[2];
}

int main() {
    int y[] = {1, 2, 3};
    foo(y);
    foo2(y);
    foo3(y);

    return 0;
}
