#include <stdio.h>
#include <stdlib.h>

/* A struct is like a class, but without methods */
struct node {
  int x;
  struct node *next;
};


int main() {
    struct node *current, *prev, *head;
    int n;

    printf("How long is the list?");
    scanf("%d", &n);

    int i;
    int num;

    for (i=0;i<n;i++) {
        printf("Enter a number: ");
        scanf("%d", &num);


        if (i == 0) {
            current = (struct node *)malloc(sizeof(struct node));
            current->x = num;
            current->next = NULL;
            prev = current;
            head = current;
        }

        else {
            current = (struct node *)malloc(sizeof(struct node));
            current->x = num;
            current->next = NULL;
            prev->next = current;
            prev = current;
        }
    }
    prev->next = NULL;

    current = head;
    for (i = 0; i<n; i++) {
        printf("%d\n", current->x);
        free(current);
        current = current->next;
    }
}


