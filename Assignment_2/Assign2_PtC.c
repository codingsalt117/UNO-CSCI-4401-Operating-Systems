#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h> 

void main(){

    int n, pid;

    printf("Enter the value of n: ");
    scanf("%d", &n);

    FILE *outputFile = fopen("processes_partC.csv", "w");

    fprintf(outputFile, "%d,%d\n", getppid(), getpid());

    for(int i=1; i<=n; i++){
        fflush(outputFile);
        //parent_pid = getppid();
        pid = fork();
        if (pid > 0){
            int status;
            waitpid(pid, &status, 0);
            break; 
        } 
        if (pid == 0){
            fprintf(outputFile, "%d,%d\n", getppid(), getpid());
        }    
          
    }
    fclose(outputFile);
}