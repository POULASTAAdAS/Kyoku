#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main()
{
    int n, i;

    printf("Enter the number of elements: ");
    scanf("%d", &n);

    double *numb = malloc(n * sizeof(double));
    if (numb == NULL)
    {
        fprintf(stderr, "Memory allocation failed.\n");
        exit(1);
    }

    double sum = 0.0;

    printf("Enter %d numbers:\n", n);
    for (i = 0; i < n; i++)
    {
        scanf("%lf", &numb[i]);
        sum += numb[i];
    }

    double mean = sum / n;

    double vs = 0.0;
    for (i = 0; i < n; i++)
        vs += (numb[i] - mean) * (numb[i] - mean);

    double variance = vs / n;
    double sd = sqrt(variance);

    printf("Standard Deviation = %.2lf\n", sd);

    return 0;
}