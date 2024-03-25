#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void main(){

    int n, pid;

    printf("Enter the value of n: ");
    scanf("%d", &n);

    FILE *outputFile = fopen("processes.csv", "w");

    fprintf(outputFile, "%d,%d\n", getppid(), getpid());

    for(int i=1; i<=n; i++){
        fflush(outputFile);
        pid = fork();
        if (pid == 0){
            fprintf(outputFile, "%d,%d\n", getppid(), getpid());
        }
    }

    sleep(1);

    fclose(outputFile);
}