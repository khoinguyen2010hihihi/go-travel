#include <stdio.h>
#include <stdlib.h>

const int MAX_SIZE = 100;

struct ElementType {
    char *name;
    unsigned long ID;
};

typedef struct ElementType *Student;

struct ListInfo {
    unsigned long count;
    unsigned long max_size;
    Student *list;
};

typedef struct ListInfo *StudentList;

void Insert(StudentList L, Student e, int p) {
    if (p > L->count || L->count == L->max_size) {
        printf("Invalid insertion position or list full.\n");
        return;
    }

    for (int i = L->count; i > p; i--) {
        L->list[i] = L->list[i - 1];
    }

    L->list[p] = e;
    L->count++;
}

void Show(StudentList L) {
    printf("List of Students:\n");
    for (int i = 0; i < L->count; i++) {
        printf("*** %d ID = %lu\tName : %s\n", i + 1, L->list[i]->ID, L->list[i]->name);
    }
}

int main() {
    StudentList stdList = (StudentList)malloc(sizeof(struct ListInfo));
    stdList->max_size = 60;
    stdList->count = 0;
    stdList->list = (Student *)malloc(stdList->max_size * sizeof(Student));

    Student std1 = (Student)malloc(sizeof(struct ElementType));
    std1->name = "Tran Xuan Bach";
    std1->ID = 102230058;

    Student std2 = (Student)malloc(sizeof(struct ElementType));
    std2->name = "Phan Tran Chi Bao";
    std2->ID = 102230059;
    
    Insert(stdList, std1, 0);
    Insert(stdList, std2, 1);



    Show(stdList);

    for (int i = 0; i < stdList->count; i++) {
        free(stdList->list[i]);
    }
    free(stdList->list);
    free(stdList);

    return 0;
}

