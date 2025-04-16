#include <stdio.h>
#define MAX 5

int _index = -1;
int queue[MAX];

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

void enqueue(int data)
{
    if (isFull())
    {
        printf("queue is full\nCan't push %d\n", data);
        return;
    }

    queue[++_index] = data;
}

int dequeue()
{
    if (isEmpty())
    {
        printf("queue is empty\n");
        return -1;
    }

    int data = queue[0];

    // shift by 1
    for (int i = 0; i < _index; i++)
    {
        queue[i] = queue[i + 1];
    }
    _index--;

    return data;
}

int peek()
{
    if (isEmpty())
    {
        printf("queue is empty");
        return -1;
    }

    return queue[0];
}

int main()
{
    enqueue(10);
    enqueue(20);
    enqueue(30);
    enqueue(40);
    enqueue(50);
    enqueue(60);

    int value;

    value = peek();
    if (value != -1)
        printf("peek: %d\n", value);

    value = dequeue();
    if (value != -1)
        printf("dequeue: %d\n", value);
    value = dequeue();
    if (value != -1)
        printf("dequeue: %d\n", value);
    value = dequeue();
    if (value != -1)
        printf("dequeue: %d\n", value);
    value = dequeue();
    if (value != -1)
        printf("dequeue: %d\n", value);
    value = dequeue();
    if (value != -1)
        printf("dequeue: %d\n", value);
    value = dequeue();
    if (value != -1)
        printf("dequeue: %d\n", value);

    return 0;
}