#include <stdio.h>

#define MAX 5

int _index = -1;
int stack[MAX];

int isFull()
{
    if (_index + 1 == MAX)
        return 1;
    else
        return 0;
}

int isEmpty()
{
    if (_index < 0)
        return 1;
    else
        return 0;
}

void push(int data)
{
    if (isFull())
    {
        printf("stack is full\nCan't push %d\n", data);
        return;
    }

    stack[++_index] = data;
}

int pop()
{
    if (isEmpty())
    {
        printf("stack is empty\n");
        return -1;
    }

    return stack[_index--];
}

int peek()
{
    if (isEmpty())
    {
        printf("stack is empty");
        return -1;
    }

    return stack[_index];
}

int main()
{
    push(10);
    push(20);
    push(30);
    push(40);
    push(50);
    push(60);

    int value;

    value = peek();
    if (value != -1)
        printf("peek: %d\n", value);

    value = pop();
    if (value != -1)
        printf("pop: %d\n", value);
    value = pop();
    if (value != -1)
        printf("pop: %d\n", value);
    value = pop();
    if (value != -1)
        printf("pop: %d\n", value);
    value = pop();
    if (value != -1)
        printf("pop: %d\n", value);
    value = pop();
    if (value != -1)
        printf("pop: %d\n", value);
    value = pop();
    if (value != -1)
        printf("pop: %d\n", value);

    return 0;
}